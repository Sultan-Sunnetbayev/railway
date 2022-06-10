package tm.salam.railway.controllers;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.dtos.VanDTO;
import tm.salam.railway.helper.ResponseTransfer;
import tm.salam.railway.services.VanService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/van")
public class VanController {

    private final VanService vanService;

    @Autowired
    public VanController(VanService vanService) {
        this.vanService = vanService;
    }

    @GetMapping(path = "/get/all/vans",produces = "application/json")
    public List<VanDTO> getAllVans(){

        return vanService.getAllVanDTOS();
    }

    @GetMapping(path = "get/van/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity getVanById(@RequestParam("id")int id){

        VanDTO vanDTO=vanService.getVanDTOById(id);
        Map<Object,Object>response=new HashMap<>();

        if(vanDTO==null){

            response.put("message","van don't with this id");
            response.put("status",false);
        }else{
            response.put("van",vanDTO);
            response.put("status",true);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/load/vans/in/excel/file",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer loadVansByExcelFile(final @RequestParam("excelFile") MultipartFile excelFile){

        return vanService.loadVanByExcelFile(excelFile);
    }

    @PostMapping(path = "/add/new/van/",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addNewVan(final @ModelAttribute VanDTO vanDTO){

        return vanService.addNewVan(vanDTO);
    }

    @PutMapping(path = "/edit/van/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer editVanById(final @ModelAttribute VanDTO vanDTO){

        return vanService.editVanById(vanDTO);
    }

    @DeleteMapping(path = "/remove/van/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer removeVanById(final @RequestParam("id")int id){

        return vanService.removeVanById(id);
    }

}
