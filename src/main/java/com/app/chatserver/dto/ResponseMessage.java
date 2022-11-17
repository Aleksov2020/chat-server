package com.app.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private Integer id;
    private String senderName;
    private Integer chatId;
    private ResponseContent content;
    private ResponseMedia media;
    private Integer forwardId;
    private Date dateCreate;
}
