package com.finchina.treeviewlib;

public abstract class TreeViewDelegate<T> {

    public TreeViewAdapter<T> adapter;

    /**
     * 是否是当前类型
     *
     * @param treeNode 树节点
     * @return 当前类型
     */
    public abstract boolean isItemType(int position, TreeNode<T> treeNode);

    /**
     * 当前根视图布局id
     *
     * @return 布局id
     */
    public abstract int getLayoutId();

    /**
     * 视图适配
     *
     * @param holder   视图持有者
     * @param treeNode 树节点
     */
    public abstract void convert(TreeViewHolder holder, TreeNode<T> treeNode);
}
