package com.dispatch.system.exception;

/**
 * 费用标准异常
 * 用于费用标准相关操作的异常处理
 */
public class FeeStandardException extends RuntimeException {

    public FeeStandardException(String message) {
        super(message);
    }

    public FeeStandardException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeeStandardException(Throwable cause) {
        super(cause);
    }
}
