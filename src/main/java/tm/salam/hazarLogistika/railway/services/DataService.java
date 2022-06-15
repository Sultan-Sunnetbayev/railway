package tm.salam.hazarLogistika.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.OutputDataDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Date;
import java.util.List;

public interface DataService {

    @Transactional
    ResponseTransfer loadDataInExcelFile(final MultipartFile excelFile) throws InterruptedException;

    List<OutputDataDTO> getAllData(List<Integer> excelFile, List<String> currentStation, List<String> setStation,
                                   List<String> typeStation, List<String> typeVan, Date initialDate, Date finalDate,
                                   String numberVan);

    List<String> getCurrentStationsFromData(List<Integer>idExcelFiles);

    List<String>getSetStationsFromData(List<Integer>idExcelFiles);

    File getExcelFileById(int id);
}
