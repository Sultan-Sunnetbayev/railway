package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.hazarLogistika.railway.dtos.DataFixingDTO;
import tm.salam.hazarLogistika.railway.models.DataFixing;
import tm.salam.hazarLogistika.railway.services.DataFixingService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dataFixing")
public class DataFixingController {

    private final DataFixingService dataFixingService;

    @Autowired
    public DataFixingController(DataFixingService dataFixingService) {
        this.dataFixingService = dataFixingService;
    }

    @GetMapping(path = "/get/all",produces = "application/json")
    public List<DataFixingDTO>getAllDataFixing(){

        List<DataFixingDTO>dataFixingDTOS=dataFixingService.getAllDataFixingDTO();
        dataFixingDTOS.remove(0);

        return dataFixingDTOS;
    }

    @PostMapping(path = "/add/dataFixing", produces = "application/json")
    public ResponseEntity addDataFixing(final @ModelAttribute DataFixing dataFixing){

        Map<String,Object> response=new HashMap<>();

        if(dataFixingService.isDataFixingExists(dataFixing)){

            response.put("status",false);
            response.put("message","error this data fixing already exists");

            return ResponseEntity.ok(response);
        }
        dataFixingService.addDataFixing(dataFixing);
        if(dataFixingService.isDataFixingExists(dataFixing)){

            response.put("status",true);
            response.put("message","accept data fixing successful added");
        }else{

            response.put("status",false);
            response.put("message","error data fixing don't added");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/edit/dataFixing/by/id", produces = "application/json")
    public ResponseEntity editDataFixingById(@ModelAttribute DataFixing dataFixing){

        Map<String, Object>response=new LinkedHashMap<>();

        if(!dataFixingService.isDataFixingExistsById(dataFixing.getId())){
            response.put("status",false);
//            response.put("code","");
            response.put("message","error data fixing not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(dataFixingService.editDataFixingById(dataFixing)){
            response.put("status",true);
//            response.put("code","");
            response.put("message","accept data fixing successful edited");

            return ResponseEntity.ok(response);
        }else{
            response.put("status",false);
//            response.put("code","");
            response.put("message","accept data fixing don't edited");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @DeleteMapping(path = "/remove/dataFixing/by/id", produces = "application/json")
    public ResponseEntity removeDataFixingById(@RequestParam("dataFixingId")Integer dataFixingById){

        Map<String, Object>response=new LinkedHashMap<>();

        if(!dataFixingService.isDataFixingExistsById(dataFixingById)){
            response.put("status",false);
//            response.put("code","");
            response.put("messagge","error data fixing not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        dataFixingService.removeDataFixingById(dataFixingById);
        if(!dataFixingService.isDataFixingExistsById(dataFixingById)){
            response.put("status",true);
//            response.put("code","");
            response.put("message","accept data fixing successful removed");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else{
            response.put("status",false);
//            response.put("code","");
            response.put("message","error data fixing don't removed");

            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(response);
        }
    }

}
