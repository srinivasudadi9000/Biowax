package com.srinivas.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by RajeshT on 21-11-2017.
 */

public class DialogUtils {
    public static ProgressDialog showProgressDialog(Context context, String message){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }
}
