package com.njht.webyun.management.business.entity;

import org.springframework.stereotype.Component;

/**
 * @author daiguojun
 */
@Component
public class ProductDTO {

    /** id */
    private String id;

    /** 名字 */
    private String name;

    /** 父id */
    private String parentId;

    public ProductDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
