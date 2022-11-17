package com.app.chatserver.services;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public interface FileService {
    /**
     * Create user folders
     *
     * @param  userName string username to create directories in local storage
     * @return Path to username
     */
    Path createDirectories(String userName) throws IOException;
    /**
     * Get path to user image files
     *
     * @param  userName string username to create directories in local storage
     * @return Path to username
     */
    Path getPathToUserImages(String userName);

    /**
     * Get path to user audio files
     *
     * @param  userName string username to create directories in local storage
     * @return Path to username
     */
    Path getPathToUserAudio(String userName);

    /**
     * Save image in user directory
     *
     * @param  image need to be saved
     * @param  ImageName name of image
     * @param  pathToUserImage path to save image
     * @return Path to saved image
     */
    Path saveImage(BufferedImage image, String ImageName, Path pathToUserImage) throws IOException;

    /**
     * Save audio in user directory
     *
     * @param  audio need to be saved
     * @param  audioName name of image
     * @param  pathToUserAudio path to save image
     * @return Path to saved image
     */
    Path saveAudio(BufferedImage audio, String audioName, Path pathToUserAudio) throws IOException;

    /**
     * Get count of images in user folder
     *
     * @param  userName username for which we calculate the number of images in its folder
     * @return Path to saved image
     */
    Integer getCountOfImages(String userName);

    /**
     * Get count of audio in user folder
     *
     * @param  userName username for which we calculate the number of images in its folder
     * @return Path to saved image
     */
    Integer getCountOfAudio(String userName);

    /**
     * Convert image to base64 format
     *
     * @param  path path to image
     * @return String base64 image
     */
    String imageToBase64(String path) throws IOException;
}
