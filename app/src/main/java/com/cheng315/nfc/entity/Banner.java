package com.cheng315.nfc.entity;

import com.efeiyi.Entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/11.
 */
public class Banner extends Entity implements Serializable {
    private String title;
    private String imgUrl;
    private String directUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }
}
