package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.ExcelFileDTO;
import tm.salam.hazarLogistika.railway.dtos.OutputDataDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.services.*;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {

    private final DataService dataService;
    private final ExcelFileService excelFileService;
    private final TypeVanService typeVanService;
    private final DataFixingService dataFixingService;
    private final DocumentService documentService;

    @Autowired
    public DataController(DataService dataService, ExcelFileService excelFileService,
                          TypeVanService typeVanService, DataFixingService dataFixingService,
                          DocumentService documentService) {
        this.dataService = dataService;
        this.excelFileService = excelFileService;
        this.typeVanService = typeVanService;
        this.dataFixingService = dataFixingService;
        this.documentService = documentService;
    }

    @PostMapping(path = "/load/data/in/excel/file",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer loadDataInExcelFile(@RequestParam("idDataFixing")Integer idDataFixing,
                                                @RequestParam(value = "excelFile")MultipartFile excelFile,
                                                @RequestParam("userId")Integer userId) throws InterruptedException {

        return dataService.loadDataInExcelFile(idDataFixing,excelFile,userId);
    }

    @GetMapping(path = "/get/all/excel/file", produces = "application/json")
    public ResponseEntity getAllNameExcelFile(){

        Map<Object,Object>response=new HashMap<>();

        response.put("excel files",excelFileService.getAllExcelFileDTO());

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/get/data", produces = "application/json")
    public ResponseEntity getData(@RequestParam(value = "idDataFixing",required = false)Integer idDataFixing,
                                  @RequestParam(value = "initialDate", required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date initialDate,
                                  @RequestParam(value = "finalDate", required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date finalDate){

        Map<String,List<OutputDataDTO>>response=dataService.getAllData(idDataFixing,initialDate,finalDate);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/get/name/all/excel/files",produces = "application/json")
    public List<ExcelFileDTO> getNameAllExcelFiles(@RequestParam(value = "idDataFixing")Integer idDataFixing,
                                                   @RequestParam(value = "initialDate", required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date initialDate,
                                                   @RequestParam(value = "finalDate", required = false)
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date finalDate){

        if(idDataFixing==null){
            idDataFixing=dataFixingService.getIdByNameDataFixing("Hazar logistika");
        }
        return excelFileService.getAllExcelFilesByIdDataFixing(idDataFixing);
    }

    @GetMapping(path = "/get/all/van/types",produces = "application/json")
    public List<String> getAllTypeVan(){

        return typeVanService.getAllFullNameTypeVan();
    }

    @GetMapping(path ="/get/all/currentStations",produces = "application/json")
    public List<String>getAllCurrentStations(@RequestParam(value = "idDataFixing",required = false)Integer idDataFixing,
                                             @RequestParam(value = "idExcelFiles",required = false)List<Integer>idExcelFiles){

        return dataService.getCurrentStationsFromData(idDataFixing, idExcelFiles);
    }

    @GetMapping(path = "/get/all/setStations",produces = "application/json")
    public List<String>getAllSetStations(@RequestParam(value = "idDataFixing",required = false)Integer idDataFixing,
                                         @RequestParam(value = "idExcelFiles",required = false)List<Integer>idExcelFiles){

        return dataService.getSetStationsFromData(idDataFixing, idExcelFiles);
    }

    @GetMapping(path = "/export/excel/file/by/id",produces ="application/json")
    public ResponseEntity getExcelFile(final @RequestParam("id")int id){

        File file=dataService.getExcelFileById(id);
        Map<Object,Object>response=new HashMap<>();

        if(file.exists()){

            response.put("excel file",file);
            response.put("status",true);
        }else{

            response.put("message","not found excel file");
            response.put("status",false);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/remove/excel/file/by/id",produces = "application/json")
    public ResponseTransfer removeExcelFileById(final @RequestParam("idExcelFile")Integer idExcelFile){

        ResponseTransfer responseTransfer=excelFileService.removeExcelFileById(idExcelFile);

        if(responseTransfer.getStatus().booleanValue()){

            documentService.changeStatusById(idExcelFile);
        }

        return responseTransfer;
    }
}