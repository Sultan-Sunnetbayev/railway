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

    @Autowired
    public DataController(DataService dataService, ExcelFileService excelFileService,
                          TypeVanService typeVanService, DataFixingService dataFixingService) {
        this.dataService = dataService;
        this.excelFileService = excelFileService;
        this.typeVanService = typeVanService;
        this.dataFixingService = dataFixingService;
    }

    @PostMapping(path = "/load/data/in/excel/file",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer loadDataInExcelFile(@RequestParam("idDataFixing")Integer idDataFixing,
                                                @RequestParam(value = "excelFile")MultipartFile excelFile) throws InterruptedException {

        return dataService.loadDataInExcelFile(idDataFixing,excelFile);
    }

    @GetMapping(path = "/get/all/excel/file", produces = "application/json")
    public ResponseEntity getAllNameExcelFile(){

        Map<Object,Object>response=new HashMap<>();

        response.put("excel files",excelFileService.getAllExcelFileDTO());

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/get/data", produces = "application/json")
    public ResponseEntity getData(@RequestParam(value = "excelFiles", required = false) List<Integer>excelFiles,
                                  @RequestParam(value = "idDataFixing",required = false)Integer idDataFixing,
                                  @RequestParam(value = "currentStations", required = false)List<String>currentStations,
                                  @RequestParam(value = "setStations", required = false)List<String>setStations,
                                  @RequestParam(value = "act", required = false)List<Boolean>actAcceptense,
                                  @RequestParam(value = "typeVans", required = false)List<String>typeVans,
                                  @RequestParam(value = "numberVan", required = false)List<String> numberVan,
                                  @RequestParam(value = "initialDate", required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date initialDate,
                                  @RequestParam(value = "finalDate", required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date finalDate){

        if(finalDate!=null) {
            finalDate.setHours(23);
            finalDate.setMinutes(59);
            finalDate.setSeconds(59);
        }

        List<OutputDataDTO>filterData = dataService.getAllData(excelFiles,idDataFixing,currentStations,setStations,
                                                        typeVans, actAcceptense, initialDate,finalDate,numberVan);
        Map<String,Integer>amountIdleVans=new HashMap<>();
        Map<String,Integer>amountLadenVans=new HashMap<>();
        List<List<Object>>filterTable=new ArrayList<>();
        int sumIdleVans=0;
        int sumLadenVans=0;

        for(OutputDataDTO helper:filterData){

            filterTable.add(new ArrayList<>(
                    List.of(helper.getNumberVan(),helper.getAct(),helper.getLastStation(),helper.getCurrentStation())
            ));
            if(Objects.equals(helper.getTypeVan(),"Порожний")){

                sumIdleVans++;
                if(Objects.equals(helper.getCurrentStation(),helper.getSetStation())){

                    if(!amountIdleVans.containsKey(helper.getCurrentStation())){

                        amountIdleVans.put(helper.getCurrentStation(),1);
                    }else{
                        amountIdleVans.put(helper.getCurrentStation(),amountIdleVans.get(helper.getCurrentStation())+1);
                    }
                }
            }else if(Objects.equals(helper.getTypeVan(),"Груженный")){

                sumLadenVans++;
                if(Objects.equals(helper.getCurrentStation(),helper.getSetStation())){

                    if(!amountLadenVans.containsKey(helper.getCurrentStation())){

                        amountLadenVans.put(helper.getCurrentStation(),1);
                    }else{
                        amountLadenVans.put(helper.getCurrentStation(),amountLadenVans.get(helper.getCurrentStation())+1);
                    }
                }
            }
        }

        Map<Object,Object>response=new HashMap<>();

        response.put("data",filterData);
        response.put("amount_idle_vans",amountIdleVans);
        response.put("amount_laden_vans",amountLadenVans);
        response.put("sum_idle_vans",sumIdleVans);
        response.put("sum_laden_vans",sumLadenVans);
        response.put("table",filterTable);

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/get/name/all/excel/files",produces = "application/json")
    public List<ExcelFileDTO> getNameAllExcelFiles(@RequestParam(value = "idDataFixing")Integer idDataFixing){

        System.out.println(idDataFixing);
        if(idDataFixing==null){
            idDataFixing=dataFixingService.getIdByNameDataFixing("hazar_logistika");
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

}