package com.htht.executor.task.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.htht.executor.geoserver.GeoServerDto;
import com.htht.executor.geoserver.GeoServerPublish;
import com.htht.executor.product.entity.*;
import com.htht.executor.product.service.ProductFileInfoService;
import com.htht.executor.product.service.ProductInfoService;
import com.htht.executor.product.service.ProductService;
import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.constant.CycleConstant;
import com.htht.executor.task.issue.IssueTimeShard;
import com.htht.executor.task.issue.IssueTimeShardContext;
import com.htht.executor.task.log.ScheduleJobHelper;
import com.htht.executor.task.util.PostgreSQLUtils;
import com.htht.executor.task.util.ProductUtil;
import com.htht.executor.transaction.DataSourceTransactionals;
import com.htht.job.core.biz.model.ReturnT;
import com.htht.job.core.biz.model.TaskParam;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.DynamicParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.entity.xml.OutputXmlParamDTO;
import com.htht.job.core.entity.xml.XmlDTO;
import com.htht.job.core.exception.CommonException;
import com.htht.job.core.log.XxlJobFileAppender;
import com.htht.job.core.redis.RedisService;
import com.htht.job.core.shard.SpringContextUtil;
import com.htht.job.core.util.*;
import com.htht.job.core.xml.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author piesat
 */
@Slf4j
public abstract class BaseProductHandlerService implements BaseProductInterface {

	@Value("${agentPath}")
	private String dicPath;

	@Autowired
	private ProductInfoService productInfoService;

	@Autowired
	private ProductFileInfoService productFileInfoService;

	@Autowired
	private ProductService productService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private IssueTimeShardContext issueTimeShardContext;

	@Autowired
	private GeoServerPublish geoServerPublish;

	@Autowired
	private ProductUtil productUtil;

	// 判断当前执行服务器类型,windows 或者 Linux 服务器
	private static boolean isWin;

	private String COAY_Pattern;

	static {
		isWin = true;
		String machineOs = System.getProperty("os.name").toLowerCase();
		if (!machineOs.startsWith("win")) isWin = false;
	}

	protected BaseProductHandlerService baseBusinessHandlerService;

	/**
	 * 重写接口中的分片方法
	 * @param taskParam
	 * @return
	 */
	@Override
	public ReturnT<List<String>> executeShard(TaskParam taskParam) {
		baseBusinessHandlerService = (BaseProductHandlerService)SpringContextUtil.getBeanByType(getClass());
		ReturnT<List<String>> result = new ReturnT<>();
		// json格式 模板参数
		String param = taskParam.getModelParameters();
		//将json格式的params转换为ProductParam类型
		ProductParam productParam = JSON.parseObject(param, ProductParam.class);
		//时间参数的数据类型，now和history
		String dateType = productParam.getDateType().toLowerCase();
		Optional<IssueTimeShard> issueTimeShard = Optional.of(issueTimeShardContext.getIssueShardByType(dateType));
		List<String> issues = issueTimeShard.map(i->i.getIssueListByTimeRange(productParam)).orElse(new ArrayList<>());
		// 根据是否重做标识来判断进行产品记录清除 2022-06-07
//		this.cleanProductInfoAndFileInfoRecords(taskParam, productParam, issues);
		// 根据产品id和cycle周期过滤已经生产的issues,避免issue的重复生产调度
		List<String> productIssues = productInfoService.queryDbIssueList(taskParam.getProductId(),productParam.getCycle(),productParam.getSatellite());
		List<String> shardIssues = issues.stream().filter(s -> !productIssues.contains(s)).collect(Collectors.toList());
		result.setData(shardIssues);
		result.setCode(ReturnT.SUCCESS_CODE);
		log.info("=============总共需要处理：[{}]期数据=================",shardIssues.size());
		return result;
	}

