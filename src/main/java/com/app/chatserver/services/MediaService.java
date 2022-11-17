package com.app.chatserver.services;

import com.app.chatserver.dto.ResponseMedia;
import com.app.chatserver.dto.ResponseMediaList;
import com.app.chatserver.enums.MediaType;
import com.app.chatserver.models.Content;
import com.app.chatserver.models.Media;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MediaService {
    /**
     * create media entity and save in local storage
     *
     * @param  name String media name
     * @param  mediaType Media type
     * @param  size Long media size
     * @param  path String path to save media
     * @return Media
     */
    Media createAndSaveMedia(String name, MediaType mediaType, Long size, String path);
    /**
     * serialize media to send to user
     *
     * @param  media media entity to serialize in response
     * @return ResponseMedia
     */
    ResponseMedia serializeMediaToSend(Media media) throws IOException;
    /**
     * serialize list of media to send to user
     *
     * @param  mediaList list of media to serialize
     * @return ResponseMediaList
     */
    ResponseMediaList serializeMediaToSendInList(List<Media> mediaList) throws IOException;
    /**
     * serialize list of media to send to user
     *
     * @param  mediaBase64 media in base64 to serialize
     * @param  userName username who send media
     * @return Media
     */
    Media serializeMediaFromBase64AndSave(String mediaBase64, String userName) throws IOException;

    Media findMediaById(Integer mediaId);

    Media serializeAudioFromBase64AndSave(String audioBase64, String userName) throws IOException;
}
