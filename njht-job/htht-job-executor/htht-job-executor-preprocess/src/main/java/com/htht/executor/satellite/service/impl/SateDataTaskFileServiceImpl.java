package com.htht.executor.satellite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.satellite.constant.SatelliteConstant;
import com.htht.executor.satellite.dao.SateDataTaskFileDao;
import com.htht.executor.satellite.entity.SateDataInfoEntity;
import com.htht.executor.satellite.entity.SateDataTaskFileEntity;
import com.htht.executor.satellite.service.SateDataFileInfoService;
import com.htht.executor.satellite.service.SateDataInfoService;
import com.htht.executor.satellite.service.SateDataTaskFileService;
import com.htht.executor.sys.service.DicService;
import com.htht.executor.task.util.PreJobUtil;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.context.XxlJobHelper;
import com.htht.job.core.entity.paramtemplate.PreDataParam;
import com.htht.job.core.util.FileNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 卫星数据在入库流程中,文件的记录表
 * @author daiguojun
 * @date 2022-05-19 11:40:19
 */
@Service("sateDataTaskFileService")
@Slf4j
public class SateDataTaskFileServiceImpl extends ServiceImpl<SateDataTaskFileDao, SateDataTaskFileEntity> implements SateDataTaskFileService {

    @Value("${satellite.windows.path}")
    private String winPath;

    @Value("${satellite.linux.path}")
    private String linuxPath;

    @Value("${satellite.linux.prePath}")
    private String linuxPrePath;
    @Value("${satellite.windows.prePath}")
    private String winPrePath;


    @Autowired
    private SateDataInfoService sateDataInfoService;

    @Autowired
    private SateDataFileInfoService sateDataFileInfoService;

    @Override
    public long selectCountByFileName(String fileName) {
        QueryWrapper<SateDataTaskFileEntity> qw = new QueryWrapper<>();
        qw.eq("file_name",fileName);
        return baseMapper.selectCount(qw);
    }

    @Override
    public List<String> selectFileNameBySatellite(PreDataParam preDataParam) {
        SateDataInfoEntity sateDataInfoEntity = PreJobUtil.setSatelliteInfo(preDataParam);
        return baseMapper.selectFileNameBySatellite(sateDataInfoEntity.getSatelliteId(),sateDataInfoEntity.getSensorId(),sateDataInfoEntity.getResolution());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePreDataInfo(PreDataParam preDataParam, LinkedHashMap<String, Object> dyMap, Map<String, String> returnMap, TriggerParam triggerParam,String satelliteUrl) {
        XxlJobHelper.log("开始入库");
        String targetFilePath = (String)dyMap.get("targetFilePath");
        File targetFile = new File(targetFilePath);

        // 原始数据入库
        SateDataTaskFileEntity sateDataTaskFileEntity = this.saveTaskInfo(targetFile,satelliteUrl);

        // 预处理信息入库
        SateDataInfoEntity sateDataInfoEntity = sateDataInfoService.saveSateDataInfo(targetFile,dyMap,returnMap,preDataParam,triggerParam);

        // 预处理文件信息入库
        sateDataFileInfoService.saveSateFileInfo(sateDataInfoEntity,sateDataTaskFileEntity,dyMap,returnMap,triggerParam,satelliteUrl);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveH8ProjectToDB(String originPath, String outputXml, String issue, TriggerParam triggerParam,String satelliteUrl) {
        XxlJobHelper.log("开始入库");
        // 原始数据路径
        File originFile = new File(originPath);


        // --------------原始数据入库------------------
        SateDataTaskFileEntity taskFileEntity = this.saveTaskInfo(originFile, satelliteUrl);
        log.info(">>>> htht_dms_sate_data_task_file insert success");
        // --------------预处理结果入库----------------
        SateDataInfoEntity sateDataInfoEntity = sateDataInfoService.saveH8SateDataInfo(originFile,outputXml,triggerParam,issue);
        log.info(">>>> htht_dms_sate_data_info insert success");

        // ---------------预处理文件信息入库-----------------
        sateDataFileInfoService.saveH8SateDataFileInfo(originFile,outputXml,triggerParam,taskFileEntity,sateDataInfoEntity,satelliteUrl);
        log.info(">>>> htht_dms_sate_data_file_info insert success");
    }


    /**
     * 原始数据入库
     * @param targetFile
     * @param satelliteUrl
     * @return
     */
    private SateDataTaskFileEntity saveTaskInfo(File targetFile, String satelliteUrl) {
        // 原始数据入库
        SateDataTaskFileEntity sateDataTaskFileEntity = new SateDataTaskFileEntity();
        sateDataTaskFileEntity.setTaskId(SatelliteConstant.SCAN_NEW_FILE_TASK);
        sateDataTaskFileEntity.setDescription(SatelliteConstant.DATA_TYPE);
        sateDataTaskFileEntity.setFileName(targetFile.getName());
        String realName = FileNameUtils.getFileRealName(targetFile);
        sateDataTaskFileEntity.setFileRealName(realName);
        if (targetFile.getName().contains("FY4A")) {
            sateDataTaskFileEntity.setFilePath(targetFile.getPath().replace(linuxPrePath,winPrePath));
            String relativePath = targetFile.getPath().replace("\\","/").replace(linuxPrePath.replace("\\","/"),"");
            sateDataTaskFileEntity.setRelativePath(relativePath);
        } else {
            sateDataTaskFileEntity.setFilePath(targetFile.getPath().replace(linuxPath,winPath));
            String relativePath = targetFile.getPath().replace("\\","/").replace(linuxPath.replace("\\","/"),"");
            sateDataTaskFileEntity.setRelativePath(relativePath);
        }
        boolean directory = targetFile.isDirectory();
        sateDataTaskFileEntity.setAssertDirectory(directory?1:0);
        sateDataTaskFileEntity.setFileSize(targetFile.length());
        this.save(sateDataTaskFileEntity);
        return sateDataTaskFileEntity;
    }

}