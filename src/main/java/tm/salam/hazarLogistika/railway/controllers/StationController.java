//package tm.salam.hazarLogistika.railway.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import tm.salam.hazarLogistika.railway.dtos.StationDTO;
//import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
//import tm.salam.hazarLogistika.railway.services.StationService;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/station")
//public class StationController {
//
//    private final StationService stationService;
//
//    @Autowired
//    public StationController(StationService stationService) {
//        this.stationService = stationService;
//    }
//
//    @GetMapping(path = "/get/all/stations",produces = "application/json")
//    public List<StationDTO>getAllStations(){
//
//        return stationService.getAllStationDTOS();
//    }
//
//    @GetMapping(path = "/get/station/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces="application/json")
//    public ResponseEntity getStationById(final @RequestParam("id")int id){
//
//        StationDTO stationDTO=stationService.getStationDTOById(id);
//        Map<Object,Object>response=new HashMap<>();
//
//        if(stationDTO==null){
//
//            response.put("message","station is not found with this id");
//            response.put("status",false);
//        }else{
//
//            response.put("station",stationDTO);
//            response.put("status",true);
//        }
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping(path = "/load/stations/in/excel/file",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
//    public ResponseTransfer loadNewStationByExcelFile(final @RequestParam("excelFile")MultipartFile excelFile){
//
//        return stationService.loadNewStationsInExcelFiles(excelFile);
//    }
//
//    @PostMapping(path = "/add/new/station",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
//    public ResponseTransfer addNewStation(final @ModelAttribute StationDTO stationDTO){
//
//        return stationService.addNewStation(stationDTO);
//    }
//
//    @PutMapping(path = "/edit/station/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces="application/json")
//    public ResponseTransfer editStationById(final @ModelAttribute StationDTO stationDTO){
//
//        return stationService.editStationById(stationDTO);
//    }
//
//    @DeleteMapping(path = "/remove/station/by/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
//    public ResponseTransfer removeStationById(final @RequestParam("id")int id){
//
//        return stationService.removeStationById(id);
//    }
//
//}
