package com.bnfd.overseer.service.api.media.server;

import com.bnfd.overseer.model.api.Media;
import com.bnfd.overseer.model.constants.MediaIdType;
import com.bnfd.overseer.model.media.plex.MediaContainer;
import com.bnfd.overseer.model.media.plex.Video;
import com.bnfd.overseer.model.persistence.ApiKeyEntity;
import com.bnfd.overseer.model.persistence.CollectionEntity;
import com.bnfd.overseer.model.persistence.LibraryEntity;
import com.bnfd.overseer.model.persistence.ServerEntity;
import com.bnfd.overseer.utils.HttpUtils;
import com.bnfd.overseer.utils.PlexUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.*;
import java.util.*;

import static com.bnfd.overseer.utils.Constants.*;

@Slf4j
@Service
public class PlexMediaServerApiService implements MediaServerApiService {
    // region - Class Variables -
    private final ModelMapper overseerMapper;
    // endregion - Class Variables -

    // region - Constructors -
    @Autowired
    public PlexMediaServerApiService(@Qualifier("overseer-mapper") ModelMapper overseerMapper) {
        this.overseerMapper = overseerMapper;
    }
    // endregion - Constructors -

    // region - Libraries -
    @Override
    public List<LibraryEntity> getLibraries(ServerEntity server) {
        StringBuilder urlRequest = new StringBuilder();
        urlRequest.append(server.getApiKey().getUrl())
                .append(PLEX_LIBRARIES_URL);

        try {
            MediaContainer mediaContainer = plexConnection(urlRequest.toString(), server.getApiKey().getKey(), Collections.emptyMap(), Collections.emptyList(), HttpMethod.GET);

            return overseerMapper.map(mediaContainer.getDirectories(), new TypeToken<List<LibraryEntity>>() {
            }.getType());
        } catch (IOException | JAXBException | URISyntaxException exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }
    // endregion - Libraries -

    // region - Collections -
    // TODO: reference here to get collections and tell if tracked or not - by ratingKey = externalId
    public List<CollectionEntity> getCollections(ApiKeyEntity apiKey, String libraryId, boolean includeMedia) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(apiKey.getUrl())
                .append(PLEX_LIBRARY_COLLECTIONS_URL.replace("{referenceId}", libraryId));

        try {
            MediaContainer mediaContainer = plexConnection(requestUrl.toString(), apiKey.getKey(), Collections.emptyMap(), Collections.emptyList(), HttpMethod.GET);

            // TODO: directory to collection, then get videos for each and convert to media (builder custom if not tracked)

            if (CollectionUtils.isEmpty(mediaContainer.getDirectories())) {
                return Collections.emptyList();
            } else {
                if (includeMedia) {
                    List<CollectionEntity> collections = new ArrayList<>();
                    // Temp: limit 10 collections here
                    // TODO: add pagination
                    for (int d = 0; d < mediaContainer.getDirectories().size() && d < 10; d++) {
//                    for (Directory directory : mediaContainer.getDirectories()) {
                        collections.add(getCollection(apiKey, libraryId, mediaContainer.getDirectories().get(d).getRatingKey(), true));
                    }

                    return collections;
                } else {
                    return overseerMapper.map(mediaContainer.getDirectories(), new TypeToken<List<CollectionEntity>>() {
                    }.getType());
                }
            }
        } catch (IOException | JAXBException | URISyntaxException exception) {
            log.error(exception.getMessage());
            return Collections.emptyList();
        }
    }

    public CollectionEntity getCollection(ApiKeyEntity apiKey, String libraryId, String collectionId, boolean includeMedia) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(apiKey.getUrl())
                .append(PLEX_COLLECTIONS_URL.replace("{referenceId}", collectionId));

