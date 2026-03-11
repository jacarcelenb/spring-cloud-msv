package com.microservicios.report_listener.streams;

import com.microservicios.report_listener.documents.ReportDocument;
import com.microservicios.report_listener.repositories.ReportDocumentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@AllArgsConstructor
public class ReportListener {
   private final ReportDocumentRepository repository;

    @Bean
    public Consumer<String> consumerReport(){

        return report -> {
            this.repository.save(ReportDocument.builder().content(report).build());
        };
    }

}
