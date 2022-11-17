package com.app.chatserver.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@Slf4j
public class FileServiceImpl implements FileService{
    @Value("${hay.pathToSave}")
    private String pathToSaveAllFiles;

    private boolean checkExistsUserFolder(String userName){
        Path pathToUser = Paths.get(pathToSaveAllFiles+"/"+userName);
        return Files.exists(pathToUser);
    }

    @Override
    public Path createDirectories(String userName) throws IOException {
        if (!checkExistsUserFolder(userName)) {
            Files.createDirectory(Paths.get(pathToSaveAllFiles+"/" +userName));
            Files.createDirectory(Paths.get(pathToSaveAllFiles+"/" +userName+"/audio"));
            Files.createDirectory(Paths.get(pathToSaveAllFiles+"/" +userName+"/img"));
        }
        return Paths.get(pathToSaveAllFiles+"/" +userName);
    }

    @Override
    public Path getPathToUserImages(String userName) {
        return Paths.get(pathToSaveAllFiles+"/" +userName+"/img/");
    }

    @Override
    public Path getPathToUserAudio(String userName) {
        return Paths.get(pathToSaveAllFiles+"/" +userName+"/audio/");
    }

    @Override
    public Path saveImage(BufferedImage image, String ImageName, Path pathToUserImg) throws IOException {
        ImageIO.write(
                image,
                "jpg",
                new File(pathToUserImg +"/"+ ImageName + ".jpg"));
        return Paths.get(pathToUserImg +"/"+ ImageName + ".jpg");
    }

    @Override
    public Path saveAudio(BufferedImage audio, String audioName, Path pathToUserAudio) throws IOException {
        ImageIO.write(
                audio,
                "jpg",
                new File(pathToUserAudio +"/"+ audioName + ".wav"));
        return Paths.get(pathToUserAudio +"/"+ audioName + ".wav");
    }

    @Override
    public Integer getCountOfImages(String userName) {
        //TODO check null
        log.info(pathToSaveAllFiles+userName+"/img/");
        return new File(pathToSaveAllFiles+userName+"/img/").listFiles().length;
    }

    @Override
    public Integer getCountOfAudio(String userName) {
        //TODO check null
        return new File(pathToSaveAllFiles+userName+"/audio/").listFiles().length;
    }

    @Override
    public String imageToBase64(String path) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(path));
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
