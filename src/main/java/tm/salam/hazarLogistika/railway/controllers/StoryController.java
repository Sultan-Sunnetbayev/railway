package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tm.salam.hazarLogistika.railway.dtos.DocumentDTO;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.services.DocumentService;
import tm.salam.hazarLogistika.railway.services.UserService;

import java.util.Date;
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
    public List<DocumentDTO>getAllDocument(@RequestParam(value = "userId",required = false)Integer userId,
                                           @RequestParam(value = "initialDate",required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date initialDate,
                                           @RequestParam(value = "finalDate",required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date finalDate){

        if(finalDate!=null){
            finalDate.setHours(23);
            finalDate.setMinutes(59);
            finalDate.setSeconds(59);
        }
        return documentService.getAllDocumentDTO(userId,initialDate,finalDate);
    }

    @GetMapping(path = "/get/all/logist", produces = "application/json")
    public List<UserDTO>getAllLogist(){

        return userService.getAllUserDTO();
    }

}
