package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/1/18.
 *
 */

public class AppVersion extends Entity implements Serializable {
    private String id;
    private String name;//工艺投资
    private String platform;//客户端设备 1安卓pad 2安卓手机 3ios手机 4iospad
    private String versionId; //大版本号
    private String subVersionId;//小版本号
    private String versionCode;//版本标识 1.2
    private String type;//是否升级  1升级，0不升级，2强制升级
    private String apkUrl;
    private String upgradePoint;//升级提示
    private String status;
    private Date createTime;
    private Date updateTime;

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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getSubVersionId() {
        return subVersionId;
    }

    public void setSubVersionId(String subVersionId) {
        this.subVersionId = subVersionId;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getUpgradePoint() {
        return upgradePoint;
    }

    public void setUpgradePoint(String upgradePoint) {
        this.upgradePoint = upgradePoint;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
