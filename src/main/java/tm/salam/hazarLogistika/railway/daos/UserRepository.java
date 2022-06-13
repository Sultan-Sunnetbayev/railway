package tm.salam.hazarLogistika.railway.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.hazarLogistika.railway.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findUserByEmail(String email);
    User findUserById(int id);
}
