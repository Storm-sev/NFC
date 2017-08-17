package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.List;

public class Product extends Entity implements Serializable {

    private String id;
    private String name;
    private String serial;
    private String masterName;
    private String status;//1：商品图片 2：主图片 3：详情图片
    private long madeYear;
    private String logo;
    private String shoppingUrl;
    private String tenantProductSeriesId;
    private List<ProductPropertyValue> productPropertyValueList;
    private List<ProductImg> imgList;

    private String productSeriesId;
    private ProductSeries productSeries;

    private String tenantId;
    private OrganizationTenant tenant;

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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getMadeYear() {
        return madeYear;
    }

    public void setMadeYear(long madeYear) {
        this.madeYear = madeYear;
    }

    public List<ProductPropertyValue> getProductPropertyValueList() {
        return productPropertyValueList;
    }

    public void setProductPropertyValueList(List<ProductPropertyValue> productPropertyValueList) {
        this.productPropertyValueList = productPropertyValueList;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getShoppingUrl() {
        return shoppingUrl;
    }

    public void setShoppingUrl(String shoppingUrl) {
        this.shoppingUrl = shoppingUrl;
    }

    public List<ProductImg> getImgList() {
        return imgList;
    }

    public void setImgList(List<ProductImg> imgList) {
        this.imgList = imgList;
    }

    public String getProductSeriesId() {
        return productSeriesId;
    }

    public void setProductSeriesId(String productSeriesId) {
        this.productSeriesId = productSeriesId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantProductSeriesId() {
        return tenantProductSeriesId;
    }

    public void setTenantProductSeriesId(String tenantProductSeriesId) {
        this.tenantProductSeriesId = tenantProductSeriesId;
    }

    public ProductSeries getProductSeries() {
        return productSeries;
    }

    public void setProductSeries(ProductSeries productSeries) {
        this.productSeries = productSeries;
    }

    public OrganizationTenant getTenant() {
        return tenant;
    }

    public void setTenant(OrganizationTenant tenant) {
        this.tenant = tenant;
    }
}
