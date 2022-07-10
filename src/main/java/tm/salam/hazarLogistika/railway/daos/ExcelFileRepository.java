package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import java.util.List;

@Repository
public interface ExcelFileRepository extends JpaRepository<ExcelFile,Integer> {

    ExcelFile getExcelFileByName(String name);
    ExcelFile findExcelFileById(int id);
    ExcelFile findExcelFileByName(String name);
    List<ExcelFile>findExcelFilesByDataFixing_Id(int id);
}
