package tm.salam.hazarLogistika.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.dtos.VanDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface VanService {
    @Transactional
    ResponseTransfer loadVanByExcelFile(final MultipartFile excelFile, final Integer userId, final Integer dataFixingId);

    List<VanDTO> getAllVanDTOS();

    VanDTO getVanDTOById(final int id);

    VanDTO getVanDTOByCode(String code);
}
