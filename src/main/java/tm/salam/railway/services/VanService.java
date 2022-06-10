package tm.salam.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.dtos.VanDTO;
import tm.salam.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.List;

public interface VanService {
    @Transactional
    ResponseTransfer loadVanByExcelFile(MultipartFile excelFile);

    @Transactional
    ResponseTransfer addNewVan(VanDTO vanDTO);

    @Transactional
    ResponseTransfer editVanById(VanDTO vanDTO);

    @Transactional
    ResponseTransfer removeVanById(int id);

    List<VanDTO> getAllVanDTOS();

    VanDTO getVanDTOById(int id);
}
