package com.finchina.treeviewlib;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @ProjectName: TreeView
 * @Description: 作用描述 <todo>
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/20 11:19
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/20 11:19
 * @UpdateRemark: 无
 */
public class TreeView extends RecyclerView {

    public TreeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TreeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TreeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
    }

    public void setOnTreeItemDragListener(OnTreeItemDragListener listener) {
        TreeItemTouchCallback callback = new TreeItemTouchCallback(listener);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(this);
    }
}
