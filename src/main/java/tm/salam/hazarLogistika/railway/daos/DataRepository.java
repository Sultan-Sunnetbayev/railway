package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.hazarLogistika.railway.models.Data;

import java.util.Date;
import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<Data,Integer> {

    @Query("SELECT d FROM Data d WHERE d.created=(SELECT MAX(dat.created) FROM Data dat WHERE dat.numberVan = :numberVan)")
    Data getLastDataByNumberVan(@Param("numberVan")String numberVan);

    @Query("SELECT DISTINCT d.currentStation FROM Data d WHERE (d.excelFile.id in :idExcelFiles)")
    List<String>getCurrentStationsFromData(List<Integer>idExcelFiles);

    @Query("SELECT DISTINCT d.setStation FROM Data d WHERE (d.excelFile.id in :idExcelFiles)")
    List<String>getSetStationsFromData(List<Integer>idExcelFiles);

    @Query(nativeQuery = true, value = "SELECT * FROM data WHERE data.excel_file_id = :excelFileId")
    List<Data>findDataByExcelFile_Id(@Param("excelFileId") int excelFileId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE data SET type = :type, color = :color, description = :description " +
            "WHERE data.id = :dataId")
    void updateDataTypeColorDescriptionState(@Param("dataId")int dataId, @Param("type")String type, @Param("color")String color,
                                             @Param("description")String description);

}
