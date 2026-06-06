package com.njht.webyun.management.upload.from;

import lombok.Data;

import java.util.List;

/**
 * @Author: 代国军
 * @CreateDate: 2021/6/16 9:32
 * @Description: 数据入库树形结构
 */
@Data
public class UploadTree{
    /** id*/
    private String id;
    /** 父id*/
    private String parentId;
    /** */
    private String name;

    private String parentName;
    private String identify;
    private List<UploadTree> children;


}
