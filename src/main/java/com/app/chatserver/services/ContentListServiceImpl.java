package com.app.chatserver.services;

import com.app.chatserver.models.Content;
import com.app.chatserver.models.ContentList;
import com.app.chatserver.models.Media;
import com.app.chatserver.repo.ContentListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ContentListServiceImpl implements ContentListService {
    private final ContentListRepository contentListRepository;

    public ContentListServiceImpl(ContentListRepository contentListRepository){
        this.contentListRepository = contentListRepository;
    }
    @Override
    public List<Media> findMediaListByContent_Id(Integer contentId) {
        List<Media> mediaList = new ArrayList<>();
        for (ContentList contentL : contentListRepository.findContentListByContent_Id(contentId)) {
            mediaList.add(contentL.getMedia());
        }
        return mediaList;
    }

    @Override
    public ContentList create(Media media, Content content) {
        ContentList contentList = new ContentList();
        contentList.setContent(content);
        contentList.setMedia(media);
        return contentListRepository.save(contentList);
    }
}
