package com.ecommerce.enums;

public enum HttpResponseType {
    BAD_REQUEST(400), OK(200), CREATED(201), INTERNAL_SERVER_ERROR(500);
    private final int code;
    HttpResponseType(int code) {
        this.code = code;
    }
    public int getStatusCode() {
        return this.code;
    }
}
