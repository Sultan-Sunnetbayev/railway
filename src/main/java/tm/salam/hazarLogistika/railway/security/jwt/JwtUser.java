package tm.salam.hazarLogistika.railway.security.jwt;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class JwtUser implements UserDetails {

    private final int id;
    private final String name;
    private final String surname;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final Date lastDateResetPassword;
    private final Collection<? extends GrantedAuthority>authorities;

    public JwtUser(int id, String name, String surname, String email, String password,
                   boolean enabled, Date lastDateResetPassword, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.lastDateResetPassword = lastDateResetPassword;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public Date getLastDateResetPassword() {
        return lastDateResetPassword;
    }
}
