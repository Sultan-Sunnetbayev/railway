package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
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
            "(LOWER(d.setStation) in :setStations) AND (lower(d.typeVan) in :typeVans)")
    List<Data>getAllDataByExcelFileIdsAndCurrentStationsAndSetStationsAndTypeVans(List<Integer>idsExcelFile,
                                                                                  List<String>currentStations,
                                                                                  List<String>setStations,
                                                                                  List<String>typeVans);

    @Query("SELECT d FROM Data d WHERE (d.yearDateTime BETWEEN :initialDate AND :finalDate) AND " +
            "(d.excelFile.id in :idsExcelFile) AND (LOWER(d.currentStation) in :currentStations) AND " +
            "(LOWER(d.setStation) in :setStations) AND (LOWER(d.typeVan) in :typeVans)")
    List<Data>getAllDataByExcelFileIdsAndCurrentStationsAndSetStationsAndTypeVansAndBetweenDates(List<Integer>idsExcelFile,
                                                                                                 List<String>currentStations,
                                                                                                 List<String>setStations,
                                                                                                 List<String>typeVans,
                                                                                                 @Param("initialDate") Date initialDate,
                                                                                                 @Param("finalDate") Date finalDate);

}
