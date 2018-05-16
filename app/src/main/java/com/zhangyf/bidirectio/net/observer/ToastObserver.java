package com.zhangyf.bidirectio.net.observer;

import android.content.Context;
import android.widget.Toast;

import com.zhangyf.bidirectio.R;
import com.zhangyf.bidirectio.net.exception.ErrorException;
import com.zhangyf.bidirectio.net.exception.YzError;


/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
public abstract class ToastObserver<T> extends BaseObserver<T> {

    public ToastObserver(Context context) {
        super(context);
    }

    @Override
    public void onError(ErrorException e) {
        final Context context = getContext();
        if (context == null) {
            return;
        }
        doBizErrorCodeAction(context, e);
    }

    /**
     * 业务code处理
     *
     * @param context e
     */
    private void doBizErrorCodeAction(Context context, ErrorException e) {
        if (e.type == YzError.SERVICE_ERROR) {
            switch (e.code) {
                case 100:
                    Toast.makeText(context,"code 100",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, e.msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (e.type == YzError.APP_ERROR) {

        } else {
            Toast.makeText(context, e.msg, Toast.LENGTH_SHORT).show();
        }
    }

}
