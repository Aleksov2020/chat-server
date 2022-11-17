package com.app.chatserver.services;

import com.app.chatserver.dto.ResponseContent;
import com.app.chatserver.models.Content;
import com.app.chatserver.repo.ContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ContentServiceImpl implements ContentService{
    private final ContentRepository contentRepository;

    public ContentServiceImpl(ContentRepository contentRepository){
        this.contentRepository = contentRepository;
    }

    @Override
    public Content create(String content) {
        Content c = new Content();
        c.setContent(content);
        return contentRepository.save(c);
    }

    @Override
    public ResponseContent serializeContentToSend(Content content) {
        return new ResponseContent(
                content.getContent()
        );
    }
}
