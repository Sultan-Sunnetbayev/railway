package tm.salam.railway.daos;

import org.apache.batik.util.DoublyLinkedList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.railway.models.Van;

import java.util.Date;

@Repository
public interface VanRepository extends JpaRepository<Van,Integer> {

    Van findVanByCode(String code);
    Van findVanById(int id);
    Van findVanByCodeAndYearBuildingAndPeriodDutyAndEndOfTheDutyAndDateRepearAndDateNextRepearAndDateActAndPeriodLeaseAndComment(
            String code, Integer yearBuilding, Double periodDuty,
            Double endOfTheDuty, Date dateRepear, Date dateNextRepear,
            Date dateAct, String periodLease, String comment
    );
}
