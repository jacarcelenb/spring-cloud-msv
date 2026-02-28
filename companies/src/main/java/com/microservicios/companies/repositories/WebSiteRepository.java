package com.microservicios.companies.repositories;

import com.microservicios.companies.entities.WebSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebSiteRepository extends JpaRepository<WebSite,Long> {

}
