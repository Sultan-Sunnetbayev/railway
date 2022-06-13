package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.ExcelFileDTO;
import tm.salam.hazarLogistika.railway.dtos.OutputDataDTO;
import tm.salam.hazarLogistika.railway.dtos.StationDTO;
import tm.salam.hazarLogistika.railway.dtos.TypeVanDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.services.DataService;
import tm.salam.hazarLogistika.railway.services.ExcelFileService;
import tm.salam.hazarLogistika.railway.services.StationService;
import tm.salam.hazarLogistika.railway.services.TypeVanService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {

    private final DataService dataService;
    private final ExcelFileService excelFileService;
    private final TypeVanService typeVanService;
    private final StationService stationService;

    @Autowired
    public DataController(DataService dataService, ExcelFileService excelFileService, TypeVanService typeVanService, StationService stationService) {
        this.dataService = dataService;
        this.excelFileService = excelFileService;
        this.typeVanService = typeVanService;
        this.stationService = stationService;
    }

    @PostMapping(path = "/load/data/in/excel/file",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
                                                    produces = "application/json")
    public ResponseTransfer loadDataInExcelFile(@RequestParam("excelFile")MultipartFile excelFile){

        return dataService.loadDataInExcelFile(excelFile);
    }

    @GetMapping(path = "/get/all/excel/file",produces = "application/json")
    public ResponseEntity getAllNameExcelFile(){

        Map<Object,Object>response=new HashMap<>();

        response.put("excel files",excelFileService.getAllExcelFileDTO());
        response.put("status",true);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/get/data", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public List<OutputDataDTO>getData(@RequestParam(value = "excelFiles", required = false) List<Integer>excelFiles,
                                      @RequestParam(value = "currentStations", required = false)List<String>currentStations,
                                      @RequestParam(value = "setStations", required = false)List<String>setStations,
                                      @RequestParam(value = "typeStations", required = false)List<String>typeStations,
                                      @RequestParam(value = "typeVans", required = false)List<String>typeVans,
                                      @RequestParam(value = "initialDate", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date initialDate,
                                      @RequestParam(value = "finalDate", required = false)
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date finalDate){

        finalDate.setHours(23);
        finalDate.setMinutes(59);
        finalDate.setSeconds(59);

        return dataService.getAllData(excelFiles,currentStations,setStations,typeStations,typeVans,initialDate,finalDate);

    }

    @GetMapping(path = "/get/name/all/excel/files",produces = "application/json")
    public List<ExcelFileDTO> getNameAllExcelFiles(){

        return excelFileService.getAllExcelFileDTO();
    }

    @GetMapping(path = "/get/all/van/types",produces = "application/json")
    public List<TypeVanDTO> getAllTypeVan(){

        return typeVanService.getAllTypeVanDTOS();
    }

    @GetMapping(path ="/get/all/stations",produces = "application/json")
    public List<StationDTO>getAllStations(){

        return stationService.getAllStationDTOS();
    }

}
