package com.git.stavrosdim.sw.simplewebsite.validation;

import com.git.stavrosdim.sw.simplewebsite.validation.CustomErrorMessage.ErrorMessageType;

public class CustomExceptions {

    public static class EmptyRequestBodyException extends RuntimeException {
        public EmptyRequestBodyException() {
            super(ErrorMessageType.EMPTY_BODY.getMessage());
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(Long id) {
            super("User with id "+id+" not found");
        }
    }
}
