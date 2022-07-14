package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tm.salam.hazarLogistika.railway.dtos.DocumentDTO;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.services.DocumentService;
import tm.salam.hazarLogistika.railway.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/story")
public class StoryController {

    private final DocumentService documentService;
    private final UserService userService;

    @Autowired
    public StoryController(DocumentService documentService, UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }

    @PostMapping(produces = "application/json")
    public List<DocumentDTO>getAllDocument(@RequestParam(value = "userId",required = false)Integer userId){

        return documentService.getAllDocumentDTO(userId);
    }

    @GetMapping(path = "/get/all/logist", produces = "application/json")
    public List<UserDTO>getAllLogist(){

        return userService.getAllUserDTO();
    }

}
