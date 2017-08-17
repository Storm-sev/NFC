package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;

public class ProductPropertyValue extends Entity implements Serializable {

    private String id;
    private String productSeriesPropertyNameId;
    private String value;
    private String status;
    private String productId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductSeriesPropertyNameId() {
        return productSeriesPropertyNameId;
    }

    public void setProductSeriesPropertyNameId(String productSeriesPropertyNameId) {
        this.productSeriesPropertyNameId = productSeriesPropertyNameId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
