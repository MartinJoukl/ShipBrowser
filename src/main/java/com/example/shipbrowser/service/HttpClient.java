package com.example.shipbrowser.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.example.shipbrowser.model.Constants.AZUR_API_SHIPGIRL_URL;
import static com.example.shipbrowser.model.Constants.SHIPS_JSON;

@Service
public class HttpClient {
    String performGet(String uri){
        RestClient restClient = RestClient.create();
        return restClient.method(HttpMethod.GET)
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().body(String.class);
    }
}