	/**
	 * 根据清除标识决定是否对已生成的产品进行记录清除
	 * @param taskParam
	 * @param productParam
	 * @param issues
	 */
	@Transactional(transactionManager = "mysqlTransactionManager")
	public void cleanProductInfoAndFileInfoRecords(TaskParam taskParam, ProductParam productParam, List<String> issues) {
		if (taskParam.isRetryTask() && !issues.isEmpty()) {
			List<String> productInfoIds = productInfoService.list(new LambdaQueryWrapper<ProductInfoEntity>()
					.eq(ProductInfoEntity::getProductId, taskParam.getProductId())
					.eq(ProductInfoEntity::getCycle, productParam.getCycle())
			        .between(ProductInfoEntity::getIssue, issues.get(0), issues.get(issues.size()-1)))
					.stream().map(BaseEntity::getId).collect(Collectors.toList());
			if (productInfoService.removeByIds(productInfoIds))
				productFileInfoService.remove(new LambdaQueryWrapper<ProductFileInfoEntity>().in(ProductFileInfoEntity::getProductInfoId, productInfoIds));
		}
	}

	/**
	 * 任务调度执行
	 * @param triggerParam
	 * @return result
	 */
	@Override
	public ReturnT<String> execute(TriggerParam triggerParam) {
		if (baseBusinessHandlerService == null) {
			baseBusinessHandlerService = (BaseProductHandlerService) SpringContextUtil.getBeanByType(getClass());
		}
		ReturnT<String> result = ReturnT.SUCCESS;
		/** 1.获取参数列表 **/
		ProductParam productParam = JSON.parseObject(triggerParam.getTaskParam().getModelParameters(), ProductParam.class);
		// 转为动态参数对象
		DynamicParam dynamicParam = MapObjectUtil.mapToObject(triggerParam.getTaskParam().getDynamicMap(),DynamicParam.class);
		/** 2.解析产品参数 **/
		//shard 拿到需要处理的期次信息
		String issue = triggerParam.getExecutorParams();
		// 根据产品类型，对年产品期次进行处理
		if (BaseProductServiceConstant.COAY.equalsIgnoreCase(productParam.getCycle()))
			issue = StringUtils.replace(issue, issue.substring(4, 8), COAY_Pattern);
		issue = getIssueFromInputFile(issue);
		// 通过产品id 查询到产品对应的信息
		ProductEntity productInfo = productService.getById(triggerParam.getTaskParam().getProductId());
		// 算法执行路径
		String exePath = productParam.getExePath();
		// 产品周期
		String cycle = productParam.getCycle();
		// 产品标识 （输入参数产品标识为空时，产品标识选mark）
		String productKey = productInfo.getMark();
		productKey = StringUtils.isNotBlank(dynamicParam.getProductKey())?dynamicParam.getProductKey():productKey;
		productInfo.setMark(productKey);
		// redis 任务key
		String taskKey = null;

		try{
			String querySatellite = productParam.getSatellite();
			String querySensor = productParam.getSensor();
			// 日志缓存temp
			String pathTemp = productKey +File.separator + issue.substring(0,8) +File.separator + productKey + "_" + issue + "_" + cycle;
			// 生成日志路径
			String outPutLogPath = XxlJobHelper.getJobLogFileName();
			//往日志文件以及数据库中添加日志信息
			ScheduleJobHelper.logToFileAndDb("开始执行",true);
			// redis中存储的key(分组存储,按照产品标识分组)
			taskKey = createTaskKey(issue, cycle, productKey, querySatellite, querySensor);

			//保存到redis记录中并检验重复性
			saveRedisRecords(taskKey,issue,cycle);

			//验证产品是否生产
			productInfo.setMark(productKey);
			validateProductExist(issue, productInfo, cycle, querySatellite, querySensor);

			InputXmlParamDTO inputXmlParam = createInputXmlParam(productParam, issue);
			// 扫描文件目录,无效路径后返回
			this.scanFiles(triggerParam, productParam, inputXmlParam);

			String outputXml = productParam.getXmlPath() + File.separator + "outputXml" + File.separator + pathTemp  + ".xml";

			// 创建outputXmlParam
			OutputXmlParamDTO outputXmlParam = createOutputXmlParam(productParam, productInfo, outPutLogPath, outputXml,dynamicParam);

			// 创建inputXml
			String inputXml = this.createInputXml(productParam, productKey, pathTemp,inputXmlParam, outputXmlParam,issue);

			// 执行算法脚本
			BaseProductHandlerService.executeAlgorithm(exePath, inputXml);

//			outputXml = "D:\\HTHT\\xml\\SnowDepth_H8_202208211200_COOH.xml";
			// 判断是否需要入库（0 不入库 1 入库）
			if (!BaseProductServiceConstant.ZERO.equals(dynamicParam.getIsToDb())) {
				// 算法输出结果检验，检验outPutXml文件
				validateOutPutXmlState(taskKey, outputXml);
				// 调用入库方法
				this.saveProductAndMosaicFile(productInfo,inputXmlParam,outputXmlParam, productKey,dynamicParam,productParam);
			}
			result.setMsg("成功,执行完毕");
			result.setCode(ReturnT.SUCCESS_CODE);
			ScheduleJobHelper.logToFileAndDb("执行成功",true);
		}catch(Exception e){
			throw new CommonException("产品生产出错："+ e.getMessage());
		}finally{
			XmlUtils.removeLocal();
			if(!StringUtils.isEmpty(taskKey)) redisService.remove(taskKey);
		}
		return result;
	}


