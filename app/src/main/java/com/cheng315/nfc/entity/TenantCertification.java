package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.List;

public class TenantCertification extends Entity implements Serializable {

    private String id;
    private String tenantId;
    private String name;
    private String org;
    private long theDate;
    private String level;
    private String imgUrl;
    private String status;
    private List<TenantCertificationImg> imgList;
    private List<TenantProductSeries> tenantProductSeriesList;

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

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public long getTheDate() {
        return theDate;
    }

    public void setTheDate(long theDate) {
        this.theDate = theDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TenantCertificationImg> getImgList() {
        return imgList;
    }

    public void setImgList(List<TenantCertificationImg> imgList) {
        this.imgList = imgList;
    }

    public List<TenantProductSeries> getTenantProductSeriesList() {
        return tenantProductSeriesList;
    }

    public void setTenantProductSeriesList(List<TenantProductSeries> tenantProductSeriesList) {
        this.tenantProductSeriesList = tenantProductSeriesList;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
