package com.app.chatserver.controllers;

import com.app.chatserver.dto.ResponseException;
import com.app.chatserver.exceptions.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class AdviceController {
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ResponseException> handleRegistrationException(TokenException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "Token error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ResponseException> handleMessageException(MessageException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "Message error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ChatRoomException.class)
    public ResponseEntity<ResponseException> handleChatRoomException(ChatRoomException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "ChatRoom error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ResponseException> handleUserException(UserException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "User error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(SubscribeException.class)
    public ResponseEntity<ResponseException> handleSubscribeException(SubscribeException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "Subscribe error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(CommentsException.class)
    public ResponseEntity<ResponseException> handleCommentsException(CommentsException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "Comments error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MediaException.class)
    public ResponseEntity<ResponseException> handleMediaException(MediaException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "Media error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BlackListException.class)
    public ResponseEntity<ResponseException> handleBlackListException(BlackListException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(
                new ResponseException(
                        e.getMessage(),
                        "BlackList error."
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
