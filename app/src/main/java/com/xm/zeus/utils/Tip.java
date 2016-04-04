package com.xm.zeus.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 作者：小孩子xm on 2016-04-04 11:29
 * 邮箱：1065885952@qq.com
 */
public class Tip {

    public static void toast(Context ctx, String msg) {
        if (ctx == null || TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
