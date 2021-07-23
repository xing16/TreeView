package com.finchina.treeviewlib;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @ProjectName: TreeView
 * @Description: TreeView 单一布局 adapter
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/20 13:25
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/20 13:25
 * @UpdateRemark: 无
 */
public abstract class SimpleTreeViewAdapter<T> extends TreeViewAdapter<T> {

    /**
     * 构造器
     *
     * @param context  上下文
     * @param allNodes 树节点根列表（所有数据）
     */
    public SimpleTreeViewAdapter(Context context, int layoutId, @NonNull List<TreeNode<T>> allNodes) {
        super(context, allNodes);
        final int layoutResId = layoutId;
        addItemViewDelegate(new TreeViewDelegate<T>() {
            @Override
            public boolean isItemType(int position, TreeNode<T> treeNode) {
                return true;
            }

            @Override
            public int getLayoutId() {
                return layoutResId;
            }

            @Override
            public void convert(TreeViewHolder holder, TreeNode<T> treeNode) {
                SimpleTreeViewAdapter.this.convert(adapter, holder, treeNode);
            }
        });
    }

    public abstract void convert(TreeViewAdapter<T> adapter, TreeViewHolder holder, TreeNode<T> treeNode);
}

