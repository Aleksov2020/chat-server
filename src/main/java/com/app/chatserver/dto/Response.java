package com.app.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class Response {
    private String serverMessage;
}
