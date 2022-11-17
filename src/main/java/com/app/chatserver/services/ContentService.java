package com.app.chatserver.services;

import com.app.chatserver.dto.ResponseContent;
import com.app.chatserver.models.Content;

public interface ContentService {
    /**
     * Create and save content entity.
     *
     * @param  content string content
     * @return saved Content entity
     */
    Content create(String content);
    /**
     * Serialize content to send to user
     *
     * @param  content string content
     * @return saved Content entity
     */
    ResponseContent serializeContentToSend(Content content);
}
