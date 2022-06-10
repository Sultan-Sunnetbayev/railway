package tm.salam.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.dtos.StatusVanDTO;
import tm.salam.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.List;

public interface StatusVanService {
    @Transactional
    ResponseTransfer loadNewStatusVansInExcelFiles(MultipartFile excelFile);

    @Transactional
    ResponseTransfer addNewStatusVan(StatusVanDTO statusVanDTO);

    @Transactional
    ResponseTransfer editStatusVanById(StatusVanDTO statusVanDTO);

    @Transactional
    ResponseTransfer removeStatusVanById(int id);

    List<StatusVanDTO> getAllStatusVanDTO();

    StatusVanDTO getStatusVanDTOById(int id);

    StatusVanDTO getStatusVanDTOByShortName(String shortName);
}
