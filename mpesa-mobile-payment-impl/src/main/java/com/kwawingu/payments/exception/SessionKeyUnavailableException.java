package com.kwawingu.payments.exception;

public class SessionKeyUnavailableException extends Exception {

    public SessionKeyUnavailableException(String msg) {
        super(msg);
    }

    public SessionKeyUnavailableException(Throwable t) {
        super(t);
    }
}
