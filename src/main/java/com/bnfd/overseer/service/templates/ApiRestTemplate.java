package com.bnfd.overseer.service.templates;

import com.bnfd.overseer.exception.OverseerAuthenticationException;
import com.bnfd.overseer.exception.OverseerAuthorizationException;
import com.bnfd.overseer.exception.OverseerException;
import com.bnfd.overseer.exception.OverseerNotFoundException;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ApiRestTemplate {
    // region - Class Variables -
    private final RestTemplate restTemplate;
    // endregion - Class Variables -

    public <T> T callRest(String url, HttpMethod httpMethod, MediaType contentType, Map<String, String> headerMap, Object requestObj, ParameterizedTypeReference<T> responseType, String queryName, Object... uriVariables) {
        try {
            return restTemplate.exchange(url, httpMethod, new HttpEntity<>(requestObj, getHeadersUsingHeaderMap(contentType, headerMap)), responseType, uriVariables).getBody();
        } catch (RestClientException e) {
            throw getException("Unable to " + queryName, e);
        }
    }

    // region - Private Methods -
    private OverseerException getException(String message, RestClientException ex) {
        var statusCode = Try.of(() -> (HttpClientErrorException) ex)
                .map(HttpClientErrorException::getStatusCode)
                .toJavaOptional()
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        return switch (statusCode) {
            case HttpStatus.UNAUTHORIZED -> new OverseerAuthenticationException(ex.getMessage(), ex);
            case HttpStatus.FORBIDDEN -> new OverseerAuthorizationException(ex.getMessage(), ex);
            case HttpStatus.NOT_FOUND -> new OverseerNotFoundException(ex.getMessage(), ex.getCause());
            default -> new OverseerException(message, ex);
        };
    }

    private HttpHeaders getHeaders(MediaType mediaType) {
        var headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private HttpHeaders getHeadersUsingHeaderMap(MediaType mediaType, Map<String, String> headerMap) {
        var headers = getHeaders(mediaType);

        Optional.ofNullable(headerMap)
                .orElse(Map.of())
                .forEach(headers::set);

        return headers;
    }
    // endregion - Private Methods -
}
