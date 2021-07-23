package com.finchina.treeview;

/**
 * @ProjectName: TreeView
 * @Description: 作用描述 <todo>
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/23 13:53
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/23 13:53
 * @UpdateRemark: 无
 */
public class TreeModel {

    private String name;
    private boolean enable;
    private boolean isAdded;

    public TreeModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }
}
