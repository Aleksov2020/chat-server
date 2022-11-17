package com.app.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseChatRoom {
    private Integer id;
    private String chatName;
    private ResponseMedia responseMedia;
    private Date dateLastUpdate;
    private List<ResponseParticipants> participants = new ArrayList<>();
}
