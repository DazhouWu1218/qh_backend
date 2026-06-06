package com.htht.executor.satellite.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.satellite.dao.SateDataFileInfoDao;
import com.htht.executor.satellite.entity.SateDataFileInfoEntity;
import com.htht.executor.satellite.entity.SateDataInfoEntity;
import com.htht.executor.satellite.entity.SateDataTaskFileEntity;
import com.htht.executor.satellite.service.SateDataFileInfoService;
import com.htht.executor.sys.service.DicService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.xml.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author daiguojun
 * @date 2022-05-19 11:40:19
 */
@Service("sateDataFileInfoService")
@Slf4j
public class SateDataFileInfoServiceImpl extends ServiceImpl<SateDataFileInfoDao, SateDataFileInfoEntity> implements SateDataFileInfoService {


    @Value("${satellite.linux.prePath}")
    private String satellitePath;


    @Value("${satellite.windows.prePath}")
    private String windowsPath;

    @Override
    public void saveSateFileInfo(SateDataInfoEntity sateDataInfoEntity, SateDataTaskFileEntity sateDataTaskFileEntity,
                                 LinkedHashMap<String, Object> dyMap, Map<String, String> returnMap, TriggerParam triggerParam, String satelliteUrl) {
        SateDataFileInfoEntity sateDataFileInfoEntity = new SateDataFileInfoEntity();
        sateDataFileInfoEntity.setDataId(sateDataInfoEntity.getId());
        sateDataFileInfoEntity.setSourceFileId(sateDataTaskFileEntity.getId());
        String outPutXmlPath = (String)dyMap.get("outPutXmlPath");
        File outPutFile = new File(outPutXmlPath);
        //文件存放路径
        String path = outPutFile.getParentFile().getPath();
        //tif ldf
        String tifName = returnMap.get("OutputFilename");
        String tifPath = path + File.separator + tifName;
        File tifFile = new File(tifPath);
        sateDataFileInfoEntity.setTifFileName(tifName);
        sateDataFileInfoEntity.setTifFilePath(tifPath.replace(satellitePath,windowsPath));
        sateDataFileInfoEntity.setTifFileSize(tifFile.length());

        //缩略图
        String thumbnailName =returnMap.get("Thumbnail");
        String thumbnailPath = path +File.separator +thumbnailName;
        File thumbnailFile = new File(thumbnailPath);
        sateDataFileInfoEntity.setThumbnailFilePath(thumbnailPath.replace(satellitePath,windowsPath));
        sateDataFileInfoEntity.setThumbnailFileName(thumbnailFile.getName());
        sateDataFileInfoEntity.setThumbnailFileSize(thumbnailFile.length());
        String dictCode = (String)dyMap.get("satelliteUrlPath");
        if( dictCode == null ||"".equals(dictCode) ){
            dictCode = satelliteUrl;
        }
        dictCode = dictCode.replace("\\", "/");
        log.info("相对路径:"+dictCode);
        String fileUrl = thumbnailPath.replace("\\","/").replace(dictCode,"");
        sateDataFileInfoEntity.setThumbnailFileUrl(fileUrl);
        String tifUrl = tifPath.replace("\\","/").replace(dictCode,"");
        sateDataFileInfoEntity.setTifFileUrl(tifUrl);
        sateDataFileInfoEntity.setThumbChartFileName(thumbnailName);
        sateDataFileInfoEntity.setThumbChartFilePath(thumbnailPath.replace(satellitePath,windowsPath));
        String thumbnailChartFileUrl = thumbnailPath.replace("\\","/").replace(dictCode,"");
        sateDataFileInfoEntity.setThumbChartFileUrl(thumbnailChartFileUrl);
        sateDataFileInfoEntity.setThumbChartFileSize(new File(thumbnailPath).length());
        this.save(sateDataFileInfoEntity);
        log.info("sateFileInfo信息入库成功");
    }

    @Override
    public void saveH8SateDataFileInfo(File originFile, String outputXml, TriggerParam triggerParam,
                                       SateDataTaskFileEntity taskFileEntity, SateDataInfoEntity sateDataInfoEntity,
                                       String satelliteUrl) {
        //预处理结果以及对应的png
        List<String> mosaicFiles = XmlUtils.getXmlAttrFileElementVal(outputXml, "outFiles");
        List<String> tifPathList = mosaicFiles.stream().filter(s -> s.contains("tif") || s.contains("hdf")).collect(Collectors.toList());
        List<String> jpgPathList = mosaicFiles.stream().filter(s -> s.contains("jpg") || s.contains("png")).collect(Collectors.toList());
        final SateDataFileInfoEntity sateDataFileInfoEntity = new SateDataFileInfoEntity();
        if(!tifPathList.isEmpty()){
            final File tifFile = new File(tifPathList.get(0));
            final String unixStyleFilePath = tifFile.getAbsolutePath().replace("\\", "/");
            sateDataFileInfoEntity.setDataId(sateDataInfoEntity.getId());
            sateDataFileInfoEntity.setSourceFileId(taskFileEntity.getId());
            sateDataFileInfoEntity.setTifFileName(tifFile.getName());
            sateDataFileInfoEntity.setTifFilePath(unixStyleFilePath.replace(satellitePath,windowsPath));
            sateDataFileInfoEntity.setTifFileSize(tifFile.length());
            sateDataFileInfoEntity.setTifFileUrl(this.getRelativePath(satelliteUrl, unixStyleFilePath));
        }
        if (!jpgPathList.isEmpty()){
            // 缩略图,拇指图入库
            final File thumbnailFile = new File(jpgPathList.get(0));
            // 生成拇指图
            // 入库缩略图和拇指图
            sateDataFileInfoEntity.setDataId(sateDataInfoEntity.getId());
            sateDataFileInfoEntity.setSourceFileId(taskFileEntity.getId());
            // 缩略图
            sateDataFileInfoEntity.setThumbnailFileName(thumbnailFile.getName());
            String thumbnailFileABPath = thumbnailFile.getAbsolutePath().replace("\\", "/");
            sateDataFileInfoEntity.setThumbnailFilePath(thumbnailFileABPath.replace(satellitePath,windowsPath));
            sateDataFileInfoEntity.setThumbnailFileUrl(this.getRelativePath(satelliteUrl, thumbnailFileABPath));
            sateDataFileInfoEntity.setThumbnailFileSize(thumbnailFile.length());
            // 拇指图
            sateDataFileInfoEntity.setThumbChartFileName(thumbnailFile.getName());
            sateDataFileInfoEntity.setThumbChartFilePath(thumbnailFileABPath.replace(satellitePath,windowsPath));
            sateDataFileInfoEntity.setThumbChartFileUrl(this.getRelativePath(satelliteUrl, thumbnailFileABPath));
            sateDataFileInfoEntity.setThumbChartFileSize(thumbnailFile.length());
        }
        this.save(sateDataFileInfoEntity);
    }

    /**
     * 获取绝对路径
     * @param satelliteUrl
     * @param filePath
     * @return
     */
    private String getRelativePath(String satelliteUrl, String filePath) {
        String url = satelliteUrl.replace("\\", "/");
        return filePath.replace("\\","/").replace(url,"");
    }
}