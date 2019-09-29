package com.optimaize.test.task.responses;

public class ResultResponse extends BaseResponse {
    private final double result;

    public ResultResponse(Responses response, double result) {
        super(response);
        this.result = result;
    }

    public double getResult() {
        return result;
    }
}
