package com.finchina.treeviewlib;

import java.util.List;

/**
 * @Description: TreeView 视图模型
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/20 13:20
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/20 13:20
 * @UpdateRemark: 无
 */
public class TreeNodeViewModel<T> {

    // 节点
    private TreeNode<T> node;
    // 是否展开
    private boolean expand;
    // 是否可用
    private boolean enable;
    // 是否已添加
    private boolean isAdded;

    public TreeNodeViewModel(TreeNode<T> node) {
        this.node = node;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public TreeNode<T> getNode() {
        return node;
    }

    public void setNode(TreeNode<T> node) {
        this.node = node;
    }

}
