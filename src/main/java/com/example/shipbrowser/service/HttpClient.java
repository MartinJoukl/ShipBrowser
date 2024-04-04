package com.example.shipbrowser.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class HttpClient {
    String performGet(String uri) {
        RestClient restClient = RestClient.create();
        return restClient.method(HttpMethod.GET)
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().body(String.class);
    }

    byte[] returnImageBytes(String uri) {
        RestClient restClient = RestClient.create();
        byte[] result = restClient.method(HttpMethod.GET)
                .uri(uri)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve().body(byte[].class);

        return result;
    }
}
