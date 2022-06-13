package tm.salam.hazarLogistika.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.dtos.StatusVanDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface StatusVanService {
    @Transactional
    ResponseTransfer loadNewStatusVansInExcelFiles(final MultipartFile excelFile);

    @Transactional
    ResponseTransfer addNewStatusVan(final StatusVanDTO statusVanDTO);

    @Transactional
    ResponseTransfer editStatusVanById(final StatusVanDTO statusVanDTO);

    @Transactional
    ResponseTransfer removeStatusVanById(final int id);

    List<StatusVanDTO> getAllStatusVanDTO();

    StatusVanDTO getStatusVanDTOById(final int id);

    StatusVanDTO getStatusVanDTOByShortName(final String shortName);
}
