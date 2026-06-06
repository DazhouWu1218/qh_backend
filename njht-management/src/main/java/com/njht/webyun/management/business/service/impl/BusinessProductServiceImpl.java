package com.njht.webyun.management.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.entity.Tree;
import com.njht.webyun.enums.CycleTypeEnum;
import com.njht.webyun.management.business.dao.BusinessProductDao;
import com.njht.webyun.management.business.dao.ProductCategoryDao;
import com.njht.webyun.management.business.entity.*;
import com.njht.webyun.management.business.service.BusinessProductService;
import com.njht.webyun.management.business.vo.BusinessParam;
import com.njht.webyun.management.common.util.CycleType;
import com.njht.webyun.management.common.util.WordUtil;
import com.njht.webyun.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author dgj
 */
@Service("businessProductService")
public class BusinessProductServiceImpl implements BusinessProductService {

    @Autowired
    private BusinessProductDao businessProductDao;

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Autowired
    private WordUtil asyncUtil;

    @Value("${imgPath}")
    private String imgPath;

    @Value("${downLoadPath}")
    private String downLoadPath;


    @Override
    public ReturnT<Object> getProjectInfos(ProductParam productParam, List<String> regions) {
        List<ProductInfoDto> productInfoList = getProductList(productParam, regions);
        //总条数
        PageInfo<ProductInfoDto> pageInfo1 = new PageInfo<>(productInfoList);
        long total = pageInfo1.getTotal();
        List<ProductInfoDto> productInfoList1 = new ArrayList<>(10);
        List<ProductFileInfo> list = new ArrayList<>();
        for(ProductInfoDto productInfoDto:productInfoList){
            //修改时间格式
            String productTime = DateFormatUtils.dateToStr(DateFormatUtils.strToDate(productInfoDto.getCreateTime(),
                    DateFormatUtils.formatYY_MM_dd_ss), DateFormatUtils.formatYY_MM_dd_HH_mm);
            productInfoDto.setCreateTime(productTime);
            String issue = productInfoDto.getIssue().substring(0,8)+"_"+productInfoDto.getIssue().substring(8);
            productInfoDto.setIssue(issue);
            //默认为0 发服务 需要制作快视图的为1
            productInfoDto.setGeoServerType(0);
            String cycleName = CycleType.cycleMap.get(productInfoDto.getCycle());
            if(cycleName != null){
                productInfoDto.setCycleName(cycleName);
            }
            //根据productInfoid 和 type 查找不同类型的产品
            List<ProductFileInfo> productFileInfos = productInfoDto.getProductFileInfos();
            ProductFileInfo p1 = new ProductFileInfo();
            Set<String> types1 = new HashSet<>();
            for(ProductFileInfo p:productFileInfos){
                String type = p.getFileType();
                if("png".equals(type)){
                    productInfoDto.setGeoServerType(1);
                    continue;
                }
                if ("jpg".equals(type) ) {
                    types1.add("JPG");
                }
                if ("xls".equals(type)|| "xml".equals(type) || "xlsx".equals(type)) {
                    types1.add("XLS");
                }
                if ("doc".equals(type)|| "docx".equals(type)) {
                    //将word文档转换成pdf
                    String inPath = p.getFilePath();
                    String outPath = inPath.substring(0,inPath.lastIndexOf("."))+".pdf";
                    File f = new File(outPath);
                    p1.setFilePath(outPath);
                    String str = p.getRelativePath();
                    String relativePath = str.substring(0,str.lastIndexOf("."))+".pdf";
                    p1.setRelativePath(relativePath);
                    p1.setFileType("pdf");
                    p1.setFileName(f.getName());
                    list.add(p);
                    types1.add("DOC");
                }
                if ("tif".equals(type)) {
                    types1.add("TIF");
                }
            }
            Collections.reverse(productFileInfos);
            if(p1.getRelativePath() != null){
                productFileInfos.add(p1);
            }
            productInfoDto.setProductFileInfos(productFileInfos);
            productInfoDto.setTypes(types1);
            productInfoList1.add(productInfoDto);
        }
        //存放word信息,另一个线程将所有的word转换成pdf
        List<String> sList = new ArrayList<>();
        for(ProductFileInfo p:list){
            sList.add(p.getFilePath());
        }
        asyncUtil.wordToPdf(sList);
        PageInfo<ProductInfoDto> pageInfos = new PageInfo<>(productInfoList1);
        pageInfos.setTotal(total);
        return ReturnT.success(pageInfos);
    }

