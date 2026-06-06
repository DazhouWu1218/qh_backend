package com.njht.webyun.publish.notice.vo;

import com.njht.webyun.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 代国军
 * @CreateDate: 2021/11/12 9:12
 * @Description: 通知公告返回类
 */
@Data
@ApiModel(value = "查询通知公告相关信息")
public class NoticeInfoSearchVo extends PageEntity {

    @ApiModelProperty(value = "用户id")
    private Integer userId;
}
