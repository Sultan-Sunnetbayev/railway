package tm.salam.hazarLogistika.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.OutputDataDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DataService {

    @Transactional
    ResponseTransfer loadDataInExcelFile(final Integer idDataFixing, final MultipartFile excelFile, final Integer userId) throws InterruptedException;

    Map<String,List<OutputDataDTO>> getAllData(Integer idDataFixing, Date initialDate, Date finalDate);

    List<String> getCurrentStationsFromData(Integer idDataFixing, List<Integer>idExcelFiles);

    List<String>getSetStationsFromData(Integer idDataFixing, List<Integer>idExcelFiles);

    File getExcelFileById(int id);
}
