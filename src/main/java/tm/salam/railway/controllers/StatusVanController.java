package tm.salam.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.dtos.StatusVanDTO;
import tm.salam.railway.helper.ResponseTransfer;
import tm.salam.railway.services.StatusVanService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/statusVan")
public class StatusVanController {

    private final StatusVanService statusVanService;

    @Autowired
    public StatusVanController(StatusVanService statusVanService) {
        this.statusVanService = statusVanService;
    }

    @GetMapping(path = "/get/all/statusVan",produces = "application/json")
    public List<StatusVanDTO> getAllStatusVan(){

        return statusVanService.getAllStatusVanDTO();
    }

    @GetMapping(path = "/get/statusVan/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseEntity getStatusVanById(@RequestParam("id")int id){

        StatusVanDTO statusVanDTO=statusVanService.getStatusVanDTOById(id);
        Map<Object,Object>response=new HashMap<>();

        if(statusVanDTO==null){

            response.put("message","status van not found with this id");
            response.put("status",false);
        }else{
            response.put("statusVan",statusVanDTO);
            response.put("status",true);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/renew/statusVan",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces="application/json")
    public ResponseTransfer loadNewStatusVanByExcelFile(final @RequestParam("excelFile")MultipartFile excelFile){

        return statusVanService.loadNewStatusVansInExcelFiles(excelFile);
    }

    @PostMapping(path = "/add/new/statusVan",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addNewStatusVan(final @ModelAttribute StatusVanDTO statusVanDTO){

        return statusVanService.addNewStatusVan(statusVanDTO);
    }

    @PutMapping(path = "/edit/statusVan/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer editStatusVanById(final @ModelAttribute StatusVanDTO statusVanDTO){

        return statusVanService.editStatusVanById(statusVanDTO);
    }

    @DeleteMapping(path = "/remove/statusVan/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer removeStatusVanById(@RequestParam("id")int id){

        return statusVanService.removeStatusVanById(id);
    }

    @GetMapping(path = "/get/statusVan/by/shortName",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
                                                    produces = "application/json")
    public ResponseEntity getStatusVanByShortName(@RequestParam("shortName")String shortName){

        Map<Object,Object>response=new HashMap<>();

        StatusVanDTO statusVanDTO=statusVanService.getStatusVanDTOByShortName(shortName);

        if(statusVanDTO==null){

            response.put("message","status van don't found with this shortName");
            response.put("status",true);
        }else{

            response.put("statusVanDTO",statusVanDTO);
            response.put("status",true);
        }

        return ResponseEntity.ok(response);
    }

}
