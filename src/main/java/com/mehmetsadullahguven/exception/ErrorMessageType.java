package com.mehmetsadullahguven.exception;

import lombok.Getter;

@Getter
public enum ErrorMessageType {
    NO_RECORD_EXIST("1004", "kullanıcı bulunamadı"),
    TOKEN_ERROR("1005", "Token error: "),
    GENERAL_EXIST("9999", "General error: "),
    USERNAME_EXIST("1006", "Username exist: ");


    private String errorCode;
    private String errorMessage;

    ErrorMessageType(String errorCode, String errorMessage)
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
