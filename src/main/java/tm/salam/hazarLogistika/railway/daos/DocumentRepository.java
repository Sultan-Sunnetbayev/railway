package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.Document;

import javax.print.Doc;
import java.util.Date;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    List<Document>findDocumentsByUserId(Integer userId);
    Document findDocumentById(Integer id);
    List<Document>findDocumentsByUserIdAndCreatedBetween(Integer userId, Date initialDate, Date finalDate);
    List<Document>findDocumentsByCreatedBetween(Date initialDate, Date finalDate);
    @Query("SELECT MIN(file.created) FROM Document file")
    Date getDateFirstAddedFile();
}