	/**
	 * 检验算法输出结果的合理性
	 * @param taskKey
	 * @param outputXml
	 * @return
	 */
	private void validateOutPutXmlState(String taskKey, String outputXml) {
		File outputXmlFile = new File(outputXml);
		if (!outputXmlFile.exists()) {
			ScheduleJobHelper.logToFileAndDb("outputXmlFile文件不存在，路径为：" + outputXml,true);
			redisService.remove(taskKey);
			throw new CommonException("outputXmlFile文件不存在，入库失败");
		}
		XxlJobHelper.log("开始读取输出xml文件,输出xml路径为:"+outputXml);
		if (!XmlUtils.isSuccessByXml(outputXml)) {
			ScheduleJobHelper.logToFileAndDb("outputXmlFile文件不存在，路径为：" + outputXml,true);
			throw new CommonException("outPutXml显示算法失败" + outputXml);
		}
	}

	/**
	 * 调用算法脚本
	 * @param exePath
	 * @param inputXml
	 */
	private static void executeAlgorithm(String exePath, String inputXml) throws Exception {
		if (StringUtils.isBlank(exePath)) throw new CommonException("算法挂载为空");
		int executorTimeout = XxlJobHelper.getTriggerParam().getExecutorTimeout();
		int timeout =  executorTimeout == 0 ? 2 * 60 * 60 : executorTimeout;
		XxlJobHelper.log("开始执行算法：");
		ScriptUtil.execute(exePath,inputXml,String.valueOf(timeout));
	}


	/**
	 * 创建任务执行唯一标识key
	 * @param issue
	 * @param cycle
	 * @param productKey
	 * @param querySatellite
	 * @param querySensor
	 * @return
	 */
	private static String createTaskKey(String issue, String cycle, String productKey, String querySatellite, String querySensor) {
		String taskKey;
		taskKey = productKey + "::" + cycle + issue +"_"+ querySatellite + "_" + querySensor;
		log.info("---BaseProductHandlerService--taskKey::"+taskKey);
		// 添加redisKey,用于任务回调时清除记录
		XxlJobHelper.setRedisKey(taskKey);
		return taskKey;
	}

	/**
	 * 保存任务处理记录到redis中
	 * @param taskKey
	 * @param issue
	 * @param cycle
	 * @return
	 */
	private void saveRedisRecords(String taskKey, String issue, String cycle){
		//任务正在执行中
		if(redisService.exists(taskKey)){
			ScheduleJobHelper.logToFileAndDb("该期次产品制作中" + cycle + "_" + issue,true);
			throw new CommonException("产品制作中" + cycle + "_" + issue);
		}
		redisService.set(taskKey, taskKey);
	}

	/**
	 * 验证该期次产品是否制作存在
	 * @param issue
	 * @param productInfo
	 * @param cycle
	 * @param querySatellite
	 * @param querySensor
	 * @return
	 */
	private void validateProductExist(String issue, ProductEntity productInfo, String cycle, String querySatellite, String querySensor) {
		ProductInfoEntity queryDto = createProductInfoDto(issue, productInfo, cycle, querySatellite, querySensor);
		// 数据库去重
		boolean isExits = productInfoService.findProductExits(queryDto);
		if(isExits){
			ScheduleJobHelper.logToFileAndDb("该期次产品已入库" + cycle + "_" + issue,true);
			throw new CommonException("产品已经制作过" + cycle + "_" + issue);
		}
	}


	/**
	 * 创建产品任务输入参数对象
	 * @param productParam
	 * @param issue
	 * @return
	 */
	private InputXmlParamDTO createInputXmlParam(ProductParam productParam, String issue) {
		InputXmlParamDTO inputXmlParam  = new InputXmlParamDTO();
		inputXmlParam.setIssue(issue);
		inputXmlParam.setCycle(productParam.getCycle());
		inputXmlParam.setAreaID(productParam.getAreaID());
		return inputXmlParam;
	}

