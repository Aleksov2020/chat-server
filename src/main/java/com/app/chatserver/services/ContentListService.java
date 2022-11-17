package com.app.chatserver.services;

import com.app.chatserver.models.Content;
import com.app.chatserver.models.ContentList;
import com.app.chatserver.models.Media;

import java.util.List;

public interface ContentListService {
    /**
     * Create and save chat list entity. This entity linked content and media
     *
     * @param  media media of content
     * @param  content content
     * @return saved Content List entity
     */
    ContentList create(Media media, Content content);
    /**
     * Find all media, linked to content
     *
     * @param  contentId content id
     * @return list of media, linked to content
     */
    List<Media> findMediaListByContent_Id(Integer contentId);
}
