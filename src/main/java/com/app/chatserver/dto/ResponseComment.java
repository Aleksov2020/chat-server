package com.app.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseComment {
    private Integer id;
    private String ownerName;
    private String ownerAvatar;
    private ResponseContent content;
    private Integer forwardId; // future
    private Long likeCount;
    private Date dateCreate;
    private Date dateLastUpdate;
    private Boolean isLiked;
}
