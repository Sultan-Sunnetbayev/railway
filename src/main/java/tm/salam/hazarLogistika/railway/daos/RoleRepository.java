package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findRoleByName(String name);
}
