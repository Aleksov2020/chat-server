package com.app.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseParticipants {
    private String userName;
    private String image;
}
