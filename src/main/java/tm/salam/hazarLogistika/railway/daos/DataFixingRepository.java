package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.DataFixing;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DataFixingRepository extends JpaRepository<DataFixing,Integer> {

    DataFixing findDataFixingById(int id);
    DataFixing findDataFixingByName(String name);

    @Query("SELECT CASE WHEN COUNT(dataFixing)>0 THEN TRUE ELSE FALSE END FROM DataFixing dataFixing WHERE " +
            "dataFixing.id = :dataFixingId")
    boolean isDataFixingExistsById(@Param("dataFixingId")int dataFixingId);

    @Query("DELETE FROM DataFixing dataFixing WHERE dataFixing.id = :dataFixingId")
    @Modifying
    @Transactional
    void removeDataFixingById(@Param("dataFixingId")int dataFixingId);

    @Query("SELECT dataFixing FROM DataFixing dataFixing ORDER BY dataFixing.id")
    List<DataFixing> getAllDataFixings();

}
