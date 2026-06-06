package com.htht.executor.task.service.base.impl;


import com.htht.executor.task.constant.BaseProductServiceConstant;
import com.htht.executor.task.constant.IssueConstant;
import com.htht.executor.task.service.BaseProductHandlerService;
import com.htht.executor.task.service.excel.ExcelDataOperatorService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.entity.paramtemplate.DynamicParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import com.htht.job.core.entity.xml.InputXmlParamDTO;
import com.htht.job.core.enums.FileTypeEnum;
import com.htht.job.core.util.MapObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author: 代国军
 * @CreateDate: 2021/10/26 16:35
 * @Description: 基础插件，输入文件个数为7个
 */
@Service(value="baseBusinessHandlerService")
@Slf4j
public class BaseBusinessHandlerService extends BaseProductHandlerService {

	protected String COAY_Pattern;

	public BaseBusinessHandlerService (){
		this.COAY_Pattern = IssueConstant.NORAML_COAY_PATTERN;
	}

	@Autowired
	private ExcelDataOperatorService<Object> excelDataOperatorService;

	@Override
	public String addInputXmlParam(TriggerParam triggerParam, ProductParam productParam, InputXmlParamDTO inputXmlParam) {
		String issue = triggerParam.getExecutorParams();
		String cycle = productParam.getCycle();
		DynamicParam dynamicParam = MapObjectUtil.mapToObject(triggerParam.getTaskParam().getDynamicMap(),DynamicParam.class);
		log.info("---BaseBusinessHandlerService,获取输入文件信息====={}",issue);
		String inputPath = productParam.getInputFilePath();
		String fileNamePattern = productParam.getFileNamePattern();
		// 如果是年产品,用COAYPattern 替换issue
		if (BaseProductServiceConstant.COAY.equalsIgnoreCase(cycle)){
			issue = StringUtils.replace(issue,issue.substring(4,8),COAY_Pattern);
		}
		// 判断是否为单一文件
		boolean isSingleProduce = BaseProductServiceConstant.SINGLE_PRODUCE.equals(dynamicParam.getIs_single_produce());

		// 判断输入路径是否为文件目录
		boolean isDirectory = BaseProductServiceConstant.IS_DIRECTORY.equals(dynamicParam.getIs_directory());

		//没有正则输入文件只有路径,有正则输入文件为匹配到的具体文件
		String inputInfo = null;
		if (isSingleProduce) {
			inputInfo = super.getInputInfo(issue, fileNamePattern, inputPath, dynamicParam.getTimeRegex(), isDirectory);
		} else {
			inputInfo = super.getInputInfo(issue, fileNamePattern, inputPath, isDirectory);
		}
//		// 非侯产品和月产品的逻辑
//		if (!BaseProductServiceConstant.COAM.equals(cycle)&& !BaseProductServiceConstant.COFD.equals(cycle)) {
//
//		}
//		// 如果是月产品且的时候，需要获取路径下的文件夹
//		if (BaseProductServiceConstant.COAM.equals(cycle)){
//
//			List<File> subFiles = FileUtil.iteratorFileAndDirectory(new File(inputInfo),"^\\d{12}$");
//			inputInfo = subFiles.stream().map(s->s.getPath()).collect(Collectors.joining(","));
//		}
//		// 如果产品为侯产品的话，需要获取之后五天的文件夹路径
//		if (BaseProductServiceConstant.COFD.equals(cycle)){
//			List<String> childInfoList = new ArrayList<>();
//			String issueTime = issue.substring(0,8);
//			Date beginTime = DateUtil.strToDate(issueTime, DateConstant.YYYYMMDD);
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(beginTime);
//			calendar.add(Calendar.DAY_OF_YEAR, 4);
//			Date endTime = calendar.getTime();
//			calendar.setTime(beginTime);
//			while(calendar.getTimeInMillis() <= endTime.getTime()){
//				String childIssue = MatchTime.matchIssue(calendar.getTime(), BaseProductServiceConstant.COOD);
//				log.info("ChildIssue:{}",childIssue);
//				if (isSingleProduce) {
//					inputInfo = super.getInputInfo(issue, fileNamePattern, inputPath, dynamicParam.getTimeRegex(), isDirectory);
//				}else {
//					inputInfo = super.getInputInfo(childIssue, fileNamePattern, inputPath, isDirectory);
//				}
//				if (inputInfo != null) {
//					childInfoList.add(inputInfo);
//				}
//				calendar.add(Calendar.DAY_OF_YEAR, 1);
//			}
//			if (childInfoList.size() == 5){
//				inputInfo = childInfoList.stream().collect(Collectors.joining(","));
//			}
//		}

		if(StringUtils.isEmpty(inputInfo)){
			return BaseProductServiceConstant.No_Input_Msg + ":" + inputPath;
		}
		inputXmlParam.setInputFile(inputInfo);
		return null;
	}


	/**
	 * 默认保存文件内容（excel）
	 * @param filePath 文件路径
	 * @param fileType 文件类型
	 * @param regionId  区域ID
	 */
	@Override
	public void saveStatisticDataToDb(String filePath, String fileType, String regionId) {
		if (!Objects.isNull(filePath)&& !Objects.isNull(fileType)){
			if (FileTypeEnum.XLS.getCode().equalsIgnoreCase(fileType)){
				excelDataOperatorService.saveExcelDataToDbAsy(filePath,2, Object.class);
			}
		}
	}

}
