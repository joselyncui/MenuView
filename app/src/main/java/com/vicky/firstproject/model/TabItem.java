package com.vicky.firstproject.model;

/**
 * tabview 数据项
 *
 * Created by yao.cui on 2016/6/17.
 */
public class TabItem {

    private String title;//标题
    private String icon;//图标地址

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public String getIcon() {
        return icon;
    }
}
