package com.bins.biffhelps.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bins.biffhelps.utils.GsonUtil;
import com.bins.biffhelps.utils.Logger;

public abstract class PermissionActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();
    protected Activity activity;

    /**
     *
     * @param context
     * @return false:context是根Activity；true:context不是根Activity。
     */
    public static boolean isFirstOpen(Activity context) {
        // 通过 isTaskRoot() 方法来判断是否是任务栈中的根Activity，如果是就不做任何处理，如果不是则直接finish掉;
        if (!context.isTaskRoot()) {
            Intent intent = context.getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                context.finish();
                return true;
            }
        }
        return false;
    }

    @Override
    @Deprecated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("PermissionActivity", TAG + ":onCreate");
        activity = this;
        if (isFirstOpen(activity)) {
            return;
        }
        if (PermissionUtil.checkPermissions(this, onCheckPermission())) {
            // 如果都已授权，调用onPermissionAccept(true)
            onPermissionAccept(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.e("PermissionActivity", TAG + ":onResume");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.e("PermissionActivity", TAG + ":onNewIntent");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.REQUEST_CODE == requestCode) {
            // 获取拒绝授权的权限
            String[] arr = PermissionUtil.getDeniedPermissions(this, permissions);
            Logger.e(TAG, "arr = " + GsonUtil.obj2Json(arr));
            // accept 等于 true：说明都已经授权(拒绝的权限为 null）
            boolean accept = null == arr || 0 == arr.length;
            onPermissionAccept(accept);
        }
    }

    /**
     * 设置检测权限的数组
     *
     * @return
     */
    protected abstract String[] onCheckPermission();

    /**
     * 权限检测结果
     *
     * @param accept true:所需权限都已授权。false:所需权限没有已授权。
     */
    protected abstract void onPermissionAccept(boolean accept);
}