    @Override
    public List<Tree> findProductTreeByUid(String userId) {
        List<HthtDataCategory> productCategoryList = productCategoryDao.selectCategoryByUserId(userId);
        List<String> strList = productCategoryDao.selectParentIds(userId);
        String s = JSON.toJSONString(strList);
        ArrayList<HthtDataCategory> collect = productCategoryList.stream().filter(item -> s.contains(item.getId())).collect(Collectors.toCollection(ArrayList::new));
        return this.changeCategoryToProductTree(collect);
    }

    @Override
    public List<Tree> findProductTree() {
        Integer userId = UserUtil.getCurrentUser().getUserId();
        List<HthtDataCategory> productCategoryList = productCategoryDao.selectCategoryByUserId(String.valueOf(userId));
        return this.changeCategoryToProductTree(productCategoryList);
    }

    @Override
    public ProductSelectForm getCycleAndDataSourceById(String id) {
        ProductSelectForm form = new ProductSelectForm();
        //数据类型
        List<ProductVo> productInfos = productCategoryDao.selectProductInfo(id);
        form.setProductList(productInfos);
        List<String> productIds = new ArrayList<>();
        productInfos.stream().forEach(item -> productIds.add(item.getValue()));
        if(productIds.isEmpty()){
            return null;
        }
        //周期信息
        List<String> cycles = businessProductDao.selectCycles(productIds).stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<CycleTypeInfo> cycleTypeInfoList = new ArrayList<>();
        cycles.stream().forEach(s -> {
            Integer anEnum = CycleTypeEnum.getSort(s);
            CycleTypeInfo cycleTypeInfo = new CycleTypeInfo(s,anEnum);
            cycleTypeInfoList.add(cycleTypeInfo);
        });
        List<ProductVo> cycleList = Collections.synchronizedList(new ArrayList<>());
        cycleTypeInfoList.stream().sorted(Comparator.comparing(CycleTypeInfo::getWeights)).forEach(s -> {
            String cycleName = CycleType.cycleMap.get(s.getValue());
            cycleList.add(new ProductVo(s.getValue(),cycleName));
        });
        form.setCycleList(cycleList);

        //数据源信息
        List<String> dataSources = businessProductDao.selectDataSources(productIds).stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<ProductVo> dataSourceList = Collections.synchronizedList(new ArrayList<>());
        dataSources.stream().forEach(s -> dataSourceList.add(new ProductVo(s,s)));
        form.setDataSourceList(dataSourceList);

        //单位信息
        UnitInfo unitInfo = businessProductDao.getUnitInfo(id);
        if(unitInfo == null){
            unitInfo = new UnitInfo();
        }
        form.setUnit(unitInfo);
        return form;
    }

    @Override
    public List<Object> getProductList(String userId) {
        List<HthtDataCategory> productCategoryList = productCategoryDao.selectCategoryInfo(userId);
        List<Tree> productCategoryTrees = changeCategoryToProductTree(productCategoryList);

        List<Tree> productTreeList = TreeBuilderUtil.buildTreeList(productCategoryTrees);
        List<Tree> childrenList = new ArrayList<>();
        List<Tree> productChildrenTree = getProductChildrenTree(productTreeList,childrenList);

        List<Object> list = new ArrayList<>();
        for(Tree p:productChildrenTree){
            List<ProductInfo> productInfo = businessProductDao.getProductInfoList(p.getValue());
            if(!productInfo.isEmpty()){
                List<Object> productList = getProductList(productInfo, p.getValue());
                list.addAll(productList);
            }
            else{
                Map<String,Object> map = new HashMap<>(16);
                String name = "0";
                map.put("feiLei",p.getValue());
                map.put("value",UUID.randomUUID().toString().replace("-",""));
                map.put("name",name);
                map.put("label",name);
                map.put("cycle","");
                map.put("dataSource","");
            }
        }
        for(int i=0;i<list.size();i++){
            Map map = (Map)list.get(i);
            map.put("id",i+1);
        }
        return list;

    }

