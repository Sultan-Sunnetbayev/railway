package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import java.util.Date;
import java.util.List;

@Repository
public interface ExcelFileRepository extends JpaRepository<ExcelFile,Integer> {

    ExcelFile getExcelFileByName(String name);
    ExcelFile findExcelFileById(int id);
    ExcelFile findExcelFileByName(String name);
    List<ExcelFile>findExcelFilesByDataFixing_IdAndCreatedBetween(Integer idDatafixing, Date initialDate, Date finalDate);
    List<ExcelFile>findExcelFilesByDataFixing_Id(Integer idDataFixing);
    @Query("SELECT MIN(exl.created) FROM ExcelFile exl WHERE exl.dataFixing.id = :idDataFixing")
    Date getDateFirstAddedExcelFiles(Integer idDataFixing);


}
