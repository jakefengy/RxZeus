package com.xm.zeus.view.home.entity;

import android.text.TextUtils;

/**
 * 检查更新结果
 */
public class CheckVersionResult {

    private String newversion;

    private String fileid;

    private String updateinfo;

    private String necessity;

    public String getNewversion() {
        return newversion;
    }

    public void setNewversion(String newversion) {
        this.newversion = newversion;
    }

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getUpdateinfo() {
        return updateinfo;
    }

    public void setUpdateinfo(String updateinfo) {
        this.updateinfo = updateinfo;
    }

    public String getNecessity() {
        return necessity;
    }

    public void setNecessity(String necessity) {
        this.necessity = necessity;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(newversion) && TextUtils.isEmpty(fileid) && TextUtils.isEmpty(updateinfo)
                && TextUtils.isEmpty(necessity);
    }

}
