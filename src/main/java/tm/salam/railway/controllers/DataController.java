package tm.salam.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.helper.ResponseTransfer;
import tm.salam.railway.services.DataService;
import tm.salam.railway.services.ExcelService;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {

    private final DataService dataService;
    private final ExcelService excelService;

    @Autowired
    public DataController(DataService dataService, ExcelService excelService) {
        this.dataService = dataService;
        this.excelService = excelService;
    }

    @PostMapping(path = "/load/data/in/excel/file",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
                                                    produces = "application/json")
    public ResponseTransfer loadDataInExcelFile(@RequestParam("excelFile")MultipartFile excelFile){

//        System.out.println(excelService.read());

        return dataService.loadDataInExcelFile(excelFile);
    }

}
