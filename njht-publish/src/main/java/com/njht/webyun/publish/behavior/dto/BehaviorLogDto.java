package com.njht.webyun.publish.behavior.dto;

import com.njht.webyun.publish.behavior.entity.BehaviorDownloadLogEntity;
import com.njht.webyun.publish.behavior.entity.BehaviorLogEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/12/6 16:51
 * @Description: 行为日志db返回结果
 */
@Data
public class BehaviorLogDto extends BehaviorLogEntity {

    private String userName;
    private String userOrg;
    private String tel;
    private String roleName;
    private List<BehaviorDownloadLogEntity> downloadLogEntityList;

}
