package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.salam.hazarLogistika.railway.dtos.DataFixingDTO;
import tm.salam.hazarLogistika.railway.services.DataFixingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/page")
public class PageController {

    private final DataFixingService dataFixingService;

    @Autowired
    public PageController(DataFixingService dataFixingService) {
        this.dataFixingService = dataFixingService;
    }

    @GetMapping(path = "/get/all/data/fixing", produces = "application/json")
    public List<DataFixingDTO>getAllDataFixing(){

        return dataFixingService.getAllDataFixingDTO();
    }


}
