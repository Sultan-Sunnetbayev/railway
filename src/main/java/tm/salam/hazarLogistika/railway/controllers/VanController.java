package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.VanDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.services.VanService;

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

    @PostMapping(path = "/load/vans/in/excel/file",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer loadVansByExcelFile(final @RequestParam("excelFile") MultipartFile excelFile,
                                                final @RequestParam(value = "userId")Integer userId,
                                                final @RequestParam(value = "dataFixingId")Integer dataFixingId){

        return vanService.loadVanByExcelFile(excelFile,userId,dataFixingId);
    }

}
