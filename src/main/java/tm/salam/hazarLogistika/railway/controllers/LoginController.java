package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.helper.ParseJwtToken;
import tm.salam.hazarLogistika.railway.models.User;
import tm.salam.hazarLogistika.railway.security.jwt.JwtTokenProvider;
import tm.salam.hazarLogistika.railway.services.UserService;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    @Value("${upload.imagePath}")
    private String imagePath;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/login", produces = "application/json")
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

//            response.put("user",userService.getUserDTOById(user.getId()));
            response.put("access_token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException exception) {

            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @GetMapping(path = "/get/user",produces = "application/json")
    public ResponseEntity getUserByToken(@RequestHeader("Authorization")String token){

        UserDTO userDTO=userService.getUserDTOByEmail(ParseJwtToken.getEmail(token));

        if(userDTO==null){

            return ResponseEntity.badRequest().body("error with parsing token");
        }

        return ResponseEntity.ok(userDTO);

    }

    @GetMapping(path = "/imageUsers/{file}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("file")String fileName) throws IOException {

        File image=new File(imagePath+"/"+fileName);

        if(image.exists()){

            return ResponseEntity.ok().contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().
                    getContentType(image))).body(Files.readAllBytes(image.toPath()));
        }else{

            return null;
        }
    }

}
