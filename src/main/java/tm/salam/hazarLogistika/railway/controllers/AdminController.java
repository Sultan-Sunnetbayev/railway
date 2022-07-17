package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(path = "/add/new/logist",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addNewLogist(final @ModelAttribute UserDTO userDTO,
                                         final MultipartFile image) throws IOException {

        return userService.addNewLogist(userDTO,image);
    }

    @PostMapping(path = "/remove/logist/by/id",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer removeLogistById(final @RequestParam("id")int id) throws Exception {

        return userService.removeLogistById(id);
    }

    @GetMapping(path = "/get/profile",produces = "application/json")
    public ResponseEntity getProfile(@RequestHeader("Authorization")String token){

        UserDTO userDTO=userService.getUserDTOByEmail(ParseJwtToken.getEmail(token));

        if(userDTO==null){

            return ResponseEntity.badRequest().body("error with parsing token");
        }

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping(path = "/edit/profile",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity editProfile(final @ModelAttribute UserDTO userDTO,
                                      final @RequestParam("image") MultipartFile image,
                                      @RequestHeader("Authorization")String token){

        Map<Object,Object>response=new HashMap<>();
        User checkUser=userService.getUserByEmail(ParseJwtToken.getEmail(token));

        if(checkUser==null){

            return ResponseEntity.badRequest().body("error with parsing token");
        }

        try {
            ResponseTransfer responseTransfer=userService.editProfile(userDTO,checkUser.getId(),image);

            if(responseTransfer.getStatus().booleanValue()){
                try {

                    token=jwtTokenProvider.createToken(userDTO.getEmail(),checkUser.getRoles(),checkUser.getId());

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

    @GetMapping(path = "/get/all/user",produces = "application/json")
    public List<UserDTO> getAllUser(){

        return userService.getAllUserDTO();
    }
}
