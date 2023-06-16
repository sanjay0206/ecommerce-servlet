package com.ecommerce.utilities;

public class ResponseDto {
    private String message;
    private int code;

    public ResponseDto() {
    }

    public ResponseDto(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
