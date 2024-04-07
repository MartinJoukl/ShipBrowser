package com.example.shipbrowser.repository;

import com.example.shipbrowser.repository.ShipsSearchCriteria;
import com.example.shipbrowser.repository.Ship;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ShipSpecification {
    public static Specification<Ship> createFilterQuery(ShipsSearchCriteria shipSearchCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (shipSearchCriteria.getCode() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("code"), shipSearchCriteria.getCode());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getName() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("name"), shipSearchCriteria.getName());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getHullType() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("hullType"), shipSearchCriteria.getHullType());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getNationality() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("nationality"), shipSearchCriteria.getNationality());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getRarity() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("rarity"), shipSearchCriteria.getRarity());
                predicates.add(predicate);
            }

            if (shipSearchCriteria.getShipClass() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("shipClass"), shipSearchCriteria.getShipClass());
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
