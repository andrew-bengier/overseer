package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerException;
import com.bnfd.overseer.model.api.info.LogFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class LogService {
    // region - Class Variables -
    @Value("${spring.log.file.location}")
    private String logLocation;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public LogService() {
    }
    // endregion - Constructors -

    public List<LogFile> getAllLogFiles() {
        List<LogFile> files = new ArrayList<>();
        try {
            File logFolder = ResourceUtils.getFile("file:" + logLocation);
            log.info(logFolder.getPath());

            FileUtils.listFiles(logFolder, null, true).forEach(file -> {
                files.add(new LogFile(file.getName(), new Date(file.lastModified()).toInstant(), file.getPath()));
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return files;
    }

    public byte[] getLogFileContent(String fileName) {
        try {
            File logFile = ResourceUtils.getFile("file:" + fileName);
            log.info(logFile.getPath());

            InputStreamResource resource = new InputStreamResource(new FileInputStream(logFile));
            return IOUtils.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new OverseerException(e.getMessage());
        }
    }
}
