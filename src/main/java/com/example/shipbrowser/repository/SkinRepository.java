package com.example.shipbrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SkinRepository extends JpaRepository<Skin, Long>, JpaSpecificationExecutor<Skin> {

}
