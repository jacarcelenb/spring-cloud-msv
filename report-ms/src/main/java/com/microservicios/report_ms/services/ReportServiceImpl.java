package com.microservicios.report_ms.services;

import com.microservicios.report_ms.helper.ReportHelper;
import com.microservicios.report_ms.models.Company;
import com.microservicios.report_ms.models.WebSite;
import com.microservicios.report_ms.repositories.CompaniesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final CompaniesRepository repository;
    private final ReportHelper helper;

    @Override
    public String makeReport(String name) {
        return this.helper.readTemplate(this.repository.getByName(name).orElseThrow());

    }

    @Override
    public String saveReport(String report) {
      var format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      var placeHolders = this.helper.getPlaceholderFromTemplate(report);

      var websites = Stream.of(placeHolders.get(3))
              .map(website -> WebSite.builder().name(website).build())
              .toList();

      var company = Company.builder()
              .name(placeHolders.get(0))
              .foundationDate(LocalDate.parse(placeHolders.get(1), format))
              .founder(placeHolders.get(2))
              .webSites(websites)
              .build();

       this.repository.postByName(company);

        return "Saved";
    }

    @Override
    public void deleteReport(String name) {
     this.repository.deleteByName(name);
    }
}
