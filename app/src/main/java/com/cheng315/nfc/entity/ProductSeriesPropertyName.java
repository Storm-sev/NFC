package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;

public class ProductSeriesPropertyName extends Entity implements Serializable {

    private String id;
    private String productSeriesId;
    private String name;
    private String status;
    private ProductPropertyValue productPropertyValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductSeriesId() {
        return productSeriesId;
    }

    public void setProductSeriesId(String productSeriesId) {
        this.productSeriesId = productSeriesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductPropertyValue getProductPropertyValue() {
        return productPropertyValue;
    }

    public void setProductPropertyValue(ProductPropertyValue productPropertyValue) {
        this.productPropertyValue = productPropertyValue;
    }
}
