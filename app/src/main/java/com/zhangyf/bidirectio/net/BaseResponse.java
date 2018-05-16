package com.zhangyf.bidirectio.net;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
@Keep
public class BaseResponse<T> {

    public static final int CODE_SUCCESS = 0;

    public String msg;
    public int code;
    @SerializedName("data")
    public T data;

    public static class ErrorResponse {
        public String msg;
        public Integer bizCode = CODE_SUCCESS;
    }

}
