package com.njht.webyun.management.business.entity;

/**
 * @author ：daiguojun
 * @date ：Created in 2021/3/8 11:05
 * @description：对外的接口实体类
 */
public class ProductFileInfoForgien {

    private static final long serialVersionUID = -710849983557978270L;

    /** 主键id */
    private String id;

    /** 文件类型 */
    private String fileType;

    /** 文件路径*/
    private String fileUrl;

    /** 文件名 */
    private String fileName;


    public ProductFileInfoForgien() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
