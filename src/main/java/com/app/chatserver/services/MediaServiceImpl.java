package com.app.chatserver.services;

import com.app.chatserver.dto.ResponseMedia;
import com.app.chatserver.dto.ResponseMediaList;
import com.app.chatserver.enums.MediaType;
import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.MediaException;
import com.app.chatserver.models.Content;
import com.app.chatserver.models.Media;
import com.app.chatserver.repo.MediaRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MediaServiceImpl implements MediaService{
    private final MediaRepository mediaRepository;
    private final FileService fileService;

    public MediaServiceImpl(MediaRepository mediaRepository,
                            FileService fileService){
        this.mediaRepository = mediaRepository;
        this.fileService = fileService;
    }

    @Override
    public Media createAndSaveMedia(String name, MediaType mediaType, Long size, String path) {
        Media m = new Media();
        m.setName(name);
        m.setType(mediaType);
        m.setSize(size);
        m.setPath(path);
        return mediaRepository.save(m);
    }

    @Override
    public ResponseMedia serializeMediaToSend(Media media) throws IOException {
        return new ResponseMedia(
            fileService.imageToBase64(media.getPath())
        );
    }

    @Override
    public ResponseMediaList serializeMediaToSendInList(List<Media> mediaList) throws IOException {
        ResponseMediaList responseMediaList = new ResponseMediaList();
        for (Media media : mediaList) {
            responseMediaList.getResponseMediaList().add(serializeMediaToSend(media));
        }
        return responseMediaList;
    }

    //TODO rewrite
    @Override
    public Media serializeMediaFromBase64AndSave(String mediaBase64, String userName) throws IOException {
        String base64Image = mediaBase64.split(",")[0];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

        Media media = new Media();
        media.setPath(
                String.valueOf(
                        fileService.saveImage(
                            img,
                            String.valueOf(fileService.getCountOfImages(userName) + 1),
                            fileService.getPathToUserImages(userName)
                        )
                )
        );
        media.setType(MediaType.image);
        //TODO get size
        media.setSize(0L);
        media.setName(String.valueOf(fileService.getCountOfImages(userName) + 1));

        return mediaRepository.save(media);
    }

    @Override
    public Media findMediaById(Integer mediaId) {
        return mediaRepository.findById(mediaId).orElseThrow(
                () -> new MediaException(ServerChatErrors.MEDIA_NOT_FOUND.name())
        );
    }

    @Override
    public Media serializeAudioFromBase64AndSave(String audioBase64, String userName) throws IOException {
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(audioBase64);
        BufferedImage audio = ImageIO.read(new ByteArrayInputStream(imageBytes));

        Media m = new Media();
        m.setPath(String.valueOf(
                fileService.saveAudio(
                        audio,
                        String.valueOf(fileService.getCountOfAudio(userName) + 1),
                        fileService.getPathToUserAudio(userName)
                )
        ));
        m.setName(String.valueOf(fileService.getCountOfAudio(userName) + 1));
        m.setSize(0l); //TODO get size
        m.setType(MediaType.audio);

        return mediaRepository.save(m);
    }
}
