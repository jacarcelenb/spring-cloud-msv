package com.microservicios.companies.controllers;

import com.microservicios.companies.entities.Company;
import com.microservicios.companies.services.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping(path = "company")
@Slf4j
@Tag(name="companies resource")
public class CompanyController {

    private final CompanyService service;
    @Operation(summary = "get a company given a company name")
    @GetMapping(path = "{name}")
    public ResponseEntity<Company> get(@PathVariable String name){
      log.info("Get: company {}",name);
      return  ResponseEntity.ok(this.service.readByName(name));

    }

    @Operation(summary = "save a company given a company from body")
    @PostMapping
    public ResponseEntity<Company> post(@RequestBody Company company){
        log.info("Post: company {}", company.getName());
        return ResponseEntity.created(URI.create(this.service.create(company).getName()))
                .build();
    }

    @Operation(summary = "update a company given a company from body and name")
    @PutMapping(path = "{name}")
    public ResponseEntity<Company> put(@RequestBody Company company, @PathVariable String name){
        log.info("Put: company {}", company.getName());
        return ResponseEntity.ok(this.service.update(company, name));
    }


    @Operation(summary = "delete a company given a company name")
    @DeleteMapping(path = "{name}")
    public ResponseEntity<?> delete( @PathVariable String name){
        log.info("Delete: company {}", name);
        this.service.delete(name);
        return ResponseEntity.noContent().build();
    }
}
