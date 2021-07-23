package com.finchina.treeview;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.finchina.treeviewlib.TreeNode;
import com.finchina.treeviewlib.TreeViewAdapter;
import com.finchina.treeviewlib.TreeViewHolder;

import java.util.List;

/**
 * @ProjectName: TreeView
 * @Description: 作用描述 <todo>
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/22 9:43
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/22 9:43
 * @UpdateRemark: 无
 */
public class TreeViewAdapterWrapper<T> extends TreeViewAdapter<T> {

    public static final int ITEM_TYPE_HEADER = 1000;
    public static final int ITEM_TYPE_FOOTER = 2000;
    private SparseArray<View> headerViews = new SparseArray<>();
    private SparseArray<View> footerViews;

    private TreeViewAdapter<T> adapter;

    /**
     * 构造器
     *
     * @param context  上下文
     * @param allNodes 树节点根列表（所有数据）
     */
    public TreeViewAdapterWrapper(Context context, @NonNull List<TreeNode<T>> allNodes, TreeViewAdapter<T> adapter) {
        super(context, allNodes);
        this.adapter = adapter;
    }

    private boolean isHeaderPosition(int position) {
        return position < getHeaderCount();
    }

    private int getHeaderCount() {
        return headerViews.size();
    }

    public void addHeader(View view) {
        headerViews.put(ITEM_TYPE_HEADER + headerViews.size(), view);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return headerViews.keyAt(position);
        }
        return adapter.getItemViewType(position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount() + getHeaderCount();
    }

    @NonNull
    @Override
    public TreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (headerViews.get(viewType) != null) {
            return new TreeViewHolder(headerViews.get(viewType));
        }
        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            return;
        }
        adapter.onBindViewHolder(holder, position - getHeaderCount());
    }
}
