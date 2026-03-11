package com.microservicios.report_listener.repositories;

import com.microservicios.report_listener.documents.ReportDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportDocumentRepository  extends MongoRepository<ReportDocument, String> {

}
