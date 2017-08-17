package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.List;

public class PurchaseOrder extends Entity implements Serializable {

    private String id;
    private String serial;
    private String tenantId;
    private String userId;
    private long createDatetime;
    private String status;
    private List<PurchaseOrderLabel> purchaseOrderLabelList;
    private List<PurchaseOrderPayment> purchaseOrderPaymentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(long createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PurchaseOrderLabel> getPurchaseOrderLabelList() {
        return purchaseOrderLabelList;
    }

    public void setPurchaseOrderLabelList(List<PurchaseOrderLabel> purchaseOrderLabelList) {
        this.purchaseOrderLabelList = purchaseOrderLabelList;
    }

    public List<PurchaseOrderPayment> getPurchaseOrderPaymentList() {
        return purchaseOrderPaymentList;
    }

    public void setPurchaseOrderPaymentList(List<PurchaseOrderPayment> purchaseOrderPaymentList) {
        this.purchaseOrderPaymentList = purchaseOrderPaymentList;
    }
}