	/**
	 * 生成算法脚本需要的xml 文件
	 * @param productParam
	 * @param productKey
	 * @param pathTemp
	 * @param inputXmlParam
	 * @param outputXmlParam
	 * @return
	 */
	private String createInputXml(ProductParam productParam, String productKey, String pathTemp, InputXmlParamDTO inputXmlParam, OutputXmlParamDTO outputXmlParam,String issue) {
		Map<String,Object> outputXmlParamMap = MapObjectUtil.objectToMap(outputXmlParam);
		Map<String,Object> inputXmlParamMap = MapObjectUtil.objectToMap(inputXmlParam);
		if (inputXmlParamMap.containsKey("otherMap")){
			JSONObject otherMapObj = (JSONObject) inputXmlParamMap.get("otherMap");
			Map otherMap = MapObjectUtil.objectToMap(otherMapObj);
			if (otherMap.size()>0) {
				inputXmlParamMap.putAll(otherMap);
				inputXmlParamMap.remove("otherMap");
			}
		}
		// 添加算法参数
		// 处理 VNP 产品的 算法参数
		LinkedHashMap<String, Object> fixedMap = executeFixMap(issue);
		inputXmlParamMap.putAll(fixedMap);
		List<XmlDTO> inputList = XmlUtils.formatXmlParam(inputXmlParamMap);
		List<XmlDTO> outputList = XmlUtils.formatXmlParam(outputXmlParamMap);

		String inputXml = productParam.getXmlPath() + File.separator + "inputXml" + File.separator + pathTemp + ".xml";
		XmlUtils.createAlgorithmXml(productKey, inputList,outputList, inputXml);
		ScheduleJobHelper.logToFileAndDb("生成输入xml,输入xml路径为:"+inputXml,true);
		return inputXml;
	}

	/**
	 * 创建产品任务输出参数对象
	 * @param productParam
	 * @param productInfo
	 * @param outPutLogPath
	 * @param outputXml
	 * @param dynamicParam
	 * @return
	 */
	private OutputXmlParamDTO createOutputXmlParam(ProductParam productParam, ProductEntity productInfo, String outPutLogPath, String outputXml, DynamicParam dynamicParam) {
		// 创建输出文件outputXml
		boolean flag = XxlJobFileAppender.makeFileNameByPath(outputXml);
		ScheduleJobHelper.logToFileAndDb("创建"+outputXml+"是否成功:"+flag,true);
		//输出路径 页面输入参数有输出路径 输出路径从页面拿
		String outFolder = StringUtils.isEmpty(dynamicParam.getOutFolder())?productInfo.getProductPath():dynamicParam.getOutFolder();
		return addOutputXmlParam(outPutLogPath, outputXml, outFolder, productParam.getTempPath());
	}

	/**
	 * 扫描文件夹路径，判断文件是否存在
	 * @param triggerParam
	 * @param productParam
	 * @param inputXmlParam
	 * @return
	 */
	private void scanFiles(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
		// 添加输入参数的补充参数
		log.info("------> 扫描文件");
		String inputMsg = addInputXmlParam(triggerParam, productParam, inputXmlParam);
		if(StringUtils.isNotBlank(inputMsg)){
			ScheduleJobHelper.logToFileAndDb(inputMsg,true);
			throw new CommonException(inputMsg);
		}
		ScheduleJobHelper.logToFileAndDb("文件扫描成功",true);
	}


	/**
	 * 添加输出xml参数
	 * @param outPutLogPath
	 * @param outputXml
	 * @param outputPath
	 * @param temp
	 * @return
	 */
	private OutputXmlParamDTO addOutputXmlParam(String outPutLogPath, String outputXml, String outputPath, String temp) {
		OutputXmlParamDTO outputXmlParam = new OutputXmlParamDTO();
		outputXmlParam.setTemp(temp);
		outputXmlParam.setOutXMLPath(outputXml);
		outputXmlParam.setOutLogPath(outPutLogPath);
		outputXmlParam.setOutFolder(outputPath);
		return outputXmlParam;
	}

