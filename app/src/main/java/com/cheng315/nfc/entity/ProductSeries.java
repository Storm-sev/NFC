package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.List;

public class ProductSeries extends Entity implements Serializable {

    private String id;
    private String name;
    private String serial;
    private String status;
    private List<ProductSeriesPropertyName> productSeriesPropertyNameList;
    private List<TenantProductSeries> tenantProductSeriesList;
    private List<Product> productList;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductSeriesPropertyName> getProductSeriesPropertyNameList() {
        return productSeriesPropertyNameList;
    }

    public void setProductSeriesPropertyNameList(List<ProductSeriesPropertyName> productSeriesPropertyNameList) {
        this.productSeriesPropertyNameList = productSeriesPropertyNameList;
    }

    public List<TenantProductSeries> getTenantProductSeriesList() {
        return tenantProductSeriesList;
    }

    public void setTenantProductSeriesList(List<TenantProductSeries> tenantProductSeriesList) {
        this.tenantProductSeriesList = tenantProductSeriesList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
