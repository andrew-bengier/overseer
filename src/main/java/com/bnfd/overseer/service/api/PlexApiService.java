package com.bnfd.overseer.service.api;

import com.bnfd.overseer.model.media.plex.*;
import com.bnfd.overseer.model.persistence.libraries.*;
import com.bnfd.overseer.model.persistence.servers.*;
import jakarta.xml.bind.*;
import lombok.extern.slf4j.*;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static com.bnfd.overseer.utils.Constants.*;

@Slf4j
@Service
public class PlexApiService implements ApiService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public PlexApiService(@Qualifier("overseer-mapper") ModelMapper overseerMapper) {
        this.overseerMapper = overseerMapper;
    }
    // endregion - Constructors -

    @Override
    public List<LibraryEntity> getLibraries(ServerEntity server) {
        String url = server.getApiKey().getUrl() + PLEX_LIBRARIES_URL;

        try {
            MediaContainer mediaContainer = getFromPlex(url, server.getApiKey().getKey());

            return overseerMapper.map(mediaContainer.getDirectories(), new TypeToken<List<LibraryEntity>>() {
            }.getType());
        } catch (IOException | JAXBException exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    // region - Protected Methods -
    public MediaContainer getFromPlex(String apiUrl, String token) throws IOException, JAXBException {
        String requestUrl = apiUrl + PLEX_TOKEN_PARAM + token;

        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        configureConnection(connection, "GET");
        connection.connect();

        if (connection.getResponseCode() != HttpStatus.OK.value()) {
            throw new RuntimeException("Error accessing plex api - " + connection.getResponseMessage());
        }

        StringBuilder responseText = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());
        while (scanner.hasNext()) {
            responseText.append(scanner.nextLine());
        }

        scanner.close();
        connection.disconnect();

        JAXBContext jaxbContext = JAXBContext.newInstance(MediaContainer.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return (MediaContainer) jaxbUnmarshaller.unmarshal(new StringReader(responseText.toString()));
    }
    // endregion - Protected Methods -

    // region - Private Methods -
    public void configureConnection(HttpURLConnection connection, String requestMethod) throws ProtocolException {
        connection.setRequestMethod(requestMethod.toUpperCase());
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Language", "en-US");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
    }
    // endregion - Private Methods -
}
