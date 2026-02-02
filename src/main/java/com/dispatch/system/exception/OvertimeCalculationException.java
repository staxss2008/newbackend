package com.dispatch.system.exception;

/**
 * 加班计算异常
 * 用于加班费计算过程中的异常处理
 */
public class OvertimeCalculationException extends RuntimeException {

    public OvertimeCalculationException(String message) {
        super(message);
    }

    public OvertimeCalculationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OvertimeCalculationException(Throwable cause) {
        super(cause);
    }
}
