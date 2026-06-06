package com.njht.webyun.management.upload.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhushizhen
 */
@Data
public class FileInformationEntity implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 树结构id
     */
    private String menuId;

    /** 数结构三级目录 */
    private String menuName;


    /** 临时存储目录 */
    private String temporaryDirectory;

    /** 最终归档目录 */
    private String archiveDirectory;

    /** 文件入库正则匹配 */
    private String regularMatch;

    /** 需要入库的表名 */
    private String insertTable;

    /** 尾缀的正则 */
    private String suffixMatch;

    /** 标识*/
    private String treeKey;
    /**
     * 是否为excel
     */
    private Integer isExcel;
    /**
     * 是excel,表头约束
     */
    private String headerField;
    /**
     * 是否需要解析
     */
    private Integer isParse;


}
