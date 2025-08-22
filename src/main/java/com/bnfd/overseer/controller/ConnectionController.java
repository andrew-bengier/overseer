package com.bnfd.overseer.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@Tag(name = "ApiKey Endpoints")
@RequestMapping("api/connections")
public class ConnectionController {

    @GetMapping(value = "/plex", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> getPlexConnection(@RequestParam String pinId) throws Throwable {
//        PlexAPI plexAPI = PlexAPI.builder().build();
//
//        PostUsersSignInDataRequest signInDataRequest = PostUsersSignInDataRequest.builder()
//                .clientID("3381b62b-9ab7-4e37-827b-203e9809eb58")
//                .clientName("Overseer Plex Client")
//                .deviceNickname("Firefox (Overseer)")
//                .clientVersion("1.0.0")
//                .platform("Firefox")
//                .build();
//
//        PostUsersSignInDataResponse response = plexAPI.authentication().postUsersSignInData()
//                .request(signInDataRequest)
//                .call();
//
//        if (response.userPlexAccount().isPresent()) {
//            // handle response
//            log.info(response.userPlexAccount().get().toString());
//        }

        String url = "https://app.plex.tv/auth#?code=p28tvahinguyvbcr9109bc2qc&context%5Bdevice%5D%5Bproduct%5D=Kometa&context%5Bdevice%5D%5Bdevice%5D=OS%20X%2010.15&clientID=0198be0a-5736-730f-a6cc-d172fba3f663";
        url += "&forwardUrl=http://localhost:8080/api/connections/plex?pinId=" + pinId;
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop is not supported, cannot open browser automatically.");
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/plexResponse", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> handlePlexResponse(@RequestParam String pinId) throws Throwable {

//        const url = 'https://plex.tv/api/v2/pins/{pinID}';
//const options = {
//                method: 'GET',
//                headers: {'X-Plex-Client-Identifier': '<x-plex-client-identifier>'},
//        body: undefined
//};

        return ResponseEntity.ok().build();
    }
}
