package tm.salam.hazarLogistika.railway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tm.salam.hazarLogistika.railway.models.User;
import tm.salam.hazarLogistika.railway.security.jwt.JwtUser;
import tm.salam.hazarLogistika.railway.security.jwt.JwtUserFactory;
import tm.salam.hazarLogistika.railway.services.UserService;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user=userService.getUserByEmail(email);

        if(user==null){

            throw new UsernameNotFoundException("user with email "+email+" not found");
        }

        JwtUser jwtUser= JwtUserFactory.create(user);

        return jwtUser;
    }
}
