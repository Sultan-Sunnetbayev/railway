package tm.salam.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.railway.models.Station;

@Repository
public interface StationRepository extends JpaRepository<Station,Integer> {

    Station findStationByFullName(String fullName);
    Station findStationByShortName(String shortName);
    Station findStationById(int id);
    Station findStationByShortNameAndFullName(String shortName, String fullName);
}
