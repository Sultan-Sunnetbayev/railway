package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.Document;

import javax.print.Doc;
import java.util.Date;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query("SELECT document FROM Document document WHERE document.userId = :userId")
    List<Document>findDocumentsByUserId(@Param("userId") Integer userId);
    Document findDocumentById(Integer id);
    List<Document>findDocumentsByUserIdAndCreatedBetween(Integer userId, Date initialDate, Date finalDate);
    List<Document>findDocumentsByCreatedBetween(Date initialDate, Date finalDate);
    @Query("SELECT MIN(file.created) FROM Document file")
    Date getDateFirstAddedFile();

    @Query("SELECT document.name FROM Document document WHERE document.id = :documentId")
    String getDocumentNameByDocumentId(@Param("documentId")int documentId);

}
