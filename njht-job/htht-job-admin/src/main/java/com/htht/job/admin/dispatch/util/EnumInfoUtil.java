package com.htht.job.admin.dispatch.util;

import com.htht.job.admin.dispatch.core.route.ExecutorRouteStrategyEnum;
import com.htht.job.admin.dispatch.core.scheduler.MisfireStrategyEnum;
import com.htht.job.admin.dispatch.core.scheduler.ScheduleTypeEnum;
import com.htht.job.core.enums.ExecutorBlockStrategyEnum;
import com.htht.job.core.glue.GlueTypeEnum;
import com.njht.webyun.entity.CommonEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 代国军
 * @description: 枚举键值对信息
 * @date 2022/6/2 10:54
 */
@Component
public class EnumInfoUtil {


    /**
     * 路由策略键值对
     * @return
     */
    public static List<CommonEntity> executorRouteStrategyList(){
        List<CommonEntity> executorRouteStrategyList = new ArrayList<>();
        // 路由策略-列表
        for (ExecutorRouteStrategyEnum st : ExecutorRouteStrategyEnum.values()) {
            CommonEntity entity = new CommonEntity(st.name(),st.getTitle());
            executorRouteStrategyList.add(entity);
        }
        return executorRouteStrategyList;
    }

    /**
     * 运行模式键值对
     * @return
     */
    public static List<CommonEntity> glueTypeList() {
        List<CommonEntity> glueTypeList = new ArrayList<>();
        // 路由策略-列表
        for (GlueTypeEnum st : GlueTypeEnum.values()) {
            CommonEntity entity = new CommonEntity(st.name(),st.getDesc());
            glueTypeList.add(entity);
        }
        return glueTypeList;
    }

    /**
     * 阻塞策略键值对
     * @return
     */
    public static List<CommonEntity> executorBlockList() {
        List<CommonEntity> executorBlockList = new ArrayList<>();
        // 路由策略-列表
        for (ExecutorBlockStrategyEnum st : ExecutorBlockStrategyEnum.values()) {
            CommonEntity entity = new CommonEntity(st.name(),st.getTitle());
            executorBlockList.add(entity);
        }
        return executorBlockList;
    }

    /**
     * 阻塞策略键值对 ScheduleTypeEnum
     * @return
     */
    public static List<CommonEntity> scheduleTypeList() {
        List<CommonEntity> scheduleTypeList = new ArrayList<>();
        // 路由策略-列表
        for (ScheduleTypeEnum st : ScheduleTypeEnum.values()) {
            CommonEntity entity = new CommonEntity(st.name(),st.getTitle());
            scheduleTypeList.add(entity);
        }
        return scheduleTypeList;
    }

    /**
     * 阻塞策略键值对 MisfireStrategyEnum
     * @return
     */
    public static List<CommonEntity> misfireStrategyList() {
        List<CommonEntity> misfireStrategyList = new ArrayList<>();
        // 路由策略-列表
        for (MisfireStrategyEnum st : MisfireStrategyEnum.values()) {
            CommonEntity entity = new CommonEntity(st.name(),st.getTitle());
            misfireStrategyList.add(entity);
        }
        return misfireStrategyList;
    }


}