	/**
	 * 创建产品查询对象
	 * @param issue
	 * @param productInfo
	 * @param cycle
	 * @param querySatellite
	 * @param querySensor
	 * @return
	 */
	private static ProductInfoEntity createProductInfoDto(String issue, ProductEntity productInfo, String cycle, String querySatellite, String querySensor) {
		ProductInfoEntity queryDto = new ProductInfoEntity();
		queryDto.setIssue(issue);
		queryDto.setProductId(productInfo.getId());
		queryDto.setCycle(cycle);
		queryDto.setMark(productInfo.getMark());
		queryDto.setSatellite(querySatellite);
		queryDto.setSensor(querySensor);
		return queryDto;
	}


	/**
	 * 处理shard中处理数据返回结果不是期次的情况
	 * @param issue
	 * @return
	 */
	public String getIssueFromInputFile(String issue){
		return issue;
	}


	/**
	 * 产品信息入库
	 * @param product
	 * @param inputXmlParam
	 * @param outputXmlParam
	 * @param identify
	 */
	@DataSourceTransactionals(transactionManagers = {"pgsqlTransactionManager","mysqlTransactionManager"})
	private void saveProductAndMosaicFile(ProductEntity product, InputXmlParamDTO inputXmlParam, OutputXmlParamDTO outputXmlParam, String identify, DynamicParam dynamicParam, ProductParam productParam) throws Exception {
		ScheduleJobHelper.logToFileAndDb("读取xml准备产品数据入库",true);
		String issue = inputXmlParam.getIssue();
		String cycle = inputXmlParam.getCycle();
		String outputXml = outputXmlParam.getOutXMLPath();
		String inputXml = outputXml.replace("outputXml", "inputXml");

		//读取xml，把xml转换成map对象
		Map<String,List<Element>> map = XmlUtils.outputFilesXmlToMap(outputXml);
		//获取所有的行政区域
		List<String> regionIdList = XmlUtils.getXmlAttrVal( map, "region", BaseProductServiceConstant.Input_identify);

		String mosaicFile = "";
		if(map.containsKey("mosaicFile")){
			List<String> mosaicFiles = XmlUtils.getXmlAttrFileElementVal(map,"mosaicFile");
			mosaicFile = CollectionUtils.isNotEmpty(mosaicFiles)? mosaicFiles.get(0):"";
		}
		final boolean flag = false;
		Map<String,String> regionProductMap = new HashMap<>(16);
		//产品文件结果入库
		if (CollectionUtils.isNotEmpty(regionIdList)) {
			String dictCode = dicPath;
			saveProductInfo(product, issue, cycle, inputXml, dictCode, map, regionIdList, mosaicFile,regionProductMap,dynamicParam,productParam);
			ScheduleJobHelper.logToFileAndDb( "产品文件结果入库成功",true);
			// 发布imageMosaic图层
//			flag = publishImageMosaicLayer(dynamicParam, false);
		}
		//产品geoSQL结果结果入库
		//含有除了空格之外的字符，返回true，否则返回false
		// 首次发布成功后,pgsql 记录会自动入库，无需重新入库
		String pgsqlTab = StringUtils.isBlank(dynamicParam.getPgsqlTab())? identify: dynamicParam.getPgsqlTab();
		savePostgresInfo(pgsqlTab, issue, cycle, map,mosaicFile);
		// 产品统计结果入库
//		saveStatisticList(outputXml, product,cycle,productParam);
	}

	/**
	 * 发布Geoserver多时项镶嵌数据图层
	 * @param dynamicParam
	 * @param flag
	 * @return
	 */
	private boolean publishImageMosaicLayer(DynamicParam dynamicParam, boolean flag) {
		if (StringUtils.isNotBlank(dynamicParam.getPublishPath())){
			GeoServerDto geoServerDto = createGeoServerDto(dynamicParam);
			String imageMosaicKey = geoServerDto.getWorkspace()+geoServerDto.getStoreName();
			// 当redis 中不存在imageMosaicKey任务，或者状态为false的时候，调用发布方法
			if (!(boolean) redisService.get(imageMosaicKey) || !redisService.exists(imageMosaicKey))
				flag = geoServerPublish.publishImageMosaic(geoServerDto);
			if (flag) {
				XxlJobHelper.log("产品图层服务发布成功");
				redisService.set(imageMosaicKey,true);
			}else{
				XxlJobHelper.handleFail("产品图层服务发布失败");
				redisService.set(imageMosaicKey,false);
			}
		}
		return flag;
	}


