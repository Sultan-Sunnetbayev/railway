package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.services.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/add/new/logist",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addNewLogist(final @ModelAttribute UserDTO userDTO, final MultipartFile image) throws IOException {

        return userService.addNewLogist(userDTO,image);
    }

    @DeleteMapping(path = "/remove/logist/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer removeLogistById(final @RequestParam("id")int id) throws Exception {

        return userService.removeLogistById(id);
    }
}
