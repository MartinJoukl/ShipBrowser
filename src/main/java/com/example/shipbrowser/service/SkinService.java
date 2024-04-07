package com.example.shipbrowser.service;

import com.example.shipbrowser.model.dto.dtoIn.ListSkinDtoIn;
import com.example.shipbrowser.model.dto.dtoIn.PageInfoDtoIn;
import com.example.shipbrowser.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.shipbrowser.repository.SkinSpecification.createFilterQuery;

@Service
public class SkinService {

    private final SkinRepository skinRepository;

    public SkinService(SkinRepository skinRepository) {
        this.skinRepository = skinRepository;
    }

    public void remove(List<Skin> skinsToRemove) {
        skinRepository.deleteAll(skinsToRemove);
    }

    public Optional<Skin> getBySkinId(long id) {
        return skinRepository.findById(id);
    }

    public Page<Skin> listSkins(ListSkinDtoIn dtoIn) {
        SkinSearchCriteria criteria;
        PageInfoDtoIn pageInfoDtoIn;
        if (dtoIn.getSearchCriteria() == null) {
            criteria = new SkinSearchCriteria();
        } else {
            criteria = dtoIn.getSearchCriteria().toDbCriteria();
        }
        if (dtoIn.getPageInfo() == null) {
            pageInfoDtoIn = new PageInfoDtoIn();
        } else {
            pageInfoDtoIn = dtoIn.getPageInfo();
        }
        PageRequest pageRequest = PageRequest.of(pageInfoDtoIn.getPageIndex(), pageInfoDtoIn.getPageSize());
        if (dtoIn.getSortCriteria() != null) {
            for (Map.Entry<String, String> entry : dtoIn.getSortCriteria().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                pageRequest = pageRequest.withSort(
                        pageRequest.getSort().and(Sort.by(value.toUpperCase().equals(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC, key))
                );
            }
        }
        //Add default sort
        pageRequest = pageRequest.withSort(pageRequest.getSort().and(Sort.by(Sort.Direction.ASC, "id")));
        return skinRepository.findAll(createFilterQuery(criteria), pageRequest);
    }
}