	/**
	 * 创建GeoServer对象
	 * @param dynamicParam
	 * @return
	 */
	private static GeoServerDto createGeoServerDto(DynamicParam dynamicParam) {
		GeoServerDto geoServerDto = new GeoServerDto();
		File file = new File(dynamicParam.getPublishPath());
		geoServerDto.setWorkspace(dynamicParam.getWorkspace());
		geoServerDto.setStoreName(dynamicParam.getStoreName());
		geoServerDto.setZipFile(file);
		return geoServerDto;
	}

	/**
	 * 生成日志路径
	 * @param productParam
	 * @param pathTemp
	 * @return
	 */
	private static String createOutPutLogPath(ProductParam productParam, String pathTemp) {
		String outPutLogPath = productParam.getLogPath() + File.separator + pathTemp +"_"+System.currentTimeMillis()+ ".log";
		XxlJobFileAppender.makeFileNameByPath(outPutLogPath);
		log.info("生成日志路径{}",outPutLogPath);
		return outPutLogPath;
	}

	/**
	 * 产品统计结果入库
	 * @param outputXml
	 * @param product
	 * @param cycle
	 */
	private void saveStatisticList(String outputXml, ProductEntity product, String cycle, ProductParam productParam){
		List<Element> xmlList = XmlUtils.getTablenameElements(outputXml, "table");

		if (CollectionUtils.isEmpty(xmlList)) ScheduleJobHelper.logToFileAndDb("统计表信息为空，不进行统计入库 ;", true);
		else {
			String proSatellite = productParam.getSatellite();
			String proSensor = productParam.getSensor();
			saveTableInfo(xmlList, product.getId(),proSatellite,proSensor,cycle);
			ScheduleJobHelper.logToFileAndDb( "产品统计结果入库;",true);

		}
	}

	/**
	 * 产品productInfo与文件详细productFileInfo 入库
	 * @param product
	 * @param issue
	 * @param cycle
	 * @param inputXml
	 * @param dictCode
	 * @param map
	 * @param regionIdList
	 * @param mosaicFile
	 * @param regionProductMap
	 * @param dynamicParam
	 * @throws Exception
	 */
	private void saveProductInfo(ProductEntity product,
								 String issue, String cycle, String inputXml,
								 String dictCode, Map<String, List<Element>> map,
								 List<String> regionIdList, String mosaicFile, Map<String, String> regionProductMap, DynamicParam dynamicParam, ProductParam productParam) throws Exception {

		//产品手动发布，自动发布（默认手动发布）
		Integer isRelease = product.getIsRelease()==null?1:product.getIsRelease();
		String productId = Optional.of(product).map(ProductEntity::getId).get();
		for (String regionId : regionIdList) {
			//分辨率字段不为Null
			ProductInfoEntity productInfo;
			// 保存到产品表中
			try {
				productInfo = productUtil.saveProductInfo(dynamicParam, product, regionId, issue, cycle, mosaicFile, inputXml,isRelease,productParam);
			} catch (Exception e) {
				log.error("saveProductInfo" +e.getMessage());
				ScheduleJobHelper.handleFail( "regionId:"+regionId+"->产品表入库异常");
				throw new Exception(e.getMessage());
			}

			String productInfoId = Optional.of(productInfo).map(ProductInfoEntity::getId).get();

			// 统计区域和产品的关系
			regionProductMap.put(regionId,productId);

			List<String> lFiles = XmlUtils.getXmlAttrFileElementVal(map,
					"region", BaseProductServiceConstant.Input_identify, regionId);
			//保存到产品文件表中
			for (String file : lFiles) {
				String filePath = null, fileType = null;
				try {
					filePath = file.replace("\\", "/");
					if (FileUtil.checkFile(filePath)){
						fileType = new File(filePath).getName().split("\\.")[1];
						productUtil.saveProductInfoFile(productId,productInfoId, filePath, dictCode, regionId, issue, cycle);
					}
				} catch (Exception e) {
					log.error("saveProductInfoFile" +e.getMessage());
					ScheduleJobHelper.handleFail("regionId:"+regionId+"-filePath:"+filePath+"存储异常");
					throw new Exception(e.getMessage());
				}
				try {
					boolean isSaveStatistic = BaseProductServiceConstant.ITERATOR.equals(dynamicParam.getSaveStatistic()) ? true: false;
					// 是否保存统计数据到表中
					if (isSaveStatistic) saveStatisticDataToDb(filePath, fileType, regionId);
				} catch (Exception e) {
					log.error("saveStatisticDataToDb:"+e.getMessage());
					throw new Exception(e.getMessage());
				}
			}
		}
	}