    @Override
    public ReturnT<Object> getBusinessProductInfo(BusinessParam businessParam, List<String> regions) {

        PageUtil.setPageAndSize(businessParam.getPageNum(), businessParam.getPageSize(),1,10);
        List<ProductInfoDto> productList = businessProductDao.selectProductList(businessParam.getBeginTime(), businessParam.getEndTime(),null,businessParam.getCycle(),businessParam.getDataSource(),regions,businessParam.getMark());
        for(ProductInfoDto productInfoDto:productList){
            String cycleName = CycleType.cycleMap.get(productInfoDto.getCycle());
            if(cycleName != null){
                productInfoDto.setCycleName(cycleName);
            }
            String createTime = DateFormatUtils.issueFormat(productInfoDto.getCreateTime());
            productInfoDto.setCreateTime(createTime);
            //根据productInfoid 和 type 查找不同类型的产品
            String id = productInfoDto.getId();
            List<ProductFileInfoForgien> productFileInfos = businessProductDao.selectProductFileInfo(id);
            Set<String> types1 = new HashSet<>();
            for(ProductFileInfoForgien p:productFileInfos){
                String path = p.getFileUrl();
                path = imgPath+ File.separator+path;
                p.setFileUrl(path.replace("\\","/"));
                String type = p.getFileType();
                if("png".equals(type)){
                    continue;
                }
                if ("jpg".equals(type) ) {
                    types1.add("JPG");
                }
                if ("xls".equals(type)|| "xml".equals(type)) {
                    types1.add("XLS");
                }
                if ("doc".equals(type)|| "docx".equals(type)) {
                    types1.add("DOC");
                }
                if ("tif".equals(type)) {
                    types1.add("TIF");
                }
            }

            productInfoDto.setProductFileInfoList(productFileInfos);
            productInfoDto.setTypes(types1);
        }
        PageInfo<ProductInfoDto> pageInfoList = new PageInfo<>(productList);
        return ReturnT.success(pageInfoList);
    }

    @Override
    public ReturnT<Object> getPngInfo(String id) {
        PngInfoDto pngInfoDto =  businessProductDao.getPngInfo(id);
        String path = pngInfoDto.getFilePath();
        File file = new File(path);
        String regex1 = "(\\d{14}|\\d{12}).*.png$";
        List<File> files = FileSearchUtils.getFileList(file.getParentFile(), regex1);
        List<PngInfo> urlList = new ArrayList<>();
        for(File f:files){
            PngInfo png =  new PngInfo();
            String fileUrl = f.getPath().replace("\\", "/").replace(downLoadPath, "");
            String regex = "\\d{12}";
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(fileUrl);
            if(m.find()){
                String group = m.group(0);
                Date date = DateFormatUtils.strToDate(group,"yyyyMMddHHmm");
                String time = DateFormatUtils.dateToStr(date, "yyyy-MM-dd");
                png.setTime(time);
                png.setFileUrl(fileUrl);
                urlList.add(png);
            }
        }
        List<PngInfo> collect = urlList.stream().sorted(Comparator.comparing(PngInfo::getTime)).collect(Collectors.toList());
        Map<String,Object> map = new HashMap<>(2);
        map.put("name",pngInfoDto.getName());
        map.put("list",collect);
        return ReturnT.success(map);
    }

    @Override
    public ReturnT<Object> getParentListInfo(String id) {
        Integer userId = UserUtil.getCurrentUser().getUserId();
        List<HthtDataCategory> productCategoryList = productCategoryDao.selectCategoryByUserId(String.valueOf(userId));
        List<String> list = new ArrayList<>();
        this.getParentId(productCategoryList,id,list);
        ArrayList<String> collect = list.stream().filter(s -> !"0".equals(s)).collect(Collectors.toCollection(ArrayList::new));
        Map<Integer,String> map = new HashMap<>(3);
        for(int i=1;i<collect.size()+1;i++){
            map.put(i,list.get(collect.size()-i));
        }
        return ReturnT.success(map);
    }


