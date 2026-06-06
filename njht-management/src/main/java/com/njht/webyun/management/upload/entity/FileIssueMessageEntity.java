package com.njht.webyun.management.upload.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取文件期次的信息的实体类
 * @author zhushizhen
 */
@Data
public class FileIssueMessageEntity implements Serializable {
    /**
     * id
     */
    private String id;


    /** 数结构三级目录 */
    private String menuName;

    /**
     * 树结构id
     */
    private String treeId;



    /** 文件入库正则匹配 */
    private String issueRegularMatch;



    /** 文件的个数 */
    private Integer size;

    /** 标识*/
    private String treeKey;


    private String parsingFileType;

    private Integer  parsingFileNumber;

}
