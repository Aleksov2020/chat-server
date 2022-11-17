package com.app.chatserver.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class InputMessage {
    private String media;
    private String mediaType;
    private String recipientName;
    private Integer chatId;
    private String content;
    private Integer forwardId;
}
