package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.jwt.JwtHelper;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.helper.ParseJwtToken;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.User;
import tm.salam.hazarLogistika.railway.security.jwt.JwtTokenProvider;
import tm.salam.hazarLogistika.railway.services.UserService;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PropertySource("classpath:value.properties")
public class AdminController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Value("${jwt.token.secret}")
    private String secret;

    @Autowired
    public AdminController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(path = "/add/new/logist",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addNewLogist(final @ModelAttribute UserDTO userDTO, final MultipartFile image) throws IOException {

        return userService.addNewLogist(userDTO,image);
    }

    @DeleteMapping(path = "/remove/logist/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer removeLogistById(final @RequestParam("id")int id) throws Exception {

        return userService.removeLogistById(id);
    }

    @GetMapping(path = "/get/profile",produces = "application/json")
    public ResponseEntity getProfile(@RequestHeader("Authorization")String token){

        Map<Object,Object>response=new HashMap<>();
        UserDTO userDTO=userService.getUserDTOByEmail(ParseJwtToken.getEmail(token));

        if(userDTO==null){

            return ResponseEntity.badRequest().body("error with parsing token");
        }

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping(path = "/edit/profile",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity editProfile(@ModelAttribute UserDTO userDTO,
                                      @RequestParam("image") MultipartFile image,
                                      @RequestHeader("Authorization")String token){

        System.out.println(userDTO);
        Map<Object,Object>response=new HashMap<>();
        User checkUser=userService.getUserByEmail(ParseJwtToken.getEmail(token));

        if(checkUser==null){

            return ResponseEntity.badRequest().body("error with parsing token");
        }

        try {
            ResponseTransfer responseTransfer=userService.editProfile(userDTO,checkUser.getId(),image);

            if(responseTransfer.getStatus().booleanValue()){
                try {

                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
                    token=jwtTokenProvider.createToken(checkUser.getEmail(),checkUser.getRoles(),checkUser.getId());

                    response.put("access_token",token);
                    response.put("user",userService.getUserDTOById(checkUser.getId()));

                    return ResponseEntity.ok(response);
                }catch (AuthenticationException exception){

                    throw new BadCredentialsException("error with creating token");
                }
            }else{
                return ResponseEntity.ok(responseTransfer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("error with change image");
        }
    }

    @GetMapping(path = "/get/all/user",produces = "application/json")
    public List<UserDTO> getAllUser(){

        return userService.getAllUserDTO();
    }
}
