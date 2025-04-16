package com.bnfd.overseer.config;

import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {
    // region - Class Variables -
//    @Value("${spring.request.timeout}")
//    private String readTimeout;
//
//    @Value("${spring.request.connection.timeout}")
//    private String requestConnectTimeout;
//
//    @Value("${spring.request.pool.timeout}")
//    private String requestPoolTimeout;
//
//    @Value("${spring.request.connection.pool.max}")
//    private String connectionPoolMax;
//
//    @Value("${spring.request.connection.pool.route.max}")
//    private String connectionPoolMaxPerRoute;
    // endregion - Class Variables -

    @Bean
    public RestTemplate restTemplate() {
        // Connect timeout
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(Long.parseLong("200") * 1000L))
                .setValidateAfterInactivity(Timeout.ofSeconds(1))
                .build();

        // Connection Request timeout
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(Long.parseLong("2000") * 2000L))
                .build();

        // Socket timeout
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(Long.parseLong("200") * 1000L)).build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultSocketConfig(socketConfig);
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setMaxTotal(Integer.parseInt("512"));
        connectionManager.setDefaultMaxPerRoute(Integer.parseInt("64"));

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .disableRedirectHandling()
                .disableCookieManagement()
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofSeconds(3))
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new
                HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(clientHttpRequestFactory);
    }
}
