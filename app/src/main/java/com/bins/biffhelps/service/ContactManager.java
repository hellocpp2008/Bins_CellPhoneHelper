package com.bins.biffhelps.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.bins.biffhelps.activity.MainActivity;
import com.bins.biffhelps.common.PermissionUtil;
import com.bins.biffhelps.model.ContactModel;
import com.bins.biffhelps.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc:
 *
 * @author zhouxingbin
 * @date 2022/8/22
 */
public class ContactManager {

    static final String TAG = "ContactManager";

    /**
     * 读取通讯录
     *
     * @param activity
     *
     * @return
     */
    public static List<ContactModel> readContacts(Activity activity) {
        List<ContactModel> contactsList = new ArrayList<>();
        Cursor cursor = null;
        try {
            PermissionUtil.checkPermission(activity, Manifest.permission.READ_CONTACTS, 1);
            cursor = activity.getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 获取联系人姓名（为什么要加 @SuppressLint("Range") 注解，参https://blog.csdn.net/weixin_51395622/article/details/121911873）
                    @SuppressLint("Range") String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // 获取联系人手机号
                    @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactModel contactModel = new ContactModel();
                    contactModel.setName(displayName);
                    contactModel.setMobileNumber(number);
                    contactsList.add(contactModel);
                }
            }
            Log.i(TAG, "====>>> 获取的contactsList大小是" + contactsList.size() + ",内容是:" + GsonUtil.obj2Json(contactsList));
            //     [{
            //         "mobileNumber": "+86 156 1920 7676",
            //         "name": "背调电话",
            //         "baseObjId": 0
            //     }, {
            //         "mobileNumber": "+86 400 603 7555",
            //         "name": "老虎证券客服",
            //         "baseObjId": 0
            //     }]
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "====>>> 异常" + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contactsList;
    }

    public static void callPhone(Context mContext, String phoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNum));
            mContext.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
