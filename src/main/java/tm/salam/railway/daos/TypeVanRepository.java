package tm.salam.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.railway.models.TypeVan;

@Repository
public interface TypeVanRepository extends JpaRepository<TypeVan,Integer> {

    TypeVan findTypeVanById(int id);
    TypeVan findTypeVanByShortName(String shortName);
    TypeVan findTypeVanByFullName(String fullName);
    TypeVan findTypeVanByShortNameAndFullName(String shortName, String fullName);
}
