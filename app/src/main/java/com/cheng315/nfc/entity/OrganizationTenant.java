package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.List;

public class OrganizationTenant extends Entity implements Serializable {

    private String id;
    private String name;
    private String provinceId;
    private String cityId;
    private String districtId;
    private String status;
    private List<TenantCertification> tenantCertificationList;
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

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TenantCertification> getTenantCertificationList() {
        return tenantCertificationList;
    }

    public void setTenantCertificationList(List<TenantCertification> tenantCertificationList) {
        this.tenantCertificationList = tenantCertificationList;
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
