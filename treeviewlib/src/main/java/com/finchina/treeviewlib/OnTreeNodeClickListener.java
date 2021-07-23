package com.finchina.treeviewlib;

import android.view.View;

/**
 * @ProjectName: TreeView
 * @Description: TreeView节点点击事件
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/22 16:57
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/22 16:57
 * @UpdateRemark: 无
 */
public interface OnTreeNodeClickListener<T> {

    void onTreeNodeClick(View view, TreeViewHolder holder, TreeNode<T> treeNode, int position);
}
