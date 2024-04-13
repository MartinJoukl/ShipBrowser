package com.example.shipbrowser.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ShipSpecification {
    public static Specification<Ship> createFilterQuery(ShipsSearchCriteria shipSearchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (shipSearchCriteria.getCode() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), shipSearchCriteria.getCode().toLowerCase());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getName() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), shipSearchCriteria.getName().toLowerCase());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getHullType() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("hullType"), shipSearchCriteria.getHullType());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getNationality() != null) {
                Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("nationality")), shipSearchCriteria.getNationality().toLowerCase());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getRarity() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("rarity"), shipSearchCriteria.getRarity());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getShipClass() != null) {
                Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get("shipClass")), shipSearchCriteria.getShipClass().toLowerCase());
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
