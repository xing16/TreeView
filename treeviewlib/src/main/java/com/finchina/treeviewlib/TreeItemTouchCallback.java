package com.finchina.treeviewlib;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @ProjectName: MultiLevelList
 * @Description: 作用描述 <todo>
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/19 16:37
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/19 16:37
 * @UpdateRemark: 无
 */
public class TreeItemTouchCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "ItemDragCallback";
    private final OnTreeItemDragListener listener;

    public TreeItemTouchCallback(OnTreeItemDragListener listener) {
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "getMovementFlags() called with: recyclerView = [" + recyclerView + "], viewHolder = [" + viewHolder + "]");
        // 是否是编辑状态
        if (!listener.isEdit()) {
            return 0;
        }
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        Log.d(TAG, "onMove() called with: recyclerView = [" + recyclerView + "], viewHolder = [" + viewHolder + "], target = [" + target + "]");
        int fromPosition = viewHolder.getAdapterPosition();   // 拖动的position
        int toPosition = target.getAdapterPosition();         // 释放的position
        // int position = viewHolder.getLayoutPosition();
        if (listener != null) {
            listener.itemMove(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (dX != 0 && dY != 0 || isCurrentlyActive) {
            // SimpleTreeViewAdapter adapter = (SimpleTreeViewAdapter) recyclerView.getAdapter();
            // isEdit = false;
        }
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (listener != null) {
            listener.onSelectedChanged(viewHolder, actionState);
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //松手时调用
        if (listener != null) {
            listener.clearView(recyclerView, viewHolder);
        }
    }
}
