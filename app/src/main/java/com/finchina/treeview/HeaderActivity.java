package com.finchina.treeview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.finchina.treeviewlib.OnTreeItemDragListener;
import com.finchina.treeviewlib.OnTreeNodeClickListener;
import com.finchina.treeviewlib.SimpleTreeViewAdapter;
import com.finchina.treeviewlib.TreeNode;
import com.finchina.treeviewlib.TreeNodeHelper;
import com.finchina.treeviewlib.TreeView;
import com.finchina.treeviewlib.TreeViewAdapter;
import com.finchina.treeviewlib.TreeViewHolder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class HeaderActivity extends AppCompatActivity {

    private TreeView treeView;
    private SwitchMaterial switchMaterial;
    private List<TreeNode<TreeModel>> list = new ArrayList<>();
    private List<TreeNode<TreeModel>> editList = new ArrayList<>();
    private int itemPadding;
    private Context context;
    private OnTreeViewDragAdapter<TreeModel> dragAdapter;
    private TreeNode<TreeModel> commonlyUsedNode = new TreeNode<>(new TreeModel("我的常用"));
    private MySimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        treeView = findViewById(R.id.rv_list);
        switchMaterial = findViewById(R.id.sw_switch);
        itemPadding = DensityUtils.dp2px(this, 15);
        initData();
        // initEditList();
        adapter = new MySimpleAdapter(this, R.layout.item_test_1, list);
        adapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener<TreeModel>() {
            @Override
            public void onTreeNodeClick(View view, TreeViewHolder holder, TreeNode<TreeModel> treeNode, int position) {
                if (adapter.isEdit()) {
                    if (treeNode.getData().isAdded()) {
                        dragAdapter.removeTreeNode(commonlyUsedNode, treeNode);
                    } else {
                        dragAdapter.addTreeNode(commonlyUsedNode, treeNode);
                    }
                    treeNode.getData().setAdded(!treeNode.getData().isAdded());
                    adapter.notifyTreeNode(treeNode);
                } else {
                    printNodesPath(treeNode);
                }
            }
        });
        View headerView = initHeaderView();
        adapter.addHeader(headerView);
        treeView.setAdapter(adapter);
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dragAdapter.setEdit(isChecked);
                adapter.setEdit(isChecked);
            }
        });
    }

    private void printNodesPath(TreeNode<TreeModel> treeNode) {
        List<TreeNode<TreeModel>> list = TreeNodeHelper.getRootPathNodes(treeNode);
        StringBuffer sb = new StringBuffer();
        if (list.size() > 0) {
            for (int i = list.size() - 1; i >= 0; i--) {
                TreeNode<TreeModel> node = list.get(i);
                sb.append(node.getData().getName()).append("-->");
            }
            String res = sb.substring(0, sb.length() - 1);
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        }
    }

    private void initEditList() {
        editList.clear();
        List<TreeNode<TreeModel>> edits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            edits.add(new TreeNode<>(new TreeModel("编辑" + i)));
        }
        commonlyUsedNode.setChildren(edits);
        editList.add(commonlyUsedNode);
    }

    private View initHeaderView() {
        View view = View.inflate(context, R.layout.layout_header, null);
        TreeView dragTreeView = view.findViewById(R.id.rv_recycler);
        dragAdapter = new OnTreeViewDragAdapter<TreeModel>(context, R.layout.item_test_1, editList) {
            @Override
            public void convert(TreeViewAdapter<TreeModel> adapter, TreeViewHolder holder, TreeNode<TreeModel> treeNode) {
                RelativeLayout testLayout = holder.getView(R.id.testLayout);
                testLayout.setPadding(treeNode.getLevel() * itemPadding, 0, 0, 0);
                TextView textTv = holder.getView(R.id.textTv);
                ImageView iconImageView = holder.getView(R.id.iv_icon);
                if (adapter.isEdit()) {
                    iconImageView.setImageResource(R.drawable.ic_drag);
                    iconImageView.setPadding(0, 0, 0, 0);
                } else {
                    iconImageView.setImageResource(R.drawable.shape_circle);
                    int dp5 = DensityUtils.dp2px(context, 5);
                    iconImageView.setPadding(dp5, dp5, dp5, dp5);
                }
                textTv.setText(treeNode.getData().getName());
            }
        };
        dragAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener<TreeModel>() {
            @Override
            public void onTreeNodeClick(View view, TreeViewHolder holder, TreeNode<TreeModel> treeNode, int position) {
                if (dragAdapter.isEdit()) {
                    treeNode.getData().setAdded(false);
                    dragAdapter.removeTreeNode(commonlyUsedNode, treeNode);
                    adapter.notifyTreeNode(treeNode);
                } else {
                    printNodesPath(treeNode);
                }
            }
        });
        dragTreeView.setAdapter(dragAdapter);
        dragTreeView.setOnTreeItemDragListener(new OnTreeItemDragListener() {
            @Override
            public boolean isEdit() {
                return dragAdapter.isEdit();
            }

            @Override
            public void itemMove(int from, int to) {
                dragAdapter.itemMove(from, to);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                dragAdapter.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder) {
                dragAdapter.clearView(recyclerView, viewHolder);
            }
        });
        return view;
    }

    private void initData() {
        list.clear();
        editList.clear();
        List<TreeNode<TreeModel>> edits = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TreeNode<TreeModel> treeNode0 = new TreeNode<>(new TreeModel("0 级节点: " + i));
            list.add(treeNode0);
            for (int j = 0; j < 10; j++) {
                TreeModel model = new TreeModel(treeNode0.getData().getName() + " / " + "1 级节点: " + j);
                TreeNode<TreeModel> treeNode2 = new TreeNode<>(model);
                if (i == 0 && j == 0) {
                    model.setAdded(true);
                    edits.add(treeNode2);
                }
                treeNode0.addChild(treeNode2);
            }
        }
        commonlyUsedNode.setChildren(edits);
        editList.add(commonlyUsedNode);
    }

    class MySimpleAdapter extends SimpleTreeViewAdapter<TreeModel> {

        /**
         * 构造器
         *
         * @param context  上下文
         * @param layoutId
         * @param allNodes 树节点根列表（所有数据）
         */
        public MySimpleAdapter(Context context, int layoutId, @NonNull List<TreeNode<TreeModel>> allNodes) {
            super(context, layoutId, allNodes);
        }

        @Override
        public void convert(TreeViewAdapter<TreeModel> adapter, TreeViewHolder holder, TreeNode<TreeModel> treeNode) {
            TreeModel treeModel = treeNode.getData();
            RelativeLayout testLayout = holder.getView(R.id.testLayout);
            testLayout.setPadding((treeNode.getLevel()) * itemPadding, 0, 0, 0);
            TextView titleTxtView = holder.getView(R.id.textTv);
            titleTxtView.setText(treeModel.getName());
            ImageView iconView = holder.getView(R.id.iv_icon);
            if (treeNode.getLevel() == 0) {
                titleTxtView.setTextSize(17);
                titleTxtView.setTextColor(Color.BLACK);
                iconView.setImageResource(R.drawable.ic_company);
                iconView.setPadding(0, 0, 0, 0);
            } else {
                titleTxtView.setTextSize(16);
                titleTxtView.setTextColor(0xFF3C3C3C);
                iconView.setImageResource(R.drawable.shape_circle);
                int dp5 = DensityUtils.dp2px(context, 5);
                iconView.setPadding(dp5, dp5, dp5, dp5);
            }
            ImageView rightImageView = holder.getView(R.id.iv_right);
            if (treeNode.isLeaf()) {     // 叶子节点
                if (adapter.isEdit()) {  // 编辑状态
                    if (treeModel.isAdded()) {
                        rightImageView.setImageResource(R.drawable.ic_remove_circle);
                    } else {
                        rightImageView.setImageResource(R.drawable.ic_add_circle);
                    }
                } else {   // 非编辑状态
                    rightImageView.setImageResource(0);
                }
            } else {      // 非叶子节点
                if (treeNode.isExpand()) {
                    rightImageView.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    rightImageView.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tree, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_expand:
                adapter.expandAllTreeNode();
                dragAdapter.expandAllTreeNode();
                break;
            case R.id.menu_collapse:
                adapter.collapseAllTreeNode();
                dragAdapter.collapseAllTreeNode();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}