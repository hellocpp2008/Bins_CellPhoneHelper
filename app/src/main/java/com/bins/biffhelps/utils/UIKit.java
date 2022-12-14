package com.bins.biffhelps.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

public class UIKit {
    public final static String KEY_BASE = "key_basis";
    public final static String KEY_BASE1 = "key_basis1";
    public final static String KEY_OBJ = "key_obj";

    private static Application mBaseContext;
    private final static Handler mainHand = new Handler(Looper.getMainLooper());

    public static Context getContext() {
        if (null == mBaseContext) {
            mBaseContext = getInstanceFromReflexG();
        }
        return mBaseContext;
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static void postDelayed(Runnable r, long delay) {
        mainHand.postDelayed(r, delay);
    }

    public static void removeTask(Runnable r) {
        if (null != r) mainHand.removeCallbacks(r);
    }

    public static void runOnUiTherad(Runnable r) {
        if (null != r) mainHand.post(r);
    }

    public static AssetManager getAssets() {
        return getContext().getAssets();
    }

    public static View inflate(int layoutId) {
        return View.inflate(getContext(), layoutId, null);
    }

    public static <T extends View> T getView(Activity activity, int viewId) {
        return (T) activity.findViewById(viewId);
    }

    public static <T extends View> T getView(View parent, int viewId) {
        return (T) parent.findViewById(viewId);
    }

    public static <T extends View> void setVisiable(T t, boolean visiable) {
        if (null == t) return;
        t.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public static <T extends Activity> void startActivity(Activity actx, Class<T> activityClass) {
        actx.startActivity(new Intent(actx, activityClass));
    }

    public static <T extends Activity> void startActivityByObj(Activity actx, Class<T> activityClass, Serializable serializable) {
        actx.startActivity(new Intent(actx, activityClass).putExtra(KEY_OBJ, serializable));
    }

    public static <T extends Activity> void startActivityByBasis(Activity actx, Class<T> activityClass, Serializable serializable) {
        actx.startActivity(new Intent(actx, activityClass).putExtra(KEY_BASE, serializable));
    }

    private static Application getInstanceFromReflexG() {
        Application application = null;
        try {
            application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (application == null) {
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
            }
        } catch (final Exception e) {
            try {
                application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                e.printStackTrace();
            }
        }
        return application;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param layout
     * @param clazz
     * @param maxLevel
     * @param <T>
     * @return
     */
    public static <T extends View> T getFirstViewByClass(View layout, Class clazz, int maxLevel) {
        if (clazz.isInstance(layout)) {
            Logger.e(clazz.getSimpleName() + " ?????????????????????");
            return (T) layout;
        } else if (layout instanceof ViewGroup) {
            return findChildFromTreeByTypeCalss((ViewGroup) layout, clazz, 1, maxLevel);
        }
        return null;
    }

    /**
     * ???????????????????????????????????????
     * ???????????????????????????
     *
     * @param layout   ?????????????????????
     * @param level    ????????????
     * @param maxLevel ???????????? ??????0 ??????????????????
     */
    @Deprecated
    public static <T extends View> T findChildFromTreeByTypeCalss(ViewGroup layout, Class clazz, int level, int maxLevel) {
        if (level < 1) {
            level = 1;
        }
        if (maxLevel > 0 && level > maxLevel) {
            return null;
        }
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {//????????????????????????
            View ch1 = layout.getChildAt(i);
            if (clazz.isInstance(ch1)) {
                Logger.e(clazz.getSimpleName() + " ???" + level + "???????????????");
                return (T) ch1;
            } else if (ch1 instanceof ViewGroup) {
                ViewGroup chg = (ViewGroup) ch1;
                T t = findChildFromTreeByTypeCalss(chg, clazz, level + 1, maxLevel);
                if (null != t) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * ????????????????????????????????????child
     *
     * @param children ????????????????????????
     * @param layout   ?????????
     * @param clazz    ??????child????????????
     * @param <T>
     */
    public static <T extends View> void findChildrenFromTreeByTypeCalss(List<T> children, ViewGroup layout, Class clazz) {
        if (null == children) return;
        int count = layout.getChildCount();
        for (int i = 0; i < count; i++) {//????????????????????????
            View ch1 = layout.getChildAt(i);
            if (clazz.isInstance(ch1)) {
                children.add((T) ch1);
            } else if (ch1 instanceof ViewGroup) {
                ViewGroup chg = (ViewGroup) ch1;
                findChildrenFromTreeByTypeCalss(children, chg, clazz);
            }
        }
    }
}
