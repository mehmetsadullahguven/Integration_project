package com.mehmetsadullahguven.exception;

public class BaseException extends RuntimeException {

    public BaseException(ErrorMessage errorMessage) {
        super(errorMessage.perpareErrorMessage());
    }
}