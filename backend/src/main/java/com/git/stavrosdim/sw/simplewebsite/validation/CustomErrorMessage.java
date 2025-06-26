package com.git.stavrosdim.sw.simplewebsite.validation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomErrorMessage {

    private String message;
    private List<String> errors;

    public CustomErrorMessage(String message, String error) {
        this.message = message;
        this.errors = List.of(error);
    }

    @Getter
    @AllArgsConstructor
    public enum ErrorMessageType {

        INVALID_REQUEST("Invalid Request"),
        INVALID_PARAMETERS("Invalid Parameters"),
        EMPTY_BODY("Request body is missing"),
        INCOMPATIBLE_DATE_FORMAT("Birthdate must be in 'yyyy-mm-dd' format"),
        INVALID_METHOD("Method not allowed"),
        UNEXPECTED_ERROR("Unexpected Error"),
        RESOURCE_NOT_FOUND("Resource not found"),
        USER_NOT_FOUND("User not found");

        private final String message;
    }
}
