package com.bnfd.overseer.controller;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SwaggerController")
@RestController
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true")
@OpenAPIDefinition(
        info = @Info(
                title = "Overseer API",
                version = "${git.build.version}",
                description = "API for Overseer Application",
                license = @License(name = "GNU GPL"),
                contact = @Contact(
                        name = "Andrew Bengier",
                        email = "andrew.bengier@gmail.com",
                        url = "https://github.com/andrew-bengier"
                )
        ),
        tags = {
                @Tag(name = "Info Endpoints"),
                @Tag(name = "Lookup Endpoints"),
                @Tag(name = "ApiKey Endpoints"),
                @Tag(name = "Server Endpoints"),
                @Tag(name = "Library Endpoints"),
                @Tag(name = "Collection Endpoints")
        }
)
public class SwaggerController {
    @Hidden
    @GetMapping({"/docs", "/swagger", "/swagger-ui"})
    public HttpEntity<Void> redirectToSwagger() {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "/swagger-ui.html")
                .build();
    }
}
