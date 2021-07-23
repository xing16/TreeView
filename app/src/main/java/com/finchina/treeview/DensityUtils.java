package com.finchina.treeview;

import android.content.Context;
import android.util.TypedValue;

/**
 * @ProjectName: TreeView
 * @Description: 作用描述 <todo>
 * @Author: xinxing.tao
 * @CreateDate: 2021/7/22 17:34
 * @UpdateUser: xinxing.tao
 * @UpdateDate: 2021/7/22 17:34
 * @UpdateRemark: 无
 */
public class DensityUtils {
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
