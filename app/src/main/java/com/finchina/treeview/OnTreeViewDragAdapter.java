package com.finchina.treeview;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.finchina.treeviewlib.OnTreeItemDragListener;
import com.finchina.treeviewlib.SimpleTreeViewAdapter;
import com.finchina.treeviewlib.TreeNode;
import com.finchina.treeviewlib.TreeViewHolder;

import java.util.Collections;
import java.util.List;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

/**
 * @ProjectName: TreeView
 * @Description: 作用描述 <todo>
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/21 10:58
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/21 10:58
 * @UpdateRemark: 无
 */
public abstract class OnTreeViewDragAdapter<T> extends SimpleTreeViewAdapter<T> implements OnTreeItemDragListener {

    /**
     * 构造器
     *
     * @param context  上下文
     * @param layoutId
     * @param allNodes 树节点根列表（所有数据）
     */
    public OnTreeViewDragAdapter(Context context, int layoutId, @NonNull List<TreeNode<T>> allNodes) {
        super(context, layoutId, allNodes);
    }

    /**
     * 对拖拽的元素进行交换排序
     *
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void itemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(visibleNodeList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(visibleNodeList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ACTION_STATE_DRAG) {
            // 长按时调用
            if (viewHolder instanceof TreeViewHolder) {
                TreeViewHolder holder = (TreeViewHolder) viewHolder;
                View view = holder.getView(R.id.ll_drag);
                view.setBackgroundResource(R.drawable.shape_bg_item);
                view.setElevation(30f);
            }
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder) {
        TreeViewHolder holder = (TreeViewHolder) viewHolder;
        View view = holder.getView(R.id.ll_drag);
        view.setBackgroundResource(0);
    }
}