	/**
	 *  tif 结果入库
	 * @param identify
	 * @param issue
	 * @param cycle
	 * @param map
	 * @param mosaicFile
	 */
	private static void savePostgresInfo(String identify,
										 String issue, String cycle, Map<String, List<Element>> map,
										 String mosaicFile) {
		try{

			log.info("--savePostgresInfo--identify="+identify+"--issue="+issue+"--cycle="+cycle+"--mosaicFile="+mosaicFile);
			boolean isHaveTable = PostgreSQLUtils.getTableName(identify);
			log.info("--savePostgresInfo--isHaveTable="+isHaveTable);
//			boolean extentFlag = map.containsKey("extent");
//			log.info("--savePostgresInfo--map.containsKey(extent):"+extentFlag);
			if (isHaveTable) {
				// 为了postgresSQL的功能，入库时把日期增加了8小时。
				Calendar calendar = Calendar.getInstance();
				Date date = DateUtil.strToDate(issue, DateConstant.YYYYMMDDHHMM);
				calendar.setTime(date);
				calendar.add(Calendar.HOUR_OF_DAY, 8);
				String issueDate = DateUtil.formatDateTime(calendar.getTime(),
						DateConstant.YYYY_MM_DD_HH_MM_SS) + "." + CycleConstant.GeoCycleMap.get(cycle);
				String coordinate = PostgreSQLUtils.selectCoordinate(identify);
				if(StringUtils.isBlank(coordinate)) coordinate = "4326";
				String sql = "INSERT INTO public.\""
						+ identify
						+ "\"( the_geom, location, ingestion, elevation) VALUES "
						+ "('" + coordinate + "','" + mosaicFile + "','" + issueDate
						+ "', null)";
				boolean savFlag = PostgreSQLUtils.save(sql);
				log.info("--savePostgresInfo--savFlag="+savFlag);
				XxlJobHelper.log("栅格数据入库成功");
			}
		}catch(Exception e){
			XxlJobHelper.handleFail( "栅格数据入库失败，"+e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 产品统计信息入库
	 * @param xmlList
	 * @param productId
	 * @param satellite
	 * @param sensor
	 * @param cycle
	 */
	private void saveTableInfo(List<Element> xmlList, String productId, String satellite, String sensor, String cycle) {
		for (Element e : xmlList) {
			// 获取数据集
			List<String> cc = new ArrayList<>();
			List<Element> list2 = e.elements();
			for (Element e2 : list2) if ("values".equals(e2.getName())) cc.add(e2.getText());
			// 封数据执行入库
			ProductAnalysisTableInfo productAnalysisTableInfo =
					new ProductAnalysisTableInfo(e.attribute("identify").getValue());
			String[] fields = (e.element("field").getText().replace("'", "").trim() + ",product_id,satellite,sensor,cycle").split(",");

			for (int i = 0; i < fields.length; i++)
				for (String s : cc) {
					String[] values = (s.replace("'", "") + "," + productId + "," + satellite + "," + sensor + "," + cycle).replace(",", " ").trim().split("\\s+");
					productAnalysisTableInfo.addFieldAndValue(
							fields[i].trim(), values[i]);
				}
			productUtil.saveProductDetail(productAnalysisTableInfo);
		}
	}

	/**
	 * 符合规则下，根据需求输出文件路径（目录）
	 * @param issue
	 * @param regex
	 * @param inputFile1
	 * @param timeRegex
	 * @param isDirectory
	 * @return
	 */
	protected String getInputInfo(String issue, String regex, String inputFile1, String timeRegex, boolean isDirectory) {
		String inputFile = getInputInfo(issue,regex,inputFile1,timeRegex);
		if (isDirectory) inputFile = FileNameUtils.dealFilePath(inputFile1, issue);
		return  inputFile;
	}


	/**
	 * 符合规则下，根据需求输出文件路径（目录）
	 * @param issue
	 * @param regex
	 * @param inputFile1
	 * @param isDirectory
	 * @return
	 */
	protected String getInputInfo(String issue, String regex, String inputFile1, boolean isDirectory) {
		String  inputFile = getInputInfo(issue,regex,inputFile1);
		if (isDirectory) inputFile = FileNameUtils.dealFilePath(inputFile1, issue);
		return  inputFile;
	}



	/**
	 * 根据时间规则，匹配文件名
	 * @param issue
	 * @param regex
	 * @param inputFile
	 * @param timeRegex
	 * @return
	 */
	private String getInputInfo(String issue, String regex, String inputFile, String timeRegex){
		inputFile = FileNameUtils.dealFilePath(inputFile,issue);
		//用issue替换正则中的时间信息,正则为空时直接返回文件路径（兼容不是文件的情况）
		try {
			if (StringUtils.isNotEmpty(regex) && StringUtils.isNotEmpty(timeRegex)) {
				String newRegex = FileNameUtils.dealFileNamePattern(regex, issue);
				newRegex.replaceAll(" ", "");
				inputFile.replaceAll(" ", "");
				log.info("文件正则：-----{}", newRegex);
				List<File> fileList = FileUtil.iteratorFileAndDirectory(new File(inputFile), newRegex);
				log.info("匹配到文件,{}", fileList);
				if (fileList != null && !fileList.isEmpty()) {
					List<String> filePathArr = fileList.stream().filter(file -> FileNameUtils.filterFilePathByTimeRegex(file, timeRegex))
							.map(file -> file.getPath()).collect(Collectors.toList());
					log.info("符合过滤规则文件个数,{}", filePathArr.size());
					if (filePathArr != null && filePathArr.size() > 0) {
						log.info("选择文件路径,{}", filePathArr.get(0));
						return filePathArr.get(0);
					} else {
						log.info("选择文件路径,{}", fileList.get(0).getPath());
						return fileList.get(0).getPath();
					}
				} else return null;
			} else return inputFile;
		}catch (Exception e){
			log.error(inputFile+"文件路径下未匹配到对应的文件");
			return null;
		}
	}



	/**
	 * 根据文件名和正则匹配对应文件信息，如果没有正则则返回对应的文件夹路径
	 * @param issue
	 * @param regex
	 * @param inputFile1
	 * @return
	 */
	protected String getInputInfo(String issue, String regex, String inputFile1) {
		String inputFile = FileNameUtils.dealFilePath(inputFile1,issue);
		log.info("输入文件路径：-----{}",inputFile);
		//用issue替换正则中的时间信息,正则为空时直接返回文件路径（兼容不是文件的情况）
		if(StringUtils.isNotEmpty(regex)){
			String newRegex = FileNameUtils.dealFileNamePattern(regex,issue);
			log.info("文件正则：-----{}",newRegex);
			List<File> fileList = FileUtil.iteratorFileAndDirectory(new File(inputFile), newRegex);
			log.info("匹配到文件,{}",fileList);
			if(!fileList.isEmpty()) {
				return fileList.stream().map(file -> file.getPath().replace("\\", "/")).max(Comparator.comparing(String::toString)).orElse(null);
			}else {
				return null;
			}

		} else {
			return inputFile;
		}
	}
	/**
	 * 根据表头，将数据库查询结果存储到txt中
	 * @param txtCols
	 * @param list
	 * @param filePath
	 */
	protected void saveToTxt(String txtCols, List<Map<String, Object>> list,String filePath) {
		// 表字段集合
		List<String> tableList = Optional.ofNullable(list)
				.orElse(new ArrayList<>())
				.stream()
				.map(item->this.getTableInfo(item,txtCols))
				.collect(Collectors.toList());
		// 集合第一行添加表头
		tableList.add(0,txtCols.replace(","," "));
		try {
//			FileUtils.writeLines(new File(filePath), CharEncoding.UTF_8,tableList,false);
			FileUtils.writeLines(new File(filePath),tableList,false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从map 中读取数据并返回
	 * @param map
	 */
	private String getTableInfo(Map<String, Object> map,String txtCols) {
		List<String> list = Arrays.asList(txtCols.split(","));
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0;i<list.size();i++) {
			stringBuffer.append(map.get(list.get(i)));
			if(i<list.size()-1){
				stringBuffer.append(" ");
			}
		}
		return stringBuffer.toString();
	}

}
