package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface ExcelFileRepository extends JpaRepository<ExcelFile,Integer> {

    ExcelFile getExcelFileByName(String name);
    @Query("SELECT excelFile FROM ExcelFile excelFile WHERE excelFile.id = :id")
    ExcelFile findExcelFileById(@Param("id") int id);
    @Query("SELECT excelFile FROM ExcelFile excelFile WHERE excelFile.name = :name")
    ExcelFile findExcelFileByName(@Param("name") String name);
    @Query("SELECT excelFile FROM ExcelFile excelFile WHERE excelFile.dataFixing.id = :idDataFixing AND " +
            "excelFile.created BETWEEN :initialDate AND :finalDate")
    List<ExcelFile>findExcelFilesByDataFixing_IdAndCreatedBetween(@Param("idDataFixing")Integer idDatafixing,
                                                                  @Param("initialDate")Date initialDate,
                                                                  @Param("finalDate")Date finalDate);
    @Query("SELECT excelFile FROM ExcelFile excelFile WHERE excelFile.dataFixing.id = :idDataFixing")
    List<ExcelFile>findExcelFilesByDataFixing_Id(@Param("idDataFixing") Integer idDataFixing);
    @Query("SELECT MIN(exl.created) FROM ExcelFile exl WHERE exl.dataFixing.id = :idDataFixing")
    Date getDateFirstAddedExcelFiles(Integer idDataFixing);

    @Query(value = "SELECT * FROM excel_files WHERE (excel_files.data_fixing_id = :idDataFixing) AND " +
            "NOT (LOWER(excel_files.name) LIKE '%акты_ремонт%') ORDER BY excel_files.created DESC LIMIT 5", nativeQuery = true)
    List<ExcelFile>findExcelFilesByDataFixing_IdOrderByCreatedDescLastAddedFiveFiles(@Param("idDataFixing") Integer idDataFixing);

    @Transactional
    @Modifying
    @Query("DELETE FROM ExcelFile excelFile WHERE excelFile.id = :excelFileId")
    void removeExcelFileById(@Param("excelFileId")Integer excelFileId);

}
