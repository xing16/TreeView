package com.finchina.treeviewlib;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: TreeView
 * @Description: 作用描述 <todo>
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/23 13:18
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/23 13:18
 * @UpdateRemark: 无
 */
public class MyAdapter<T> extends RecyclerView.Adapter<TreeViewHolder> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<TreeNodeViewModel<T>> allNodeList = new ArrayList<>();
    private List<TreeNodeViewModel<T>> visibleNodeList = new ArrayList<>();
    private final List<TreeViewDelegate<T>> treeViewDelegateList = new ArrayList<>();  // 所有布局类型

    public static final int ITEM_TYPE_HEADER = 1000;    // 头部布局类型
    private final SparseArray<View> headerViews = new SparseArray<>();   // 头部布局List

    /**
     * 构造器
     *
     * @param context  上下文
     * @param allNodes 树节点根列表（所有数据）
     */
    public MyAdapter(Context context, @NonNull List<TreeNodeViewModel<T>> allNodes) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.allNodeList = allNodes;
        init();
    }

    /**
     * 初始化展开的数据
     */
    private void init() {
        visibleNodeList.clear();
        for (TreeNodeViewModel<T> nodeViewModel : allNodeList) {
            visibleNodeList.add(nodeViewModel);
            if (nodeViewModel.isExpand()) {
                visibleNodeList.addAll(TreeNodeHelper.getExpendedChildren(nodeViewModel));
            }
        }
    }

    @NonNull
    @Override
    public TreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (headerViews.get(viewType) != null) {
            return new TreeViewHolder(headerViews.get(viewType));
        }
        TreeViewDelegate<T> delegate = treeViewDelegateList.get(viewType);
        View itemView = layoutInflater.inflate(delegate.getLayoutId(), parent, false);
        TreeViewHolder holder = new TreeViewHolder(itemView);
        bindViewClickListener(holder);
        return holder;
    }

    private void bindViewClickListener(TreeViewHolder holder) {
        if (holder == null) {
            return;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getLayoutPosition() - getHeaderCount();
                // TreeNode<T> treeNode = visibleNodeList.get(position);
                // if (treeNode == null) {
                //     return;
                // }
                // if (treeNode.isLeaf()) {   // 叶子节点, 事件抛出去
                //     if (mTreeNodeClickListener != null) {
                //         mTreeNodeClickListener.onTreeNodeClick(v, holder, treeNode, position);
                //     }
                // } else {
                //     expandOrCollapseTreeNode(treeNode);
                // }
            }
        });
    }





    @Override
    public void onBindViewHolder(@NonNull TreeViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return visibleNodeList.size() + getHeaderCount();
    }

    /**
     * Header 数量
     *
     * @return
     */
    private int getHeaderCount() {
        return headerViews.size();
    }


    private boolean isHeaderPosition(int position) {
        return position < getHeaderCount();
    }
}
