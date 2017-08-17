package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.List;

public class Label extends Entity implements Serializable {
    private String id;
    private long serial;
    private String code;
    private String labelBatchId;
    private String tenantId;
    private String status;
    private long firstCheckDateTime;
    private long lastCheckDateTime;
    private int checkCount;
    private String purchaseOrderLabelId;
    private List<LabelCheckRecord> labelCheckRecordList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSerial() {
        return serial;
    }

    public void setSerial(long serial) {
        this.serial = serial;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getFirstCheckDateTime() {
        return firstCheckDateTime;
    }

    public void setFirstCheckDateTime(long firstCheckDateTime) {
        this.firstCheckDateTime = firstCheckDateTime;
    }

    public long getLastCheckDateTime() {
        return lastCheckDateTime;
    }

    public void setLastCheckDateTime(long lastCheckDateTime) {
        this.lastCheckDateTime = lastCheckDateTime;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }

    public List<LabelCheckRecord> getLabelCheckRecordList() {
        return labelCheckRecordList;
    }

    public void setLabelCheckRecordList(List<LabelCheckRecord> labelCheckRecordList) {
        this.labelCheckRecordList = labelCheckRecordList;
    }

    public String getLabelBatchId() {
        return labelBatchId;
    }

    public void setLabelBatchId(String labelBatchId) {
        this.labelBatchId = labelBatchId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPurchaseOrderLabelId() {
        return purchaseOrderLabelId;
    }

    public void setPurchaseOrderLabelId(String purchaseOrderLabelId) {
        this.purchaseOrderLabelId = purchaseOrderLabelId;
    }
}
