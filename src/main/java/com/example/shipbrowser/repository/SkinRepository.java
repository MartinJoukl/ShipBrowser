package com.example.shipbrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SkinRepository extends JpaRepository<Skin, Long>, JpaSpecificationExecutor<Skin> {
    List<Skin> findAllByShipId(Long shipId);
}
