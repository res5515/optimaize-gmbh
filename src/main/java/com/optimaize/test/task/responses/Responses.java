package com.optimaize.test.task.responses;

/**
 * Contains response codes and messages.
 */
public enum Responses {

    // Common responses
    OK(1, "The request succeeded"),
    NOT_NUMBER(100, "You should save only the numbers"),
    SERVER_IS_TOO_BUSY(101, "Server is too busy. Please try again later"),
    NO_ANY_INPUT_VALUES(102, "You did not save any numbers. Could not calculate the result. Please save numbers first");

    private final int code;

    private final String message;

    Responses(final int code,
                      final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
