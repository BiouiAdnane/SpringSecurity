package org.example.securiteback.Enitities;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String message;
    private final int statusCode;

    public ErrorResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}