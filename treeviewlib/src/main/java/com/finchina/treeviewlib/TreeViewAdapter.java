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
 * 树节点多级列表适配器
 *
 * @param <T> 泛型，树节点数据的泛型
 */
public class TreeViewAdapter<T> extends RecyclerView.Adapter<TreeViewHolder> {

    protected Context context;// 上下文
    private final LayoutInflater layoutInflater;// 视图渲染器
    private final List<TreeViewDelegate<T>> treeViewDelegateList = new ArrayList<>();  // 所有布局类型
    protected final List<TreeNode<T>> visibleNodeList = new ArrayList<>();  // 当前显示的树节点
    protected List<TreeNode<T>> allNodeList;// 所有树节点
    private int maxSelectCount; // 多选个数 -->> 0：任意 非0：限制数量
    private final List<TreeNode<T>> selectTreeNodeList = new ArrayList<>();// 选中
    private boolean isEdit;  // 是否是编辑状态
    public static final int ITEM_TYPE_HEADER = 1000;    // 头部布局类型
    private final SparseArray<View> headerViews = new SparseArray<>();   // 头部布局List
    private OnTreeNodeClickListener<T> mTreeNodeClickListener;   // 节点监听器

    /**
     * 构造器
     *
     * @param context  上下文
     * @param allNodes 树节点根列表（所有数据）
     */
    public TreeViewAdapter(Context context, @NonNull List<TreeNode<T>> allNodes) {
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
        for (TreeNode<T> treeNode : allNodeList) {
            visibleNodeList.add(treeNode);
            if (treeNode.isExpand()) {
                visibleNodeList.addAll(TreeNodeHelper.getExpendedChildren(treeNode));
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
                TreeNode<T> treeNode = visibleNodeList.get(position);
                if (treeNode == null) {
                    return;
                }
                if (treeNode.isLeaf()) {   // 叶子节点, 事件抛出去
                    if (mTreeNodeClickListener != null) {
                        mTreeNodeClickListener.onTreeNodeClick(v, holder, treeNode, position);
                    }
                } else {
                    expandOrCollapseTreeNode(treeNode);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull TreeViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            return;
        }
        int viewType = getItemViewType(position);
        TreeViewDelegate<T> delegate = treeViewDelegateList.get(viewType);
        delegate.convert(holder, visibleNodeList.get(position - getHeaderCount()));
    }

    @Override
    public int getItemCount() {
        return visibleNodeList.size() + getHeaderCount();
    }

    /**
     * item 类型返回的是 treeNodeDelegateList 中的索引值
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return headerViews.keyAt(position);
        }
        TreeNode<T> treeNode = visibleNodeList.get(position - getHeaderCount());
        for (int i = 0; i < treeViewDelegateList.size(); i++) {
            TreeViewDelegate<T> delegate = treeViewDelegateList.get(i);
            if (delegate.isItemType(position - getHeaderCount(), treeNode)) {
                return i;
            }
        }
        throw new IllegalArgumentException("No TreeNodeDelegate added that matches position=" + position + " in data source");
    }

    /**
     * 添加视图样式
     *
     * @param delegate 样式
     */
    public void addItemViewDelegate(TreeViewDelegate<T> delegate) {
        delegate.adapter = this;
        treeViewDelegateList.add(delegate);
    }

    /**
     * 移除视图样式
     *
     * @param delegate 样式
     */
    public void removeItemViewDelegate(TreeViewDelegate<T> delegate) {
        treeViewDelegateList.remove(delegate);
    }

    /**
     * 刷新
     */
    public void refreshTreeNode() {
        init();
        notifyDataSetChanged();
    }

    /**
     * 展开树节点
     *
     * @param treeNode 树节点
     */
    public void expandTreeNode(TreeNode<T> treeNode) {
        if (treeNode == null) {
            return;
        }
        treeNode.setExpand(!treeNode.isExpand());
        int index = visibleNodeList.indexOf(treeNode);
        List<TreeNode<T>> children = TreeNodeHelper.getExpendedChildren(treeNode);
        if (index < 0 || index > visibleNodeList.size() - 1 || children.size() == 0) {
            return;
        }
        visibleNodeList.addAll(index + 1, children);
        notifyItemRangeInserted(index + 1 + getHeaderCount(), children.size());
        notifyTreeNode(treeNode);
    }

    /**
     * 收缩树节点
     *
     * @param treeNode 树节点
     */
    public void collapseTreeNode(TreeNode<T> treeNode) {
        if (treeNode == null)
            return;
        treeNode.setExpand(!treeNode.isExpand());
        int index = visibleNodeList.indexOf(treeNode);
        List<TreeNode<T>> children = TreeNodeHelper.getExpendedChildren(treeNode);
        if (index < 0 || index > visibleNodeList.size() - 1 || children == null || children.size() == 0) {
            return;
        }
        visibleNodeList.removeAll(children);
        notifyItemRangeRemoved(index + 1 + getHeaderCount(), children.size());
        notifyTreeNode(treeNode);
    }

    /**
     * 伸缩书记诶单
     *
     * @param treeNode 树节点
     */
    public void expandOrCollapseTreeNode(TreeNode<T> treeNode) {
        if (treeNode.isExpand()) {
            collapseTreeNode(treeNode);
        } else {
            expandTreeNode(treeNode);
        }
    }

    /**
     * 刷新树节点
     *
     * @param treeNode 树节点
     */
    public void notifyTreeNode(TreeNode<T> treeNode) {
        int index = visibleNodeList.indexOf(treeNode);
        if (index < 0 || index > visibleNodeList.size() - 1) {
            return;
        }
        notifyItemChanged(visibleNodeList.indexOf(treeNode) + getHeaderCount());
    }

    /**
     * 展开所有层级的树节点
     */
    public void expandAllTreeNode() {
        TreeNodeHelper.expandAll(allNodeList);
        refreshTreeNode();
    }

    /**
     * 收缩所有层级的树节点
     */
    public void collapseAllTreeNode() {
        TreeNodeHelper.collapseAll(allNodeList);
        refreshTreeNode();
    }

    /**
     * 展开层级内的树节点
     *
     * @param level 层级
     */
    public void expandLevelTreeNode(int level) {
        TreeNodeHelper.expandLevel(allNodeList, level);
        refreshTreeNode();
    }

    /**
     * 添加节点
     *
     * @param parent
     * @param node
     */
    public void addTreeNode(TreeNode<T> parent, TreeNode<T> node) {
        parent.addChild(node);
        refreshTreeNode();
    }

    /**
     * 移除节点
     *
     * @param parent
     * @param node
     */
    public void removeTreeNode(TreeNode<T> parent, TreeNode<T> node) {
        parent.getChildren().remove(node);
        refreshTreeNode();
    }

    /**
     * 设置可选树节点最大数量
     *
     * @param maxSelectCount 数量
     */
    public void setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
    }

    /**
     * 获取选中树节点集合
     *
     * @return 选中树节点集合
     */
    public List<TreeNode<T>> getSelectTreeNodeList() {
        return selectTreeNodeList;
    }

    /**
     * 改变选中状态，并刷新
     * 当限制最多选中数量，超出的部分会把先加入的树节点移除
     *
     * @param treeNode 树节点
     * @param select   选中状态
     */
    public void selectTreeNode(TreeNode<T> treeNode, boolean select) {
        treeNode.setSelect(select);
        if (select) {
            if (!selectTreeNodeList.contains(treeNode)) {// 添加并刷新
                selectTreeNodeList.add(treeNode);
                notifyTreeNode(treeNode);
            }
            if (maxSelectCount != 0) {// 多选且不限制数量
                if (selectTreeNodeList.size() > maxSelectCount) {// 删除首个已选并刷新
                    TreeNode<T> first = selectTreeNodeList.get(0);
                    first.setSelect(false);
                    selectTreeNodeList.remove(first);
                    notifyTreeNode(first);
                }
            }
        } else {
            if (selectTreeNodeList.contains(treeNode)) {// 移除并刷新
                selectTreeNodeList.remove(treeNode);
                notifyTreeNode(treeNode);
            }
        }
    }

    /**
     * 改变树节点选中状态
     *
     * @param treeNode 树节点
     */
    public void changeSelectTreeNode(TreeNode<T> treeNode) {
        selectTreeNode(treeNode, !treeNode.isSelect());
    }

    private boolean isHeaderPosition(int position) {
        return position < getHeaderCount();
    }

    /**
     * Header 数量
     *
     * @return
     */
    private int getHeaderCount() {
        return headerViews.size();
    }

    /**
     * 添加 header
     *
     * @param view
     */
    public void addHeader(View view) {
        headerViews.put(ITEM_TYPE_HEADER + headerViews.size(), view);
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    /**
     * 设置节点点击事件
     *
     * @param listener
     */
    public void setOnTreeNodeClickListener(OnTreeNodeClickListener<T> listener) {
        this.mTreeNodeClickListener = listener;
    }
}
