package com.njht.webyun.publish.product.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.njht.entity.category.DataCategoryEntity;
import com.njht.webyun.common.UserUtil;
import com.njht.webyun.constant.DbConstant;
import com.njht.webyun.publish.behavior.service.BehaviorLogService;
import com.njht.webyun.publish.product.constant.ProductConstant;
import com.njht.webyun.publish.product.dao.ProductFileInfoDao;
import com.njht.webyun.publish.product.entity.ProductFileInfoEntity;
import com.njht.webyun.publish.product.entity.ProductInfoEntity;
import com.njht.webyun.publish.product.service.DataCategoryService;
import com.njht.webyun.publish.product.service.ProductFileInfoService;
import com.njht.webyun.publish.product.service.ProductInfoService;
import com.njht.webyun.publish.product.util.WordUtil;
import com.njht.webyun.publish.product.vo.ImgInfoReqVo;
import com.njht.webyun.publish.product.vo.ProductFileBaseReqVo;
import com.njht.webyun.publish.product.vo.ProductInfoReqVo;
import com.njht.webyun.utils.FileNameUtils;
import com.njht.webyun.utils.FileZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author daiguojun
 */
@Service("productFileInfoService")
@Slf4j
@DS(value = DbConstant.MYSQL_1)
public class ProductFileInfoServiceImpl extends ServiceImpl<ProductFileInfoDao, ProductFileInfoEntity> implements ProductFileInfoService {

    @Value("${downLoad.path}")
    private String downLoadPath;

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private BehaviorLogService behaviorLogService;

    @Override
    public String getDownLoadPathByIdList(String ids,String types) {
        List<String> pathList = this.queryDownLoadPathFromDb(ids, types);
        if(!pathList.isEmpty()){
            if(pathList.size() == 1){
                return pathList.get(0);
            }else {
                return this.getZipFilePath(pathList);
            }
        }else{
            return null;
        }
    }

    /**
     * 从数据库过去文件路径
     * @param ids
     * @param types
     * @return
     */
    private List<String> queryDownLoadPathFromDb(String ids,String types){
        List<ProductFileInfoEntity> fileInfoEntityList = this.getProductFileInfos(ids, types);
        Integer userId = UserUtil.getCurrentUser().getUserId();
        if(!CollectionUtils.isEmpty(fileInfoEntityList)){
            //保存用户下载记录到日志表 (异步，开启另一个线程调用)
            this.saveProductDownLoadInfo(fileInfoEntityList,userId);
            //返回文件路径
            return fileInfoEntityList.stream().map(ProductFileInfoEntity::getFilePath)
                    .sorted(Comparator.comparing(String::toString)).collect(Collectors.toList());
        }else {
            return new ArrayList<>();
        }
    }

    @Autowired
    private DataCategoryService dataCategoryService;

    /**
     * 保存用户下载记录信息
     * @param fileInfoEntityList
     * @param userId
     */
    public void saveProductDownLoadInfo(List<ProductFileInfoEntity> fileInfoEntityList,Integer userId) {
        //根据id 获取产品的基本信息
        List<String> idList = fileInfoEntityList.stream().map(ProductFileInfoEntity::getProductInfoId).collect(Collectors.toList());
        List<ProductInfoEntity> productInfoEntities = productInfoService.entityList(idList);
        List<DataCategoryEntity> categoryEntities = dataCategoryService.selectTreeList(userId);
        Runnable runnable = () -> {
            log.info("当前线程：{}",Thread.currentThread().getName());
            behaviorLogService.saveProductDownLoadInfo(fileInfoEntityList,userId,productInfoEntities,categoryEntities);
        };
        new Thread(runnable).start();
    }

