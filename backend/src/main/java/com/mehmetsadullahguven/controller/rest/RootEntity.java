package com.mehmetsadullahguven.controller.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RootEntity <T>{

    private T payload;
    private Integer status;
    private String errorMessage;
    private String message;

    public static <T> RootEntity<T> ok(T payload, String message)
    {
        RootEntity<T> rootEntity = new RootEntity<>();
        rootEntity.setPayload(payload);
        rootEntity.setStatus(200);
        rootEntity.setErrorMessage(null);
        rootEntity.setMessage(message);
        return rootEntity;
    }

    public static <T> RootEntity<T> error(String errorMessage)
    {
        RootEntity<T> rootEntity = new RootEntity<>();
        rootEntity.setPayload(null);
        rootEntity.setStatus(500);
        rootEntity.setErrorMessage(errorMessage);
        return rootEntity;
    }
}
