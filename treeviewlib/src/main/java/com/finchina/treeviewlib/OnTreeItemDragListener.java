package com.finchina.treeviewlib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @ProjectName: TreeView
 * @Description: RecyclerView 拖拽
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/21 9:45
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/21 9:45
 * @UpdateRemark: 无
 */
public interface OnTreeItemDragListener {

    // 是否编辑状态
    boolean isEdit();

    void itemMove(int from, int to);

    // 长按调用
    void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState);

    //松手时调用
    void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder);
}
