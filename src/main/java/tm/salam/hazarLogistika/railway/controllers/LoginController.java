package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tm.salam.hazarLogistika.railway.models.User;
import tm.salam.hazarLogistika.railway.security.jwt.JwtTokenProvider;
import tm.salam.hazarLogistika.railway.services.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(UserService userService, JwtTokenProvider jwtTokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity login(@RequestParam("email")String email,
                                @RequestParam("password")String password) {

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userService.getUserByEmail(email);

            if (user == null) {

                throw new UsernameNotFoundException("user with email " + email + " not found");
            }
            String token = jwtTokenProvider.createToken(email, user.getRoles(), user.getId());

            Map<Object, Object> response = new HashMap<>();

            response.put("user",userService.getUserDTOById(user.getId()));
            response.put("access_token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException exception) {

            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
