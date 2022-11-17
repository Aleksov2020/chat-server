package com.app.chatserver.exceptions;

public class BlackListException extends RuntimeException{
    public BlackListException(String message) {
        super(message);
    }
}
