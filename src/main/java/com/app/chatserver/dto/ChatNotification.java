package com.app.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification {
    private Integer chatId;
    private Integer messageId;
    private String senderName;
    private boolean hasMedia;
    private Date dateSend;
}
