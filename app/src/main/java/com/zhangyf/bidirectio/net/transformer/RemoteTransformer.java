package com.zhangyf.bidirectio.net.transformer;

import android.content.Context;


import com.zhangyf.bidirectio.net.BaseResponse;
import com.zhangyf.bidirectio.net.Optional;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
public class RemoteTransformer<Z>
        implements ObservableTransformer<BaseResponse<Z>, Optional<Z>> {

    private Context mContext;

    public RemoteTransformer(final Context context) {
        mContext = context;
    }

    @Override
    public Observable<Optional<Z>> apply(@NonNull Observable<BaseResponse<Z>> upstream) {
        return upstream
                .compose(new SchedulerTransformer<BaseResponse<Z>>())
                .compose(new ErrorCheckerTransformer<Z>(mContext));
    }


}
