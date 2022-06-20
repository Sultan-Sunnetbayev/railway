package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.Data;

import java.util.Date;
import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<Data,Integer> {

    List<Data>findDataByYearDateTimeBetween(Date initialDate, Date finalDate);

    @Query("SELECT d FROM Data d WHERE d.created=(SELECT MAX(dat.created) FROM Data dat WHERE dat.numberVan = :numberVan)")
    Data getLastDataByNumberVan(@Param("numberVan")String numberVan);


    @Query("SELECT d FROM Data d WHERE (d.excelFile.id in :idsExcelFile) AND (LOWER(d.currentStation) in :currentStations) AND " +
            "(LOWER(d.setStation) in :setStations) AND (LOWER(d.typeVan) in :typeVans) AND (d.act in :act) AND " +
            "((d.numberVan IN :numberVan) OR (:numberVan IS NULL))")
    List<Data>getAllDataByExcelFileIdsAndCurrentStationsAndSetStationsAndTypeVans(List<Integer>idsExcelFile,
                                                                                  List<String>currentStations,
                                                                                  List<String>setStations,
                                                                                  List<String>typeVans,
                                                                                  List<Boolean>act,
                                                                                  List<String>numberVan);

    @Query("SELECT d FROM Data d WHERE (d.yearDateTime >= :initialDate AND d.yearDateTime <= :finalDate) AND " +
            "(d.excelFile.id in :idsExcelFile) AND (LOWER(d.currentStation) in :currentStations) AND " +
            "(LOWER(d.setStation) in :setStations) AND (LOWER(d.typeVan) in :typeVans) AND (d.act in :act) " +
            "AND ((d.numberVan IN :numberVan) OR (:numberVan IS NULL ))")
    List<Data>getAllDataByExcelFileIdsAndCurrentStationsAndSetStationsAndTypeVansAndBetweenDates(List<Integer>idsExcelFile,
                                                                                                 List<String>currentStations,
                                                                                                 List<String>setStations,
                                                                                                 List<String>typeVans,
                                                                                                 List<Boolean>act,
                                                                                                 @Param("initialDate") Date initialDate,
                                                                                                 @Param("finalDate") Date finalDate,
                                                                                                 List<String>numberVan);

    @Query("SELECT DISTINCT d.currentStation FROM Data d WHERE (d.excelFile.id in :idExcelFiles)")
    List<String>getCurrentStationsFromData(List<Integer>idExcelFiles);

    @Query("SELECT DISTINCT d.setStation FROM Data d WHERE (d.excelFile.id in :idExcelFiles)")
    List<String>getSetStationsFromData(List<Integer>idExcelFiles);

    List<Data>findDataByNumberVan(String numberVan);
}
