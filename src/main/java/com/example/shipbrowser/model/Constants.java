package com.example.shipbrowser.model;

import org.springframework.beans.factory.annotation.Value;

public interface Constants {
    @Value("httpService.uri.azur-api-url")
    String AZUR_API_SHIPGIRL_URL = "https://raw.githubusercontent.com/AzurAPI/azurapi-js-setup/master";
    String AZUR_API_SHIPGIRL_IMAGE_URL = AZUR_API_SHIPGIRL_URL + "/images";
    String SHIPS_JSON = "/ships.json";

    String IMAGES_BASE_LOCATION = "../images";
}
