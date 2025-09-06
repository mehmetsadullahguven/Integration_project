package com.mehmetsadullahguven.enums;

public enum StatusType {
    INSTOCK(1),
    OUTOFSTOCK(0),
    NOTAVAILABLE(2);

    private int status;

    StatusType(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
