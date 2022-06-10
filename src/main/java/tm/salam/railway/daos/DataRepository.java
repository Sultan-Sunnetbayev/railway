package tm.salam.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.railway.models.Data;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<Data,Integer> {

    List<Data>findDataByExcelFile_Id(int id);
    List<Data>findDataByNumberVan(long numberVan);
    List<Data>findDataBySetStation(String setStation);
    List<Data>findDataByCurrentStation(String currentStation);
}
