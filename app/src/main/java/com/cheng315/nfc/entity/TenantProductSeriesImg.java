package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;

public class TenantProductSeriesImg extends Entity implements Serializable {

    private String id;
    private String tenantProductSeriesId;
    private String imgUrl;
    private int sort;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantProductSeriesId() {
        return tenantProductSeriesId;
    }

    public void setTenantProductSeriesId(String tenantProductSeriesId) {
        this.tenantProductSeriesId = tenantProductSeriesId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
