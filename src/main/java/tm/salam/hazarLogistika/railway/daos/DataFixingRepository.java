package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.DataFixing;

@Repository
public interface DataFixingRepository extends JpaRepository<DataFixing,Integer> {

    DataFixing findDataFixingById(int id);
    DataFixing findDataFixingByName(String name);

}
