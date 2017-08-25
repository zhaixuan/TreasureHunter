package com.zhuoxin.treasurehunter.treasurehunter.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;

/**
 * Created by Dionysus on 2017/8/25.
 */

public class AlertDialogFragment extends DialogFragment {
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_MESSAGE = "key_message";

    public static AlertDialogFragment getInstance (String title, String message){
        AlertDialogFragment mAlertDialogFragment = new AlertDialogFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(KEY_TITLE,title);
        mBundle.putString(KEY_MESSAGE,message);
        mAlertDialogFragment.setArguments(mBundle);
        return mAlertDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mArguments = getArguments();
        String mTitle = mArguments.getString(KEY_TITLE);
        String mMessage = mArguments.getString(KEY_MESSAGE);
        return new AlertDialog.Builder(getContext())
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
    }
}
