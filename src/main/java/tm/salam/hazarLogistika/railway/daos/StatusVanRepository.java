package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.StatusVan;

@Repository
public interface StatusVanRepository extends JpaRepository<StatusVan,Integer> {

    StatusVan findStatusVanByFullName(String fullName);
    StatusVan findStatusVanByShortName(String shortName);
    StatusVan findStatusVanById(int id);
    StatusVan findStatusVanByCode(String code);
    StatusVan findStatusVanByCodeAndShortNameAndFullName(String code, String shortName, String fullName);
}
