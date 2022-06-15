package tm.salam.hazarLogistika.railway.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tm.salam.hazarLogistika.railway.models.Role;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@PropertySource("classpath:value.properties")
public class JwtTokenProvider {

    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expired}")
    private Long timeValidityTokenInMilliseconds;

    @Autowired
    private  UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    protected void init(){

        secret= Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(final String email, final List<Role> roles, final int id){

        Claims claims= Jwts.claims().setSubject(email);
        Date date=new Date();
        Date validity=new Date(date.getTime() + timeValidityTokenInMilliseconds);

        claims.put("roles",getNameRoles(roles));
        claims.put("id",id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(final String token){

        UserDetails userDetails=this.userDetailsService.loadUserByUsername(getEmail(token));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    public String getEmail(final String token){

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request){

        String bearerToken=request.getHeader("Authorization");

        if(bearerToken!=null && bearerToken.startsWith("Bearer_")){

            return bearerToken.substring(7,bearerToken.length());
        }

        return null;
    }

    public boolean validateToken(final String token){

        try {

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            if (claimsJws.getBody().getExpiration().before(new Date())) {

                return false;
            }

            return true;
        }catch (JwtException | IllegalArgumentException exception){

            throw new JwtAuthenticationException("jwt token is expired or invalid");
        }
    }

    public List<String> getNameRoles(List<Role>roles){

        List<String>result=new ArrayList<>();

        roles.forEach(role -> {
            result.add(role.getName());
        });

        return result;
    }
}