    @Override
    @Scheduled(cron = "0 30/10 17,18,19,20 * * ?")
    public void word2Pdf() {
        //获取当天的word文档,并将没有转成pdf的转成pdf
        Date date = new Date();
        String s = DateFormatUtils.dateToStr(date, DateFormatUtils.formatYYYYMMdd);
        List<String> list = businessProductDao.getWordList(s);
        list.stream().forEach(s1 -> {
            String outPath = s1.substring(0,s1.lastIndexOf("."))+".pdf";
            if(!new File(outPath).exists()){
                asyncUtil.executeCmd(s1);
            }
        });
    }

    private List<String> getParentId(List<HthtDataCategory> productCategoryList, String id, List<String> list) {
        if(!"0".equals(id)){
            ArrayList<HthtDataCategory> collect = productCategoryList.stream().filter(item -> id.equals(item.getId())).collect(Collectors.toCollection(ArrayList::new));
            if(CollectionUtils.isNotEmpty(collect)){
                String parentId = collect.get(0).getParentId();
                list.add(parentId);
                getParentId(productCategoryList,parentId,list);
            }
        }
        return list;
    }


    /**
     * 将ProductCategory 转化为 ProductTree
     * @param hthtDataCategoryList
     * @return
     */
    private List<Tree> changeCategoryToProductTree(List<HthtDataCategory> hthtDataCategoryList){
        List<Tree> treeList = new ArrayList<>();
        for (HthtDataCategory s : hthtDataCategoryList) {
            Tree tree = new Tree();
            tree.setValue(s.getId());
            tree.setLabel(s.getName());
            tree.setParentId(s.getParentId());
            treeList.add(tree);
        }
        return treeList;
    }

    private List<ProductInfoDto> getProductList(ProductParam productParam,List<String> regions){
        List<String> fls = Optional.ofNullable(productParam.getFeiLei()).orElse(new ArrayList<>(0)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<String> name = Optional.ofNullable(productParam.getName()).orElse(new ArrayList<>(0)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<String> cycles = Optional.ofNullable(productParam.getCycle()).orElse(new ArrayList<>(0)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<String> dataSources = Optional.ofNullable(productParam.getDataSource()).orElse(new ArrayList<>(0)).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        String begin = productParam.getBeginTime();
        String end = productParam.getEndTime();

        Date dateBegin = DateFormatUtils.strToDate(begin, "yyyy-MM-dd HH:mm:ss");
        Date dateEnd = DateFormatUtils.strToDate(end, "yyyy-MM-dd HH:mm:ss");
        String beginStr = DateFormatUtils.dateToStr(dateBegin, "yyyyMMddHHmmss");
        String endStr = DateFormatUtils.dateToStr(dateEnd, "yyyyMMddHHmmss");

        //设置分页
        PageUtil.setPageAndSize(productParam.getPageNum(), productParam.getPageSize(),1,10);
        return businessProductDao.selectProductList(beginStr, endStr, fls, cycles, dataSources,regions,name);
    }
    /**
     * 递归寻找子节点
     * @param productTreeList
     * @param childrenList
     * @return
     */
    private List<Tree> getProductChildrenTree(List<Tree> productTreeList,List<Tree> childrenList){
        for (Tree p:productTreeList){
            if(p.getChildren().isEmpty()){
                childrenList.add(p);
                continue;
            }
            getProductChildrenTree((List<Tree>)p.getChildren(),childrenList);
        }
        return childrenList;
    }

    private List<Object> getProductList(List<ProductInfo> productInfos,String id){
        List<Object> list = new ArrayList<>();
        for(ProductInfo p:productInfos){
            Map<String,Object> map = new HashMap<>(16);
            String cycle = p.getCycle();
            String satellite = p.getSatellite();
            String name = p.getName();
            map.put("cycle",cycle);
            map.put("dataSource",satellite);
            map.put("feiLei",id);
            map.put("value",UUID.randomUUID().toString().replace("-",""));
            map.put("name",name+"_"+CycleType.cycleMap.get(cycle)+"_"+satellite);
            map.put("label",name+"_"+ CycleType.cycleMap.get(cycle)+"_"+satellite);
            list.add(map);
        }
        return list;
    }
}
