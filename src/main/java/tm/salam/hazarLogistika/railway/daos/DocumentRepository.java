package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.Document;

import java.util.Date;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Document findHistoryDocumentById(int id);
    Document findHistoryDocumentByCreated(Date created);
    Document findHistoryDocumentByImportt(String importt);
    Document findHistoryDocumentByExport(String export);
}
