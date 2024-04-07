package com.example.shipbrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShipRepository extends JpaRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {

    //List<Ship> listShipsByCriteria(createFilterQuery(ListShipsSearchCriteria criteria), Paging paging);
}
