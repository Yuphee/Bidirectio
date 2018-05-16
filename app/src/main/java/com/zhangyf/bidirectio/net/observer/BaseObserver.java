package com.zhangyf.bidirectio.net.observer;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.zhangyf.bidirectio.net.Optional;
import com.zhangyf.bidirectio.net.exception.AppException;
import com.zhangyf.bidirectio.net.exception.ErrorException;
import com.zhangyf.bidirectio.net.exception.ExceptionEngine;
import com.zhangyf.bidirectio.net.exception.YzError;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
public abstract class BaseObserver<T> implements Observer<Optional<T>> {

    private WeakReference<Context> contextRef;

    public BaseObserver(Context context) {
        contextRef = new WeakReference<>(context);
    }

    protected Context getContext() {
        return contextRef == null ? null : contextRef.get();
    }

    @Override
    public void onError(Throwable t) {
        final Context context = contextRef.get();
        if (context == null) return;

        try {
            onError(ExceptionEngine.handleException(t));
        } catch (Exception e) {
            onError(new JsonParseException(e.toString()));
        }

        Log.e("zhangyf", "error----------:" + t.getMessage());
        t.printStackTrace();
        Log.e("zhangyf", "error----------end");
    }

    public abstract void onError(ErrorException e) throws Exception;

    public abstract void onCatchNext(T response) throws Exception;

    @Override
    public void onNext(Optional<T> t) {
        try {
            onCatchNext(t.getIncludeNull());
        } catch (Exception e) {
            onError(new AppException(YzError.APP_ERROR, e.toString()));
            Log.e("zhangyf", "error----------:" + e.toString());
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

}
