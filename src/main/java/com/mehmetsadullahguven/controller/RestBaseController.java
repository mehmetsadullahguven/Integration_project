package com.mehmetsadullahguven.controller;

public class RestBaseController {

    public <T> RootEntity<T> ok (T payload, String message) {
        return RootEntity.ok(payload, message);
    }

    public <T> RootEntity<T> error (String errorMessage) {
        return RootEntity.error(errorMessage);
    }
}
