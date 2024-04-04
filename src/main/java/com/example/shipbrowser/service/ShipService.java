package com.example.shipbrowser.service;

import com.example.shipbrowser.dao.Ship;
import com.example.shipbrowser.dao.ShipRepository;
import com.example.shipbrowser.model.dto.DownloadedShipEntityDtoIn;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.shipbrowser.model.Constants.AZUR_API_SHIPGIRL_URL;
import static com.example.shipbrowser.model.Constants.SHIPS_JSON;

@Service
public class ShipService {

    private final ShipRepository shipRepository;
    private final HttpClient httpClient;

    public ShipService(ShipRepository shipRepository, HttpClient httpClient) {
        this.shipRepository = shipRepository;
        this.httpClient = httpClient;
    }

    public List<Ship> synchronizeShipsWithRemote() throws JsonProcessingException {
        List<Ship> shipsFromDao = shipRepository.findAll();
        Map<String, Ship> shipMap = shipsFromDao.stream().collect(Collectors.toMap(Ship::getOriginalId, Function.identity()));
        String response = httpClient.performGet(AZUR_API_SHIPGIRL_URL + SHIPS_JSON);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DownloadedShipEntityDtoIn[] shipsFromResponse = mapper.readValue(response, DownloadedShipEntityDtoIn[].class);
        List<Ship> upsertedShips = new ArrayList<>();
        for (DownloadedShipEntityDtoIn responseShip : shipsFromResponse
        ) {
            if (shipMap.containsKey(responseShip.getId())) {

            } else if (responseShip.existsOnEn()) {
                Ship newShip = responseShip.toEntity();
            }
        }


        return null;
    }
}
