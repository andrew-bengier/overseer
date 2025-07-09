package com.bnfd.overseer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class FileArchiveService {
    // region - Class Variables -
    @Value("${spring.application.assetDirectory:/overseer/assets/}")
    private String assetDirectory;

    private final ResourceLoader resourceLoader;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public FileArchiveService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    // endregion - Constructors -

    public void archiveMedia() throws IOException {
//        Resource videoResource = resourceLoader.getResource("file:" + assetDirectory + "archive-video.mp4");
        Resource videoResource = resourceLoader.getResource("file:" + assetDirectory + "archive-video.mp4");
        String videoPath = videoResource.getFile().getPath();

        log.info("Test Video: " + videoPath);
    }
}
