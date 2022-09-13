package com.bins.biffhelps.common;

import android.content.DialogInterface;

public interface ILoadTag {
    void show();

    void dismiss();

    String getTagMsg();

    ILoadTag setOnDismissListener(DialogInterface.OnDismissListener onDismiss);
}