    /**
     * 压缩文件并返回压缩包绝对路径
     * @param pathList  文件路径集合
     * @return
     */
    private String getZipFilePath(List<String> pathList) {
        log.info("===================业务产品相关,开始压缩文件================");
        List<File> fileList = pathList.stream().map(File::new).collect(Collectors.toList());
        String targetName = FileNameUtils.getFileRealName(fileList.get(0))+System.currentTimeMillis();
        File file = FileZipUtils.zipFiles(fileList, downLoadPath, targetName);
        if(!file.exists() || file.length() <= ProductConstant.PRODUCT_FILE_LENGTH ){
            //文件不存在或者文件大小小于0 返回null
            return null;
        }
        return file.getPath();
    }

    @Override
    public List<ProductInfoReqVo> getFileNumAndTypeList(List<ProductInfoReqVo> collect) {
        //过滤出id集合，根据id集合查询文件个数以及类型。
        List<String> idList = collect.stream().map(ProductInfoReqVo::getId).collect(Collectors.toList());
        //文件类型集合
        if(CollectionUtils.isEmpty(idList)){
            return collect;
        }
        List<ProductFileInfoEntity> fileInfoEntities = baseMapper.selectFileTypeList(idList);
        //根据 product_info_id 分组 value长度是文件个数,value中的file_type 是文件类型
        Map<String, List<ProductFileInfoEntity>> fileInfoMap =
                fileInfoEntities.stream().collect(Collectors.groupingBy(ProductFileInfoEntity::getProductInfoId));
        for (Map.Entry<String, List<ProductFileInfoEntity>> entry : fileInfoMap.entrySet()) {
            String key = entry.getKey();
            List<ProductFileInfoEntity> value = entry.getValue();
            //过滤出key 对应的数据 (只有一条)
            List<ProductInfoReqVo> infoReqVos =
                    collect.stream().filter(productInfoReqVo -> productInfoReqVo.getId().equals(key)).collect(Collectors.toList());
            //value中获取文件类型以及个数
            infoReqVos.get(0).setFileNum(value.size());
            List<String> fileTypeList =
                    value.stream().map(ProductFileInfoEntity::getFileType).distinct().collect(Collectors.toList());
            infoReqVos.get(0).setFileTypeList(fileTypeList);
            //返回一张图片信息
            List<ProductFileInfoEntity> jpgList =
                    value.stream().filter(productFileInfoEntity -> productFileInfoEntity.getFileType().equals(ProductConstant.PRODUCT_FILE_INFO_JPG)
                            || productFileInfoEntity.getFileType().equals(ProductConstant.PRODUCT_FILE_INFO_PNG))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(jpgList)) {
                ProductFileInfoEntity fileInfo = jpgList.get(0);
                String relativePath = fileInfo.getRelativePath();
                String fileThumbUrl = this.getFileThumbUrl(relativePath, fileInfo.getFilePath());
                infoReqVos.get(0).setFileUrl(relativePath);
                infoReqVos.get(0).setFileThumbUrl(fileThumbUrl);
            } else {
                infoReqVos.get(0).setFileUrl("");
                infoReqVos.get(0).setFileThumbUrl("");
            }

        }
        return collect;
    }

    /**
     * 获取缩略图相关信息
     * @param relativePath
     * @param filePath
     * @return
     */
    @Override
    public String getFileThumbUrl(String relativePath, String filePath) {
        final String fileSuffix =
                FileNameUtils.formatURIasLinuxStyle(filePath).replace(FileNameUtils.formatURIasLinuxStyle(relativePath),"");
        // 压缩图片
        log.info("图片路径：[{}]",filePath);
//        File reSizeFile = ImgResizeUtils.getReSizeFile(filePath);
        return filePath.replace(fileSuffix,"");
    }

    @Value("${algorithm.exePath}")
    private String exePath;

    @Override
    public List<ProductFileBaseReqVo> getFileInfoList(String id, String mark, List<String> fileTypeList) {
        //根据product_info_id 查询
        QueryWrapper<ProductFileInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ProductConstant.DB_FIELD_PRODUCT_INFO_ID,id);
        if (!CollectionUtils.isEmpty(fileTypeList) && !ProductConstant.PRODUCT_FILE_INFO_TYPE.equals(fileTypeList.get(0))) {
            queryWrapper.in(ProductConstant.DB_FIELD_FILE_TYPE,fileTypeList);
        }else{
            queryWrapper.ne(ProductConstant.DB_FIELD_FILE_TYPE,ProductConstant.PRODUCT_FILE_INFO_PNG);
        }
        //结果list
        List<ProductFileInfoEntity> list = this.list(queryWrapper);
        //设置返回值
        return list.stream().map(item -> {
            ProductFileBaseReqVo productFileBaseReqVo = new ProductFileBaseReqVo();
            BeanUtils.copyProperties(item, productFileBaseReqVo);
            //word 修改文件返回路径 tif修改返回内容
            if(item.getFileType().equals(ProductConstant.PRODUCT_FILE_INFO_DOC) || item.getFileType().equals(ProductConstant.PRODUCT_FILE_INFO_DOCX)){
                String path = item.getRelativePath();
                WordUtil.wordToPdf(item.getFilePath(),exePath);
                String replace = path.replace(".docx", ".pdf");
                String s = replace.replace(".doc", ".pdf");
                productFileBaseReqVo.setFileUrl(s);
            }else {
                productFileBaseReqVo.setMark(mark);
                productFileBaseReqVo.setFileUrl(item.getRelativePath());
            }
            return productFileBaseReqVo;
        }).sorted(Comparator.comparing(ProductFileBaseReqVo::getFileName))
          .collect(Collectors.toList());
    }

    @Override
    public List<ImgInfoReqVo> getTifAndJpgByIds(List<String> ids) {
        List<ImgInfoReqVo> returnList = new ArrayList<>();
        //专题图片路径从file表中取
        QueryWrapper<ProductFileInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(ProductConstant.DB_FIELD_PRODUCT_INFO_ID,ids);
        queryWrapper.and(qw -> 
            qw.eq(ProductConstant.DB_FIELD_FILE_TYPE,ProductConstant.PRODUCT_FILE_INFO_JPG).or().eq(ProductConstant.DB_FIELD_FILE_TYPE,ProductConstant.PRODUCT_FILE_INFO_PNG)
        );

        //专题图片集合
        List<ProductFileInfoEntity> list = this.list(queryWrapper);
        List<ProductInfoReqVo> jpgList = list.stream()
                .map(productFileInfoEntity -> {
                    ProductInfoReqVo productInfoReqVo = new ProductInfoReqVo();
                    productInfoReqVo.setId(productFileInfoEntity.getId());
                    productInfoReqVo.setFileUrl(productFileInfoEntity.getRelativePath());
                    return productInfoReqVo;
                }).sorted(Comparator.comparing(ProductInfoReqVo::getFileUrl).reversed())
                .collect(Collectors.toList());
        ImgInfoReqVo jpgInfo = new ImgInfoReqVo(ProductConstant.PRODUCT_FILE_INFO_JPG,jpgList);
        returnList.add(jpgInfo);

        //栅格集合从productInfo表中取
        List<ProductInfoReqVo> tifInfoList = productInfoService.getTifInfoList(ids);
        ImgInfoReqVo tifInfo = new ImgInfoReqVo(ProductConstant.PRODUCT_FILE_INFO_TIF,tifInfoList);
        returnList.add(tifInfo);

        //issue
        List<String> issueList = tifInfoList.stream().
                map(ProductInfoReqVo::getPeriod).sorted(Comparator.comparing(String::toString).reversed()).collect(Collectors.toList());
        ImgInfoReqVo issueInfo = new ImgInfoReqVo("issue",issueList);
        returnList.add(issueInfo);

        return returnList;
    }

    @Override
    public List<ProductFileInfoEntity> getProductFileInfos(String ids, String types) {
        //根据id查询文件路径
        List<String> idList = Arrays.asList(ids.split(","));
        List<ProductFileInfoEntity>  fileInfoEntityList = this.listByIds(idList);
        if(CollectionUtils.isEmpty(fileInfoEntityList)){
            //为空说明id不是product_id 再根据product_info_id 查询
            QueryWrapper<ProductFileInfoEntity> qw = new QueryWrapper<>();
            //对文件类型做校验

            if (StringUtils.isNotEmpty(types) && !types.contains(ProductConstant.PRODUCT_FILE_INFO_TYPE)){
                //type 不为空 且  type 不包含all
                List<String> typeList = Arrays.asList(types);
                qw.in(ProductConstant.DB_FIELD_FILE_TYPE,typeList);
            }
            qw.in(ProductConstant.DB_FIELD_PRODUCT_INFO_ID,idList);
            fileInfoEntityList = this.list(qw);
            if(CollectionUtils.isEmpty(fileInfoEntityList)){
                return new ArrayList<>();
            }
        }
        return fileInfoEntityList;
    }

    @Override
    public String getDownLoadGifPathByIdList(String ids) {
        List<String> pathList = this.queryDownLoadPathFromDb(ids, ProductConstant.PRODUCT_FILE_INFO_JPG);
        String path = null;
        if(CollectionUtils.isNotEmpty(pathList)){
            //查询结果不为空
            path = this.jpgToGif(pathList);

        }
        return path;
    }

    @Override
    public Map<String, List<ProductFileBaseReqVo>> getFileInfoListByIds(List<ProductInfoEntity> records) {
        // 获取id
        List<String> idList = Optional.ofNullable(records).orElse(new ArrayList<>()).stream()
                .map(ProductInfoEntity::getId).collect(Collectors.toList());
        // 根据id集合处理文件信息
        List<ProductFileInfoEntity> fileInfoEntities = baseMapper.selectFileTypeList(idList);
        //根据 product_info_id 分组 value长度是文件个数,value中的file_type 是文件类型
        return  fileInfoEntities.stream()
                        .map(item -> {
                            ProductFileBaseReqVo reqVo = new ProductFileBaseReqVo();
                            BeanUtils.copyProperties(item,reqVo);
                            if(item.getFileType().equals(ProductConstant.PRODUCT_FILE_INFO_DOC) || item.getFileType().equals(ProductConstant.PRODUCT_FILE_INFO_DOCX)){
                                String path = item.getRelativePath();
                                WordUtil.wordToPdf(item.getFilePath(),exePath);
                                String replace = path.replace(".docx", ".pdf");
                                String s = replace.replace(".doc", ".pdf");
                                reqVo.setFileUrl(s);
                            }else {
                                reqVo.setFileUrl(item.getRelativePath());
                            }
                            return reqVo;
                        })
                        .sorted(Comparator.comparing(ProductFileBaseReqVo::getFileName))
                        .collect(Collectors.groupingBy(ProductFileBaseReqVo::getProductInfoId));
    }

    /**
     *
     * @param pathList
     */
    private synchronized String jpgToGif(List<String> pathList) {
        // gif路径
        String fileRealName = FileNameUtils.getFileRealName(new File(pathList.get(0)));
        String gifPath = downLoadPath+File.separator+fileRealName+System.currentTimeMillis()+".gif";
        File file = new File(gifPath);
        // 判断 父目录是否存在数据，不存在创建
        if(file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
        try{
            animatedGifEncoder.setRepeat(0);
            animatedGifEncoder.start(gifPath);
            BufferedImage[] src = new BufferedImage[pathList.size()];
            for (int i = 0; i < src.length; i++) {
                //设置播放的延迟时间
                animatedGifEncoder.setDelay(1000);
                // 读入需要播放的jpg文件
                src[i] = ImageIO.read(new File(pathList.get(i)));
                //添加到帧中
                animatedGifEncoder.addFrame(src[i]);
            }
        } catch (Exception e) {
            log.info( "jpgToGif Failed:");
            e.printStackTrace();
        } finally {
            animatedGifEncoder.finish();
        }
        return gifPath;
    }
}