package com.bins.biffhelps.activity;

import android.Manifest;
import android.os.Build;
import android.text.TextUtils;
import android.widget.EditText;

import com.bins.biffhelps.R;
import com.bins.biffhelps.common.KToast;
import com.bins.biffhelps.common.LoadTag;
import com.bins.biffhelps.common.PermissionActivity;
import com.bins.biffhelps.utils.Logger;
import com.bins.biffhelps.utils.UIKit;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Desc:
 *
 * @author zhouxingbin
 * @date 2022/8/6
 */
public class LoginActivity extends PermissionActivity {

    // ████ 这里定义的权限，都还得在 AndroidManifest.xml 中定义 ████
    protected final static String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS,
    };

    @Override
    protected String[] onCheckPermission() {
        return REQUIRED_PERMISSIONS;
    }

    @Override
    protected void onPermissionAccept(boolean accept) {
        Logger.e(TAG, "accept = " + accept);
        if (accept) {
            // 进入登录页面之前，会先弹窗让用户授权，授权之后才会进入登录页面
            initView();
        }
    }

    private EditText et_phone;

    void initView() {
        setContentView(R.layout.activity_login);
        et_phone = findViewById(R.id.et_phone);
        findViewById(R.id.btn_login).setOnClickListener(v -> {
            String phone = et_phone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                KToast.show("请输入手机号");
                return;
            }
            login(phone, "111111");
        });
    }

    void login(String phone, String code) {
        LoadTag tag = new LoadTag(activity, "登录...");
        tag.show();
        Map<String, Object> params = new HashMap<>(4);
        params.put("mobile", phone);
        params.put("verifyCode", code);
        params.put("deviceId", getDeviceId());
 /*       OkApi.post(Api.LOGIN, params, new WrapperCallBack() {
            @Override
            public void onResult(Wrapper result) {
                if (null != tag) tag.dismiss();
                Logger.e("result = " + GsonUtil.obj2Json(result));
                if (result.getCode() == 10000) {
                    Account account = result.get(Account.class);
                    if (null != account) {
                        QuickApplication.setAuthorization(account.getAuthorization());
                        AccoutManager.setAcctount(account, true);
                        connect(account);
                    }
                }
            }
        });*/
        UIKit.startActivity(LoginActivity.this, MainActivity.class);
    }

    static String getDeviceId() {
        String deviceIdShort =
                "35" + (Build.BOARD.length() % 10) +
                        (Build.BRAND.length() % 10) +
                        (Build.CPU_ABI.length() % 10) +
                        (Build.DEVICE.length() % 10) +
                        (Build.MANUFACTURER.length() % 10) +
                        (Build.MODEL.length() % 10) +
                        (Build.PRODUCT.length() % 10) +
                        (Build.SERIAL.length() % 10);
        return UUID.nameUUIDFromBytes(deviceIdShort.getBytes(StandardCharsets.UTF_8)).toString();
    }
}
