package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.helper.ParseJwtToken;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.User;
import tm.salam.hazarLogistika.railway.security.jwt.JwtTokenProvider;
import tm.salam.hazarLogistika.railway.services.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/logist")
public class LogistController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LogistController(UserService userService,
                            AuthenticationManager authenticationManager,
                            JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping(path = "/get/profile", produces = "application/json")
    public ResponseEntity getProfile(@RequestHeader("Authorization")String token){

        UserDTO userDTO=userService.getUserDTOByEmail(ParseJwtToken.getEmail(token));

        if(userDTO==null){

            return ResponseEntity.badRequest().body("error with parsing token");
        }else{

            return ResponseEntity.ok(userDTO);
        }
    }

    @PostMapping(path = "/edit/profile",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity editProfile(final @ModelAttribute UserDTO logistDTO,
                                      final @RequestParam("image") MultipartFile image,
                                      @RequestHeader("Authorization")String token){

        Map<Object,Object> response=new HashMap<>();
        User checkUser=userService.getUserByEmail(ParseJwtToken.getEmail(token));

        if(checkUser==null){

            return ResponseEntity.badRequest().body("error with parsing token");
        }

        try {
            ResponseTransfer responseTransfer=userService.editProfile(logistDTO,checkUser.getId(),image);

            if(responseTransfer.getStatus().booleanValue()){
                try {

                    token=jwtTokenProvider.createToken(logistDTO.getEmail(),checkUser.getRoles(),checkUser.getId());

                    response.put("access_token",token);
                    response.put("user",userService.getUserDTOById(checkUser.getId()));

                    return ResponseEntity.ok(response);
                }catch (AuthenticationException exception){

                    throw new BadCredentialsException("error with refreshing token");
                }
            }else{
                return ResponseEntity.ok(responseTransfer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error with change image");
        }
    }

}
