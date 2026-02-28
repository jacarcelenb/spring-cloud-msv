package com.microservicios.companies.services;

import com.microservicios.companies.entities.Category;
import com.microservicios.companies.entities.Company;
import com.microservicios.companies.repositories.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CompanyServiceImpl implements  CompanyService{

   private final CompanyRepository repository;

    @Override
    public Company readByName(String name) {
        return this.repository.findByName(name)
                .orElseThrow(()-> new NoSuchElementException("Company not found"));
    }

    @Override
    public Company create(Company company) {
        company.getWebSites().forEach(webSite -> {
            if (Objects.isNull(webSite.getCategory())){

              webSite.setCategory(Category.NONE);
             }
                }
            );
        return this.repository.save(company);
    }

    @Override
    public Company update(Company company, String name) {
        var companyToUpdate = this.repository.findByName(name)
                .orElseThrow(()-> new NoSuchElementException("Company not found"));
      companyToUpdate.setLogo(company.getLogo());
      companyToUpdate.setFoundationDate(company.getFoundationDate());
      companyToUpdate.setFounder(company.getFounder());

        return this.repository.save(companyToUpdate);
    }

    @Override
    public void delete(String name) {
        var companyToDelete = this.repository.findByName(name)
                .orElseThrow(()-> new NoSuchElementException("Company not found"));

        this.repository.delete(companyToDelete);

    }
}
