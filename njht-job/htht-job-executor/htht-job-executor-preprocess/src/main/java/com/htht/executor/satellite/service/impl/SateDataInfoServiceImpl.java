package com.htht.executor.satellite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htht.executor.satellite.dao.SateDataInfoDao;
import com.htht.executor.satellite.entity.SateDataInfoEntity;
import com.htht.executor.satellite.service.SateDataInfoService;
import com.htht.job.core.biz.model.TriggerParam;
import com.htht.job.core.constant.DateConstant;
import com.htht.job.core.entity.paramtemplate.PreDataParam;
import com.htht.job.core.util.DateUtil;
import com.htht.job.core.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * 
 * @author daiguojun
 * @date 2022-05-19 11:40:19
 */
@Service("sateDataInfoService")
@Slf4j
public class SateDataInfoServiceImpl extends ServiceImpl<SateDataInfoDao, SateDataInfoEntity> implements SateDataInfoService {

    @Override
    public SateDataInfoEntity saveSateDataInfo(File targetFile, LinkedHashMap<String, Object> dyMap, Map<String, String> returnMap, PreDataParam preDataParam, TriggerParam triggerParam) {
        SateDataInfoEntity sateDataInfoEntity = new SateDataInfoEntity();
        String satellite = (String)dyMap.get("satellite");
        String sensor = (String)dyMap.get("sensor");
        String resolution = (String)dyMap.get("resolution");
        String level = returnMap.get("Level");
        sateDataInfoEntity.setSatelliteId(satellite);
        sateDataInfoEntity.setSensorId(sensor);
        sateDataInfoEntity.setResolution(Double.valueOf(resolution));
        sateDataInfoEntity.setLevel(level);
        // 期次
        String issue = FileUtil.getFileTime(preDataParam.getFileDatePattern(), targetFile.getName());
        if (issue != null) {
            Date date = DateUtil.strToDate(issue,  DateConstant.YYYYMMDDHHMMSS);
            //世界时转北京时间 +8
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if("FY4A".equals(satellite)){
                calendar.add(Calendar.HOUR_OF_DAY, 8);
            }
            sateDataInfoEntity.setProductId(DateFormatUtils.format(calendar.getTime(), DateConstant.HHmmss));
            sateDataInfoEntity.setDate(DateFormatUtils.format(calendar.getTime(), DateConstant.YYYY_MM_DD));
            sateDataInfoEntity.setTime(DateFormatUtils.format(calendar.getTime(), DateConstant.HH_MM_SS));
        }
        // 保存数据四角坐标信息
        this.saveLatLonInfo(sateDataInfoEntity,triggerParam,returnMap);
        this.save(sateDataInfoEntity);
        return sateDataInfoEntity;
    }

    @Override
    public SateDataInfoEntity saveH8SateDataInfo(File originFile, String outputXml, TriggerParam triggerParam,String issue) {
        SateDataInfoEntity sateDataInfoEntity = new SateDataInfoEntity();
        sateDataInfoEntity.setSatelliteId("H8");
        sateDataInfoEntity.setSensorId("AHI");
        sateDataInfoEntity.setResolution(1000.0);
        sateDataInfoEntity.setCloud(0.0);

        final Date date = DateUtil.strToDate(issue, DateConstant.YYYYMMDDHHMM);
        sateDataInfoEntity.setProductId(DateFormatUtils.format(date, DateConstant.HHmmss));
        sateDataInfoEntity.setDate(DateFormatUtils.format(date, DateConstant.YYYY_MM_DD));
        sateDataInfoEntity.setTime(DateFormatUtils.format(date, DateConstant.HH_MM_SS));
        sateDataInfoEntity.setDataType(0);
        sateDataInfoEntity.setLevel("L1");
        this.saveLatLonInfo(sateDataInfoEntity,triggerParam,new HashMap<>());
        this.save(sateDataInfoEntity);
        return sateDataInfoEntity;
    }

    @Override
    public Integer selectCountSate(String satelliteId, String sensorId, String day, String hour) {
        LambdaQueryWrapper<SateDataInfoEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(SateDataInfoEntity::getSatelliteId,satelliteId)
                .eq(SateDataInfoEntity::getSensorId,sensorId)
                .eq(SateDataInfoEntity::getDate,day)
                .eq(SateDataInfoEntity::getTime,hour);
        return baseMapper.selectCount(qw);
    }

    /**
     * 保存四角坐标信息
     * @param sateDataInfoEntity
     * @param triggerParam
     * @param returnMap
     */
    private void saveLatLonInfo(SateDataInfoEntity sateDataInfoEntity, TriggerParam triggerParam, Map<String, String> returnMap) {

        String coordinates = (String)triggerParam.getTaskParam().getDynamicMap().getOrDefault("latLon", "0,0,0,0");
        String[] split = coordinates.split(",");
        final String xMin = Objects.equals(split[0],"0") ? returnMap.getOrDefault("minx","0"):split[0];
        final String xMax = Objects.equals(split[1],"0") ? returnMap.getOrDefault("maxx","0"):split[1];
        final String yMin = Objects.equals(split[2],"0") ? returnMap.getOrDefault("miny","0"):split[2];
        final String yMax = Objects.equals(split[3],"0") ? returnMap.getOrDefault("maxy","0"):split[3];
        //左上角
        sateDataInfoEntity.setTopleftlatitude(Double.valueOf(yMax));
        sateDataInfoEntity.setTopleftlongitude(Double.valueOf(xMin));
        //右上角
        sateDataInfoEntity.setToprightlatitude(Double.valueOf(yMax));
        sateDataInfoEntity.setToprightlongitude(Double.valueOf(xMax));
        //左下角
        sateDataInfoEntity.setBottomleftlatitude(Double.valueOf(yMin));
        sateDataInfoEntity.setBottomleftlongitude(Double.valueOf(xMin));
        //右下角
        sateDataInfoEntity.setBottomrightlatitude(Double.valueOf(yMin));
        sateDataInfoEntity.setBottomrightlongitude(Double.valueOf(xMax));

        //左上角
        sateDataInfoEntity.setPngtopleftlatitude(Double.valueOf(yMax));
        sateDataInfoEntity.setPngtopleftlongitude(Double.valueOf(xMin));
        //右上角
        sateDataInfoEntity.setPngtoprightlatitude(Double.valueOf(yMax));
        sateDataInfoEntity.setPngtoprightlongitude(Double.valueOf(xMax));
        //左下角
        sateDataInfoEntity.setPngbottomleftlatitude(Double.valueOf(yMin));
        sateDataInfoEntity.setPngbottomleftlongitude(Double.valueOf(xMin));
        //右下角
        sateDataInfoEntity.setPngbottomrightlatitude(Double.valueOf(yMin));
        sateDataInfoEntity.setPngbottomrightlongitude(Double.valueOf(xMax));
        //ploygon
        String geom = Double.valueOf(xMin) + " " +
                Double.valueOf(yMax) + "," +
                Double.valueOf(xMax) + " " +
                Double.valueOf(yMax) + "," +
                Double.valueOf(xMax) + " " +
                Double.valueOf(yMin) + "," +
                Double.valueOf(xMin) + " " +
                Double.valueOf(yMin) + "," +
                Double.valueOf(xMin) + " " +
                Double.valueOf(yMax);
        final String polygon = "POLYGON((" + geom + "))";
        sateDataInfoEntity.setTheGeom(polygon);
        sateDataInfoEntity.setDataType(0);
        sateDataInfoEntity.setCloud(0.0);
    }
}