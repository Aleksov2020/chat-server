package com.app.chatserver.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class InputPost {
    private String content;
    private List<String> imageList;
}
