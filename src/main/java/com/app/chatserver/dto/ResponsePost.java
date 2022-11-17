package com.app.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
@AllArgsConstructor
@Data
public class ResponsePost {
    private Integer postId;
    private String ownerName;
    private String ownerAvatar;
    private ResponseContent responseContent;
    private ResponseMediaList responseMediaList;
    private Integer likeCount;
    private Integer commentCounter;
    private boolean isLiked;
    private Date dateCreate;
}