        try {
            MediaContainer mediaContainer = plexConnection(requestUrl.toString(), apiKey.getKey(), Collections.emptyMap(), Collections.emptyList(), HttpMethod.GET);

            // TODO: directory to collection, then get videos for each and convert to media (builder custom if not tracked)
            if (mediaContainer != null && mediaContainer.getDirectories() != null) {
                CollectionEntity collection = overseerMapper.map(mediaContainer.getDirectories().getFirst(), new TypeToken<CollectionEntity>() {
                }.getType());

                if (includeMedia) {
                    List<Media> media = getMediaFromCollection(apiKey, collection);
//                    media.forEach(item -> {
//                        Optional<Metadata> thumb = item.getMetadata().stream().filter(metadata -> metadata.getName().equalsIgnoreCase(MediaImageType.THUMB.name())).findFirst();
//                        if (thumb.isPresent()) {
//                            try {
//                                byte[] poster = plexConnectionForImages(apiKey.getUrl() + thumb.get().getValue(), apiKey.getKey(), HttpMethod.GET);
//                                item.addMetadata(new Metadata(null, "poster", Base64.getEncoder().encodeToString(poster)));
//                            } catch (IOException | JAXBException | URISyntaxException exception) {
//                                // TODO: proper error handling here
//                                throw new RuntimeException("Error accessing plex api image - " + exception.getLocalizedMessage());
//                            }
//                        }
//                    });

                    collection.setMedia(media);
                }

                return collection;
            } else {
                return null;
            }
        } catch (IOException | JAXBException | URISyntaxException exception) {
            // TODO: proper error handling here
            log.error(exception.getMessage());
            return null;
        }
    }

    // NOTE: mediaIds are plex specific ids (ratingKey)
    public void createOrUpdateCollection(ApiKeyEntity apiKey, CollectionEntity collection, List<String> mediaIds) {
        // TODO: error and exception handling
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(apiKey.getUrl())
                .append(PLEX_LIBRARY_URL.replace("{referenceId}", collection.getLibraryId()))
                .append(PLEX_ALL_URL);

        try {
            plexConnection(
                    requestUrl.toString(),
                    apiKey.getKey(),
                    PlexUtils.getCollectionItemsParams(mediaIds, collection),
                    Collections.emptyList(),
                    HttpMethod.PUT
            );
        } catch (IOException | JAXBException | URISyntaxException exception) {
            log.error(exception.getMessage());
        }
    }

    public void removeCollection(ApiKeyEntity apiKey, CollectionEntity collection, List<String> mediaIds) {
        // TODO: error and exception handling
        // NOTE: if mediaIds is empty will remove the collection completely, otherwise remove media from collection
        try {
            if (CollectionUtils.isNotEmpty(mediaIds)) {
                StringBuilder requestUrl = new StringBuilder();
                requestUrl.append(apiKey.getUrl())
                        .append(PLEX_LIBRARY_URL.replace("{referenceId}", collection.getLibraryId()))
                        .append(PLEX_ALL_URL);

                plexConnection(
                        requestUrl.toString(),
                        apiKey.getKey(),
                        PlexUtils.getCollectionParams(mediaIds, collection),
                        List.of("collection[0].tag.tag"),
                        HttpMethod.PUT
                );
            } else {
                StringBuilder requestUrl = new StringBuilder();
                requestUrl.append(apiKey.getUrl())
                        .append(PLEX_COLLECTIONS_URL.replace("{referenceId}", collection.getExternalId()));

                plexConnection(
                        requestUrl.toString(),
                        apiKey.getKey(),
                        Collections.emptyMap(),
                        Collections.emptyList(),
                        HttpMethod.DELETE
                );
            }
        } catch (IOException | JAXBException | URISyntaxException exception) {
            log.error(exception.getMessage());
        }
    }

    public void updateCollectionMetadata(ApiKeyEntity apiKey, CollectionEntity collection) {
        // TODO: fill in here with collection.getMetadata()...
    }
    // endregion - Collections -

    // region - Media -
    @Override
    public List<Media> getMedia(ApiKeyEntity apiKey, String libraryId, Map<MediaIdType, List<String>> mediaIds) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(apiKey.getUrl())
                .append(PLEX_LIBRARY_URL.replace("{referenceId}", libraryId))
                .append(PLEX_ALL_URL);
        Map<String, String> params = Map.of("includeGuids", "1");

        List<Media> media = new ArrayList<>();
        try {
            MediaContainer results = plexConnection(requestUrl.toString(), apiKey.getKey(), params, Collections.emptyList(), HttpMethod.GET);

            for (Map.Entry<MediaIdType, List<String>> entry : mediaIds.entrySet()) {
                for (String id : entry.getValue()) {
                    String guid = entry.getKey().prefix + id;
                    Optional<Video> filtered = results.getVideos().stream()
                            .filter(video -> PlexUtils.guidsContainFilter(video.getGuids(), guid))
                            .findFirst();

                    filtered.ifPresent(video -> media.add(overseerMapper.map(video, Media.class)));
                }
            }

            return media;
        } catch (IOException | JAXBException | URISyntaxException exception) {
            // TODO: proper error handling here
            log.error(exception.getMessage());
            return null;
        }
    }

    public List<Media> getMediaFromCollection(ApiKeyEntity apiKey, CollectionEntity collection) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(apiKey.getUrl())
                .append(PLEX_COLLECTIONS_URL.replace("{referenceId}", collection.getExternalId()))
                .append(PLEX_INCLUDE_CHILDREN_URL);

        try {
            MediaContainer mediaContainer = plexConnection(requestUrl.toString(), apiKey.getKey(), Collections.emptyMap(), Collections.emptyList(), HttpMethod.GET);

            // TODO: directory to collection, then get videos for each and convert to media (builder custom if not tracked)
            if (mediaContainer != null && mediaContainer.getMetadata() != null) {
                return overseerMapper.map(mediaContainer.getMetadata(), new TypeToken<List<Media>>() {
                }.getType());
            } else if (mediaContainer != null && mediaContainer.getVideos() != null) {
                return overseerMapper.map(mediaContainer.getVideos(), new TypeToken<List<Media>>() {
                }.getType());
            } else {
                return null;
            }
        } catch (IOException | JAXBException | URISyntaxException exception) {
            // TODO: proper error handling here
            log.error(exception.getMessage());
            return null;
        }
    }
    // endregion - Media -

    // region - Protected Methods -
    // TODO: update this to use RestTemplate
    public MediaContainer plexConnection(String apiUrl, String token, Map<String, String> params, List<String> removalParams, HttpMethod httpMethod) throws IOException, JAXBException, URISyntaxException {
        StringBuilder request = new StringBuilder();
        request.append(apiUrl)
                .append(PLEX_TOKEN_PARAM)
                .append(token);

        String requestUrl = HttpUtils.generateUrlWithParams(request, params, removalParams).toString();

        URL url = new URI(requestUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        configureConnection(connection, httpMethod.name());
        connection.connect();

        if (connection.getResponseCode() != HttpStatus.OK.value()) {
            // TODO: proper error handling here
            throw new RuntimeException("Error accessing plex api - " + connection.getResponseMessage());
        }

        if (httpMethod == HttpMethod.GET) {
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
        } else {
            return null;
        }
    }

    public byte[] plexConnectionForImages(String apiUrl, String token, HttpMethod httpMethod) throws IOException, JAXBException, URISyntaxException {
        StringBuilder request = new StringBuilder();
        request.append(apiUrl)
                .append(PLEX_TOKEN_PARAM)
                .append(token);

        String requestUrl = HttpUtils.generateUrlWithParams(request, Collections.emptyMap(), Collections.emptyList()).toString();

        URL url = new URI(requestUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        configureConnection(connection, httpMethod.name());
        connection.connect();

        if (connection.getResponseCode() != HttpStatus.OK.value()) {
            // TODO: proper error handling here
            throw new RuntimeException("Error accessing plex api - " + connection.getResponseMessage());
        }

        if (httpMethod == HttpMethod.GET) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (InputStream inputStream = url.openStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }

                return byteArrayOutputStream.toByteArray();
            } catch (IOException exception) {
                // TODO: proper error handling here
                throw new RuntimeException("Error accessing plex api - " + connection.getResponseMessage());
            }
        } else {
            return null;
        }
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
