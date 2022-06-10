package tm.salam.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;

public interface DataService {

    @Transactional
    ResponseTransfer loadDataInExcelFile(final MultipartFile excelFile);
}
