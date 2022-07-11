package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.salam.hazarLogistika.railway.dtos.DataFixingDTO;
import tm.salam.hazarLogistika.railway.services.DataFixingService;

import java.util.List;

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
}
