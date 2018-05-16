package com.zhangyf.bidirectio.net.exception;

/**
 * Created on 2018-03-09 09:33.
 *
 * @author zyf
 */
public class ErrorException extends Exception {
    public int type;
    public String msg;
    public int code;


    public ErrorException(Throwable throwable, int type) {
        super(throwable);
        this.type = type;
    }

}
