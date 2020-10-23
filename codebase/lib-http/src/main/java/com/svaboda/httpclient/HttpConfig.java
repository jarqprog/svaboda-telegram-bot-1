package com.svaboda.httpclient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class HttpConfig {

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .exchangeStrategies(exchangeStrategiesWith(objectMapper()))
                .clientConnector(new JettyClientHttpConnector(httpClient()))
                .build();
    }

    private HttpClient httpClient() {
        var connectTimeoutInMillis = 1_000;
        var httpClient = new HttpClient(new SslContextFactory.Client());
        httpClient.setConnectTimeout(connectTimeoutInMillis);
        return httpClient;
    }

    private ExchangeStrategies exchangeStrategiesWith(ObjectMapper mapper) {
        return ExchangeStrategies.builder()
                .codecs(clientCodecsConfig -> {
                    clientCodecsConfig.defaultCodecs()
                            .jackson2JsonEncoder(new Jackson2JsonEncoder(mapper, MediaType.APPLICATION_JSON));
                    clientCodecsConfig.defaultCodecs()
                            .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper, MediaType.APPLICATION_JSON));

                })
                .build();
    }

    private static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

}
