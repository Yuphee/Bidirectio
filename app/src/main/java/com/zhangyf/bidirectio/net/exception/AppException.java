package com.zhangyf.bidirectio.net.exception;

/**
 * Created on 2018-03-09 09:33.
 * App异常
 *
 * @author zyf
 */
public class AppException extends RuntimeException {
    public int code;
    public String msg;

    public AppException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
