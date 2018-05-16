package com.zhangyf.bidirectio.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Created on 2018-03-09 09:33.
 * 异常管理
 *
 * @author zyf
 */
public class ExceptionEngine {
    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ErrorException handleException(Throwable e) {
        ErrorException ex;
        if (e instanceof HttpException) {
            // HTTP错误
            HttpException httpException = (HttpException) e;
            ex = new ErrorException(e, YzError.HTTP_ERROR);
            ex.code = httpException.code();
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.msg = YzError.MSG_HTTP_ERROR;
                    break;
            }
            return ex;
        } else if (e instanceof BusinessException) {
            // 服务器返回的错误
            BusinessException businessException = (BusinessException) e;
            ex = new ErrorException(businessException, YzError.SERVICE_ERROR);
            ex.code = businessException.code;
            ex.msg = businessException.msg;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {// 解析错误
            ex = new ErrorException(e, YzError.PARSE_ERROR);
            ex.code = YzError.PARSE_ERROR;
            ex.msg = YzError.MSG_PARSE_ERROR;
            return ex;
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            // 网络链接错误
            ex = new ErrorException(e, YzError.NETWORK_ERROR);
            ex.code = YzError.NETWORK_ERROR;
            ex.msg = YzError.MSG_NETWORK_ERROR;
            return ex;
        } else if (e instanceof AppException) {
            AppException appException = (AppException) e;
            ex = new ErrorException(e, YzError.APP_ERROR);
            ex.code = YzError.APP_ERROR;
            ex.msg = appException.msg;
            return ex;
        } else {// 未知错误
            ex = new ErrorException(e, YzError.UNKNOWN);
            ex.code = YzError.UNKNOWN;
            ex.msg = YzError.MSG_UNKNOWN;
            return ex;
        }
    }

}
