package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;

public class TenantCertificationImg extends Entity implements Serializable {

    private String id;
    private String tenantCertificationId;
    private String imgUrl;
    private int sort;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantCertificationId() {
        return tenantCertificationId;
    }

    public void setTenantCertificationId(String tenantCertificationId) {
        this.tenantCertificationId = tenantCertificationId;
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
