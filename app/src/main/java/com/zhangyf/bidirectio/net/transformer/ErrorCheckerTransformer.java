package com.zhangyf.bidirectio.net.transformer;

import android.content.Context;

import com.zhangyf.bidirectio.net.BaseResponse;
import com.zhangyf.bidirectio.net.Optional;
import com.zhangyf.bidirectio.net.exception.BusinessException;
import com.zhangyf.bidirectio.net.exception.YzError;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;


/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
public class ErrorCheckerTransformer<Z>
        implements ObservableTransformer<BaseResponse<Z>, Optional<Z>> {

    private WeakReference<Context> contextRef;

    public ErrorCheckerTransformer(final Context context) {
        contextRef = new WeakReference<>(context);
    }

    @Override
    public Observable<Optional<Z>> apply(Observable<BaseResponse<Z>> upstream) {
        return upstream.map(new Function<BaseResponse<Z>, Optional<Z>>() {

            @Override
            public Optional<Z> apply(BaseResponse<Z> z) throws Exception {
                String msg = null;
                int code = -1;
                if (z.code != BaseResponse.CODE_SUCCESS) {
                    code = z.code;
                    msg = z.msg;
                    if (msg == null) {
                        msg = YzError.MSG_SERVICE_ERROR;
                    }
                }
//                else if (z.data.bizCode != BaseResponse.BIZ_CODE_SUCCESS) {
//                    code = z.data.bizCode;
//                    msg = z.data.msg;
//                    if (msg == null) {
//                        msg = YzError.MSG_SERVICE_ERROR;
//                    }
//                }

                if (msg != null) {
                    try {
                        throw new BusinessException(code, msg);
                    } catch (BusinessException e) {
                        throw Exceptions.propagate(e);
                    }
                }

                return new Optional<Z>(z.data);
            }
        });
    }

    private static <Z> Observable<Optional<Z>> createHttpData(Optional<Z> t) {

        return Observable.create(e -> {
            try {
                e.onNext(t);
                e.onComplete();
            } catch (Exception exc) {
                e.onError(exc);
            }
        });
    }

}

