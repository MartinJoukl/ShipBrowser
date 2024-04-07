package com.example.shipbrowser.service;

import com.example.shipbrowser.repository.Skin;
import com.example.shipbrowser.repository.SkinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkinService {

    private final SkinRepository skinRepository;

    public SkinService(SkinRepository skinRepository) {
        this.skinRepository = skinRepository;
    }

    public void remove(List<Skin> skinsToRemove) {
        skinRepository.deleteAll(skinsToRemove);
    }
}
