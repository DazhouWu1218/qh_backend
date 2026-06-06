package com.njht.webyun.publish.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.entity.PageEntity;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.model.CurrentUser;
import com.njht.webyun.publish.index.vo.IndexProductCategoryReqVo;
import com.njht.webyun.publish.index.vo.IndexProductReqVo;
import com.njht.webyun.publish.index.vo.IndexReqVo;
import com.njht.webyun.publish.product.constant.ProductConstant;
import com.njht.webyun.publish.product.dao.ProductFileInfoDao;
import com.njht.webyun.publish.product.dao.ProductInfoDao;
import com.njht.webyun.publish.product.dto.ProductInfoDto;
import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.publish.product.service.DataCategoryService;
import com.njht.webyun.publish.product.service.ProductFileInfoService;
import com.njht.webyun.publish.product.service.ProductInfoService;
import com.njht.webyun.publish.product.service.ProductService;
import com.njht.webyun.publish.product.util.IssueUtil;
import com.njht.webyun.publish.product.vo.ProductFuzzySearchReqVo;
import com.njht.webyun.publish.product.vo.ProductFuzzySearchVo;
import com.njht.webyun.publish.product.vo.ProductInfoReqVo;
import com.njht.webyun.publish.product.vo.ProductInfoVo;
import com.njht.webyun.publish.region.service.RegionInfoService;
import com.njht.webyun.publish.sys.service.OrgService;
import com.njht.webyun.publish.sys.service.UserService;
import com.njht.webyun.utils.PageUtil;
import com.njht.webyun.utils.PageUtils;
import com.njht.webyun.utils.Query;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("productInfoService")
@DS(value = DbConstant.MYSQL_1)
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoDao, ProductInfoEntity> implements ProductInfoService {

    @Autowired
    private DataCategoryService dataCategoryService;

    @Autowired
    private ProductFileInfoService productFileInfoService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductFileInfoDao productFileInfoDao;

    @Autowired
    private RegionInfoService regionInfoService;

    @Autowired
    private OrgService orgService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //分页测试
        PageEntity pageEntity = new PageEntity(2,5);
        IPage<ProductInfoEntity> page = this.page(
                new Query<ProductInfoEntity>().getPage(pageEntity),
                new QueryWrapper<ProductInfoEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public Map<String,Object> getProductList(ProductInfoVo productInfoVo) {
        Map<String,Object> returnMap = new HashMap<>(5);
        //获取树结构信息
        Map<String, List<String>> treeMap = dataCategoryService.getProductIdAndNames(productInfoVo.getId());
        //获取最后一级id
        List<String> ids = treeMap.get("ids");
        //根据分类id 获取产品对应的id
        if(CollectionUtils.isNotEmpty(ids)){
            // 分类id 默认只查分类的第一个
            ids = productService.getProductIdList(Arrays.asList(ids.get(0)));
            // ids 为空直接返回
            if(CollectionUtils.isNotEmpty(ids)){
                productInfoVo.setId(ids.get(0));
            }else{
                return new HashMap<>(1);
            }
        }
        //封装分页参数
        PageEntity pageEntity = new PageEntity();
        BeanUtils.copyProperties(productInfoVo,pageEntity);
        // 获取最后一级名称
        if(!Objects.isNull(treeMap.get(ProductConstant.PRODUCT_TREE_NAME))){
            returnMap.put("name",treeMap.get(ProductConstant.PRODUCT_TREE_NAME).get(treeMap.get(ProductConstant.PRODUCT_TREE_NAME).size()-1));
            returnMap = this.getProductInfoList( productInfoVo, pageEntity, returnMap);
        }
        return returnMap;
    }

    /**
     * 查询行政区域集合
     * @param regionId
     * @param geo
     * @return
     */
    private List<String> getRegionList(String regionId,Map geo){
        //行政区域对应信息从用户中取
        List<String> regionList;
        if(StringUtils.isBlank(regionId)){
            //前端没有传行政区域以及地图边界信息，默认查询用户所属的组织结构
            Integer userId = UserUtil.getCurrentUser().getUserId();
            regionId = orgService.queryRegionIdByUserId(userId);
        }
        if(geo != null&&geo.size() !=0){
            regionList = this.getRegionListByGeo(geo,regionId);
        }else {
            regionList = Arrays.asList(regionId);
        }
        return regionList;
    }

    /**
     * 查询产品结果 mybatis子查询
     * @param productInfoVo
     * @param pageEntity
     * @param returnMap
     * @return
     */
    private Map<String,Object> getProductInfoListBak(ProductInfoVo productInfoVo, PageEntity pageEntity,Map<String,Object> returnMap){
        // 行政区域对应信息从用户中取
        List<String> regionList = this.getRegionList(productInfoVo.getRegionId(), productInfoVo.getGeo());
        // 数据源
        List<String> dataSourceList = Optional.ofNullable(productInfoVo.getDataSourceList()).orElse(new ArrayList<>());
        // 周期
        List<String> cycleList = Optional.ofNullable(productInfoVo.getCycleList()).orElse(new ArrayList<>());
        PageUtil.setPageAndSize(pageEntity.getPage(),pageEntity.getSize(),1,10);
        List<ProductInfoDto> dtoList = baseMapper.selectProductInfoList(regionList,getTimeStr(productInfoVo.getStartTime()),
                getTimeStr(productInfoVo.getEndTime()),dataSourceList,cycleList,productInfoVo.getId());
        PageUtils pageUtils = new PageUtils(new PageInfo<>(dtoList));
        //设置返回结果
        returnMap.put("page",pageUtils);
        return returnMap;
    }
    /**
     * 查询产品结果 mybatisPlus
     * @param productInfoVo
     * @param pageEntity
     * @return
     */
    private Map<String,Object> getProductInfoList(ProductInfoVo productInfoVo, PageEntity pageEntity,Map<String,Object> returnMap) {
        IPage<ProductInfoEntity> page = this.getProductInfoEntityIPage(productInfoVo, pageEntity);
        PageUtils pageUtils = new PageUtils(page);
        List<ProductInfoEntity> productInfoEntities = page.getRecords();
        if(CollectionUtils.isEmpty(productInfoEntities)){
            return new HashMap<>(1);
        }
        //设置返回结果
        String name = (String)returnMap.get("name");
        Set<String> dataSourceSet = new TreeSet<>();
        Set<String> cycleSet = new TreeSet<>();
        String finalName = name;
        List<ProductInfoReqVo> collect = productInfoEntities.stream().map(productInfoEntity -> {
            ProductInfoReqVo reqVo = new ProductInfoReqVo();
            BeanUtils.copyProperties(productInfoEntity, reqVo);
            String issue = IssueUtil.getIssueByMark(reqVo.getIssue(),reqVo.getMark());
            reqVo.setIssue(issue);
            String period = this.getPeriod(issue);
            reqVo.setParentName(finalName);
            reqVo.setPeriod(period);
            reqVo.setDataSource(productInfoEntity.getSatellite());
            dataSourceSet.add(productInfoEntity.getSatellite());
            cycleSet.add(CycleTypeEnum.getValue(productInfoEntity.getCycle()));
            reqVo.setCycleName(CycleTypeEnum.getValue(productInfoEntity.getCycle()));
            return reqVo;
        }).collect(Collectors.toList());
        //获取文件数量
        List<ProductInfoReqVo> list = productFileInfoService.getFileNumAndTypeList(collect);
        pageUtils.setList(list);
        //文件类型
        List<String> fileTypeList = list.get(0).getFileTypeList();
        returnMap.put("fileTypeList",fileTypeList);
        name = name+list.get(0).getName()+"--"+ String.join(",", dataSourceSet) +"-"+String.join(",",cycleSet);
        returnMap.put("name",name);
        returnMap.put("page",pageUtils);
        return returnMap;
    }

    @Override
    public IPage<ProductInfoEntity> getProductInfoEntityIPage(ProductInfoVo productInfoVo, PageEntity pageEntity) {
        QueryWrapper<ProductInfoEntity> queryWrapper = new QueryWrapper<>();
        //产品id
        queryWrapper.eq("product_id",productInfoVo.getId());
        //行政区域对应信息从用户中取
        List<String> regionList = this.getRegionList(productInfoVo.getRegionId(), productInfoVo.getGeo());
        queryWrapper.in("region_id",regionList);
        //数据源
        if(CollectionUtils.isNotEmpty(productInfoVo.getDataSourceList()) && !productInfoVo.getDataSourceList().isEmpty()){
            queryWrapper.in("satellite",productInfoVo.getDataSourceList());
        }
        //周期
        if(CollectionUtils.isNotEmpty(productInfoVo.getCycleList()) && !productInfoVo.getCycleList().isEmpty()){
            queryWrapper.in("cycle",productInfoVo.getCycleList());
        }

        // 发布平台只展示以发布的产品
        if(productInfoVo.getIsShow() == null){
            productInfoVo.setIsShow(1);
        }
        // 不是查全部的时候 查询条件带上是否展示
        if(productInfoVo.getIsShow() != 2){
            queryWrapper.eq("is_release",productInfoVo.getIsShow());
        }
        //开始结束时间
        if(StringUtils.isNotEmpty(productInfoVo.getStartTime()) && StringUtils.isNotEmpty(productInfoVo.getEndTime())){
            queryWrapper.between("issue",getTimeStr(productInfoVo.getStartTime()),getTimeStr(productInfoVo.getEndTime()));
        }
        //根据期次排序
        queryWrapper.orderByDesc("issue");
        //返回结果分页
        return this.page(
                new Query<ProductInfoEntity>().getPage(pageEntity),
                queryWrapper
        );
    }

    @Override
    public List<ProductInfoEntity> entityList(List<String> idList) {
        return baseMapper.selectEntityList(idList);
    }

    /**
     * 获取拿“_”分隔开的期次
     * @param issue
     * @return
     */
    private String getPeriod(String issue) {
        return issue.substring(0,8)+"_"+issue.substring(8);
    }

    /**
     * 获取时间字符串信息
     * @param time
     * @return
     */
    private String getTimeStr(String time) {
        String returnTime = null;
        if(StringUtils.isNotBlank(time)){
            returnTime = time.replace("-","").replace(" ","").replace(":","");
        }
        if(returnTime != null && returnTime.length() > ProductConstant.PRODUCT_FILE_INFO_SHIER) {
            return returnTime.substring(0,12);
        }else{
            return returnTime;
        }
    }

    /**
     * 通过地图边界获取行政区域集合
     * @param geo
     * @param regionId
     * @return
     */
    private List<String> getRegionListByGeo(Map geo,String regionId) {
        return regionInfoService.getRegionIdListByGeo(geo,regionId);
    }

    /**
     * 拿到productId 查询产品信息
     * @param id
     * @param page
     * @param size
     * @param regionId
     * @param geo
     * @param isShow
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Map<String,Object> getProductListById(String id, Integer page, Integer size, String regionId,
                                                 Map geo, Integer isShow,String startTime,String endTime) {
        //获取产品分级信息
        //获取最后一级id，以及每一级的产品名称
        Map<String, List<String>> map = dataCategoryService.getProductIdAndNames(id);
        List<String> ids = map.get("ids");
        //根据分类id 获取产品对应的id
        if(CollectionUtils.isNotEmpty(ids)){
            // 分类id 默认只查分类的第一个
            ids = productService.getProductIdList(Arrays.asList(ids.get(0)));
        }else{
            //输入id 为产品id
            ids = Arrays.asList(id);
        }
        if(CollectionUtils.isEmpty(ids)){
            return new HashMap<>(1);
        }
        List<String> names = map.get("names");
        //设置返回结果
        Map<String,Object> returnMap = new HashMap<>(2);
        if(!Objects.isNull(names)){
            //把最后一级的名称过滤掉
            String name = names.get(names.size()-1);
            returnMap.put("name",name);
            //获取期次对应详情列表
            ProductInfoVo productInfoVo = new ProductInfoVo();
            productInfoVo.setId(ids.get(0));
            productInfoVo.setRegionId(regionId);
            productInfoVo.setGeo(geo);
            productInfoVo.setIsShow(isShow);
            if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
                productInfoVo.setStartTime(startTime);
                productInfoVo.setEndTime(endTime);
            }
            returnMap = this.getProductInfoList(productInfoVo, new PageEntity(page, size),returnMap);
        }
        return returnMap;
    }

    @Override
    public List<ProductInfoReqVo> getTifInfoList(List<String> ids) {
        //查询数据信息
        //如果 mosaic_file 为空 则没有tif 服务
        QueryWrapper<ProductInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<ProductInfoEntity> productInfoEntities = this.list(queryWrapper);
        //设置返回结果 并根据期次排序
        return productInfoEntities.stream().map(productInfoEntity -> {
            ProductInfoReqVo productInfoReqVo = new ProductInfoReqVo();
            BeanUtils.copyProperties(productInfoEntity, productInfoReqVo);
            productInfoReqVo.setPeriod(this.getPeriod(productInfoReqVo.getIssue()));
            return productInfoReqVo;
        }).sorted(Comparator.comparing(ProductInfoReqVo::getIssue).reversed()).collect(Collectors.toList());
    }

    @Override
    public IndexReqVo getIndexInfo() {
        IndexReqVo reqVo = new IndexReqVo();
        //业务产品个数
        QueryWrapper<ProductInfoEntity> qw = new QueryWrapper<>();
        qw.select("distinct mark");
        int productCount = this.count(qw);
        reqVo.setBusinessProductNum(productCount);
        //服务单位个数
        reqVo.setServiceUnitNum(10);
        //注册用户个数
        int userCount = userService.count();
        reqVo.setRegisterUserNum(userCount);
        // 产品分类以及对应id
        List<IndexProductCategoryReqVo> categoryReqVos = dataCategoryService.getFirstCategoryInfo();
        reqVo.setCategoryInfo(categoryReqVos);
        //首页轮播图产品信息
        List<IndexProductReqVo> productReqVos = this.getIndexProductList();
        reqVo.setProductInfo(productReqVos);
        return reqVo;
    }

    /**
     * 首页产品相关信息
     * @return
     */
    private List<IndexProductReqVo> getIndexProductList() {
        // 95 为默认根节点ORG_ID(首页不登录可查看，默认查看最大组织机构产品，用户登录之后产看自己组织机构产品)
        Integer orgId = ProductConstant.PRODUCT_DEFAULT_ORG_ID;
        CurrentUser currentUser = UserUtil.getCurrentUser();
        if(currentUser != null){
            orgId = userService.getById(currentUser.getUserId()).getOrgId();
        }
        String regionId = orgService.getById(orgId).getOrgCode();
        //获取product_info_id集合,首页轮播图放两个最新的产品信息
        List<DataCategoryEntity> list = dataCategoryService.getIndexProductInfo();
        // 获取产品图片相关信息
        List<IndexProductReqVo> returnList = new ArrayList<>();
        for ( DataCategoryEntity reqVo:list) {
            //获取产品对应名称以及id
            IndexProductReqVo indexProductReqVo = dataCategoryService.getProductNameAndId(reqVo.getId());
            // 首頁不涉及用户权限
            List<ProductFileInfoEntity> entityList = productFileInfoDao.selectProductFileInfoByTreeId(indexProductReqVo.getProductId(), regionId);
            indexProductReqVo.setProductImgUrl(entityList.stream().map(item ->
                    productFileInfoService.getFileThumbUrl(item.getRelativePath(), item.getFilePath()))
                    .sorted(Comparator.comparing(String::toString).reversed()).collect(Collectors.toList()));
            returnList.add(indexProductReqVo);
        }

        Collections.synchronizedList(list).stream().parallel().forEach(reqVo -> {

        });
        return returnList;
    }


    /**
     * 插叙条件 加缓存，过期时间在数据库中设置。
     * @return
     */
    @Override
    @Cacheable(value = {ProductConstant.PRODUCT_SEARCH_INFO},key = "#root.method.name"+"+#userId")
    public List<ProductFuzzySearchReqVo> getFuzzySearchInfoList(Integer userId) {
        log.info("=============产品服务页：：模糊搜索缓存失效,查询数据库===============");
        return this.getFuzzySearchInfoListFromDb(userId);
    }

    /**
     * 从数据库中查询数据
     * @return
     */
    @SneakyThrows
    private List<ProductFuzzySearchReqVo> getFuzzySearchInfoListFromDb(Integer userId) {
        //根据用户id 获取到产品树信息
        List<String> treeIdList = dataCategoryService
                .selectTreeList(userId).stream().map(DataCategoryEntity::getId).collect(Collectors.toList());
        //缓存中无数据，查数据库并添加缓存
        List<ProductInfoEntity> entities = baseMapper.selectProductDistinctList(treeIdList);
        //根据 id 找到数据上一级的名称并返回
        List<String> idList = entities.stream().map(ProductInfoEntity::getTreeId).distinct().collect(Collectors.toList());
        Map<String,String> parentNameMap = dataCategoryService.getParentNameMapInfo(idList);
        return entities.stream().map(productInfoEntity -> {
            ProductFuzzySearchReqVo reqVo = new ProductFuzzySearchReqVo();
            BeanUtils.copyProperties(productInfoEntity, reqVo);
            reqVo.setValue(UUID.randomUUID().toString());
            reqVo.setDataSource(productInfoEntity.getSatellite());
            String cycleName = CycleTypeEnum.getValue(reqVo.getCycle());
            if(StringUtils.isNotBlank(reqVo.getCycle()) && StringUtils.isNotBlank(productInfoEntity.getName())
                     && StringUtils.isNotBlank(cycleName)
                    && StringUtils.isNotBlank(productInfoEntity.getSatellite())
                    && !Objects.isNull(parentNameMap.get(productInfoEntity.getTreeId()))){
                String label = parentNameMap.get(productInfoEntity.getTreeId()) + "_" + productInfoEntity.getName()
                        + "_" + productInfoEntity.getSatellite()+ "_" + cycleName;
                reqVo.setLabel(label);
            }
            return reqVo;
        }).filter(item -> !Objects.isNull(item.getLabel())).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getFuzzySearchProductList(ProductFuzzySearchVo searchVo) {
        ProductInfoVo productInfoVo = new ProductInfoVo();
        //封装查询参数
        if(StringUtils.isNotEmpty(searchVo.getCycle())){
            productInfoVo.setCycleList(Arrays.asList(searchVo.getCycle()));
        }
        if(StringUtils.isNotEmpty(searchVo.getDataSource())){
            productInfoVo.setDataSourceList(Arrays.asList(searchVo.getDataSource()));
        }
        productInfoVo.setId(searchVo.getProductId());
        BeanUtils.copyProperties(searchVo,productInfoVo);
        return this.getProductList(productInfoVo);
    }
}