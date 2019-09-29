package com.optimaize.test.task.responses;

public class BaseResponse {
    private final int code;

    private final String message;

    public BaseResponse(final Responses response) {
        this.code = response.getCode();
        this.message = response.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
