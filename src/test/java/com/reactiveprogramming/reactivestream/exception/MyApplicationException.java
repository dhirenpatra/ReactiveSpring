package com.reactiveprogramming.reactivestream.exception;

public class MyApplicationException extends Throwable {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public MyApplicationException(Throwable exception) {
        this.message = exception.getMessage();
    }

    @Override
    public String toString() {
        return "MyApplicationException{" +
                "message='" + message + '\'' +
                '}';
    }
}
