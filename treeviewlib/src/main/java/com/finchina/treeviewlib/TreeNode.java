package com.finchina.treeviewlib;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点
 */
public class TreeNode<T> {

    // 数据
    private T data;
    // 父类
    private TreeNode<T> parent;
    // 子节点
    private List<TreeNode<T>> children;
    // 是否展开
    private boolean expand;
    // 是否选中
    private boolean select;

    private int type = 1;

    public TreeNode() {
    }

    public TreeNode(T data) {
        this.data = data;
    }

    public TreeNode(T data, int type) {
        this.data = data;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
        if (children != null) {
            for (TreeNode<T> child : children) {
                if (child != null) {
                    child.setParent(this);
                }
            }
        }
    }

    public void addChild(TreeNode<T> child) {
        if (child == null) {
            return;
        }
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
    }

    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children == null || children.size() == 0;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "value=" + data + "}";
    }
}
