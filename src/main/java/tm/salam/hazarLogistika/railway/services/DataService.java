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
    ResponseTransfer loadDataInExcelFile(final Integer idDataFixing, final MultipartFile excelFile) throws InterruptedException;

    List<OutputDataDTO> getAllData(List<Integer> excelFile, Integer idDataFixing,List<String> currentStation,
                                   List<String> setStation, List<String> typeVan, List<Boolean>actAcceptense,
                                   Date initialDate, Date finalDate, List<String> numberVan);

    List<String> getCurrentStationsFromData(Integer idDataFixing, List<Integer>idExcelFiles);

    List<String>getSetStationsFromData(Integer idDataFixing, List<Integer>idExcelFiles);

    File getExcelFileById(int id);
}
