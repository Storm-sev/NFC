package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.List;

public class TenantProductSeries extends Entity implements Serializable {

    private String id;
    private String tenantId;
    private String productSeriesId;
    private String status;
    private String craft;//手艺、手工制作
    private String region;//创作地区
    private List<TenantProductSeriesImg> imgList;//溯源图片
    private String tenantCertificationId;
    private List<Product> productList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCraft() {
        return craft;
    }

    public void setCraft(String craft) {
        this.craft = craft;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<TenantProductSeriesImg> getImgList() {
        return imgList;
    }

    public void setImgList(List<TenantProductSeriesImg> imgList) {
        this.imgList = imgList;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProductSeriesId() {
        return productSeriesId;
    }

    public void setProductSeriesId(String productSeriesId) {
        this.productSeriesId = productSeriesId;
    }

    public String getTenantCertificationId() {
        return tenantCertificationId;
    }

    public void setTenantCertificationId(String tenantCertificationId) {
        this.tenantCertificationId = tenantCertificationId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
