package com.zhangyf.bidirectio.net.observer;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.zhangyf.bidirectio.net.exception.ErrorException;


/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
public abstract class DialogObserver<T> extends BaseObserver<T> {

    public DialogObserver(Context context) {
        super(context);
    }

    @Override
    public void onError(ErrorException e) {
        new AlertDialog.Builder(getContext()).setMessage(e.msg)
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
