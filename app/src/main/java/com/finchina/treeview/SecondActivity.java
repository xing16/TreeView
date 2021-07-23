package com.finchina.treeview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.finchina.treeviewlib.TreeNode;
import com.finchina.treeviewlib.TreeViewDelegate;
import com.finchina.treeviewlib.TreeNodeHelper;
import com.finchina.treeviewlib.TreeView;
import com.finchina.treeviewlib.TreeViewAdapter;
import com.finchina.treeviewlib.TreeViewHolder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private TreeView treeView;
    private List<TreeNode<String>> list = new ArrayList<>();
    private List<TreeNode<String>> editList = new ArrayList<>();
    private int itemPadding;
    private SwitchMaterial switchMaterial;
    private Context context;
    private OnTreeViewDragAdapter treeDragAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        treeView = findViewById(R.id.rv_list);
        switchMaterial = findViewById(R.id.sw_switch);
        initEditList();
        initData();
        itemPadding = DensityUtils.dp2px(this, 15);
        TreeViewAdapter<String> adapter = new TreeViewAdapter<>(this, list);
        adapter.addItemViewDelegate(new TreeViewDelegate<String>() {
            @Override
            public boolean isItemType(int position, TreeNode<String> treeNode) {
                return treeNode.getType() == 2;
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_list;
            }

            @Override
            public void convert(TreeViewHolder holder, TreeNode<String> treeNode) {
                TreeView treeView = holder.getView(R.id.rv_item_list);
                Log.e("dddddd", "convert: " + 2322);
                if (treeDragAdapter == null) {
                    treeDragAdapter = new OnTreeViewDragAdapter<String>(context, R.layout.item_test_1, editList) {

                        @Override
                        public void convert(TreeViewAdapter<String> adapter1, TreeViewHolder holder, TreeNode<String> treeNode) {
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
                            textTv.setText(treeNode.getData());
                            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String value = treeNode.getData();
                                }
                            });
                        }
                    };
                    treeView.setAdapter(treeDragAdapter);
                    treeView.setOnTreeItemDragListener(new OnTreeItemDragListener() {
                        @Override
                        public boolean isEdit() {
                            return adapter.isEdit();
                        }

                        @Override
                        public void itemMove(int from, int to) {
                            treeDragAdapter.itemMove(from, to);
                        }

                        @Override
                        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder,
                                                      int actionState) {
                            treeDragAdapter.onSelectedChanged(viewHolder, actionState);
                        }

                        @Override
                        public void clearView(@NonNull RecyclerView recyclerView,
                                              @NonNull RecyclerView.ViewHolder viewHolder) {
                            treeDragAdapter.clearView(recyclerView, viewHolder);
                        }
                    });
                } else {
                    treeDragAdapter.notifyDataSetChanged();
                }
            }
        });
        adapter.addItemViewDelegate(new TreeViewDelegate<String>() {
            @Override
            public boolean isItemType(int position, TreeNode<String> treeNode) {
                return treeNode.getType() != 2;
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_test_1;
            }

            @Override
            public void convert(TreeViewHolder holder, TreeNode<String> treeNode) {
                RelativeLayout testLayout = holder.getView(R.id.testLayout);
                testLayout.setPadding((treeNode.getLevel()) * itemPadding, 0, 0, 0);
                TextView titleTxtView = holder.getView(R.id.textTv);
                titleTxtView.setText(treeNode.getData());
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
                if (!treeNode.isLeaf()) {
                    if (treeNode.isExpand()) {
                        rightImageView.setImageResource(R.drawable.ic_arrow_up);
                    } else {
                        rightImageView.setImageResource(R.drawable.ic_arrow_down);
                    }
                } else {
                    if (adapter.isEdit()) {  // 编辑状态
                        rightImageView.setImageResource(R.drawable.ic_add_circle);
                    } else {   // 非编辑状态
                        rightImageView.setImageResource(0);
                    }
                }
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (treeNode.isLeaf()) {
                            List<TreeNode<String>> list = TreeNodeHelper.getRootPathNodes(treeNode);
                            StringBuffer sb = new StringBuffer();
                            if (list.size() > 0) {
                                for (int i = list.size() - 1; i >= 0; i--) {
                                    TreeNode<String> node = list.get(i);
                                    sb.append(node.getData()).append("/");
                                }
                                String res = sb.substring(0, sb.length() - 1);
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            adapter.expandOrCollapseTreeNode(treeNode);
                        }
                    }
                });
            }
        });
        treeView.setAdapter(adapter);
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setEdit(isChecked);
                Log.e("cacasdcasdc", "onCheckedChanged: " + Arrays.toString(list.toArray()));
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            TreeNode<String> treeNode0 = new TreeNode<>("0级:" + i);
            list.add(treeNode0);
            for (int j = 0; j < 10; j++) {
                TreeNode<String> treeNode1 = new TreeNode<>("1级:" + j);
                treeNode0.addChild(treeNode1);
                if (i == 0 && j == 0) {
                    treeNode1.setType(2);
                    treeNode1.setChildren(editList);
                } else {
                    for (int k = 0; k < 10; k++) {
                        TreeNode<String> treeNode2 = new TreeNode<>("2级:" + k);
                        treeNode1.addChild(treeNode2);
                        for (int m = 0; m < 10; m++) {
                            TreeNode<String> treeNode3 = new TreeNode<>("3级:" + m);
                            treeNode2.addChild(treeNode3);
                        }
                    }
                }
            }
        }
    }

    private void initEditList() {
        for (int i = 0; i < 10; i++) {
            editList.add(new TreeNode<>("编辑" + i));
        }
    }
}