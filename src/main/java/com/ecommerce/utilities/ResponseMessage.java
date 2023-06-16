package com.ecommerce.utilities;

import com.ecommerce.enums.Status;

public class ResponseMessage {
    private Status status;
    private String message;

    public ResponseMessage() {
    }

    public ResponseMessage(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseMessage{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
