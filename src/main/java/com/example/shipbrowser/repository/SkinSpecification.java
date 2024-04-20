package com.example.shipbrowser.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SkinSpecification {
    public static Specification<Skin> createFilterQuery(SkinSearchCriteria skinSearchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (skinSearchCriteria.getName() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), skinSearchCriteria.getName().toLowerCase());
                predicates.add(predicate);
            }

            if (!skinSearchCriteria.isIncludeDefaultAndRetrofit()) {
                Predicate predicate = criteriaBuilder.notEqual(root.get("name"), "Default");
                predicates.add(predicate);
                predicate = criteriaBuilder.notEqual(root.get("name"), "Retrofit");
                predicates.add(predicate);
            }

            if (skinSearchCriteria.getShipName() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("ship").get("name")), skinSearchCriteria.getShipName().toLowerCase());
                predicates.add(predicate);
            }

            if (skinSearchCriteria.getShipCode() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("ship").get("code")), skinSearchCriteria.getShipCode().toLowerCase());
                predicates.add(predicate);
            }

            if (skinSearchCriteria.getShipHullType() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("ship").get("hullType"), skinSearchCriteria.getShipHullType());
                predicates.add(predicate);
            }

            if (skinSearchCriteria.getShipNationality() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("ship").get("nationality")), skinSearchCriteria.getShipNationality().toLowerCase());
                predicates.add(predicate);
            }

            if (skinSearchCriteria.getShipRarity() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("ship").get("rarity"), skinSearchCriteria.getShipRarity());
                predicates.add(predicate);
            }

            if (skinSearchCriteria.getShipShipClass() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("ship").get("shipClass")), skinSearchCriteria.getShipShipClass().toLowerCase());
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
