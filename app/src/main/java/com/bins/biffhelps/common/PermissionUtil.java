package com.bins.biffhelps.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class PermissionUtil {

    public final static int REQUEST_CODE = 1000;
    public final static String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_CALENDAR,//日历
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.CAMERA,//相机
            Manifest.permission.READ_CONTACTS,//联系人
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.LOCATION_HARDWARE,//定位
            Manifest.permission.RECORD_AUDIO,//麦克相关

            Manifest.permission.CALL_PHONE,//手机状态
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BODY_SENSORS, //传感器
            Manifest.permission.READ_EXTERNAL_STORAGE, //存储权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,//短信
            Manifest.permission.SEND_SMS,
    };

    /**
     * 是否具有 permissions 中的所有权限
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermissions(Context context, @Size(min = 1L) @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w("EasyPermissions", "hasPermissions: API version < M, returning true by default");
            return true;
        } else if (context == null) {
            throw new IllegalArgumentException("Can't check permissions for null context");
        } else {
            int length = permissions.length;
            for (int i = 0; i < length; ++i) {
                String itemPermission = permissions[i];
                if (ContextCompat.checkSelfPermission(context, itemPermission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 检查 permissions 中的权限是否已授权，没有的话会弹窗让用户授权，返回false。
     * 如果 permissions 中的权限都已授权，返回true。
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean checkPermissions(Activity activity, String[] permissions) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ArrayList<String> requestPermissions = new ArrayList<>();
                int length = permissions.length;
                for (String permission : permissions) {
                    if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(permission)) {
                        // 没有该权限，则加入 requestPermissions 中
                        requestPermissions.add(permission);
                    }
                }
                int size = requestPermissions.size();
                if (size > 0) {
                    // requestPermissions(..)请求用户授权几个权限，调用后系统会显示一个请求用户授权的提示对话框，App不能配置和修改这个对话框
                    // requestPermissions(..)该方法是异步的，所以无返回值，当用户处理完授权操作时，会回调Activity或者Fragment的onRequestPermissionsResult()方法。
                    activity.requestPermissions(requestPermissions.toArray(new String[size]), REQUEST_CODE);
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void checkPermission(Activity activity, String permission, int requestCode) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(permission)) {
                    // 没有该权限，则申请
                    activity.requestPermissions(new String[]{permission}, requestCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取 permissions 中哪些权限是被拒绝的
     *
     * @param context
     * @param permissions
     * @return
     */
    public static String[] getDeniedPermissions(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> deniedPermissionList = new ArrayList<>();
            for (String permission : permissions) {
                if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permission);
                }
            }
            int size = deniedPermissionList.size();
            if (size > 0) {
                return deniedPermissionList.toArray(new String[size]);
            }
        }
        return null;
    }

}
