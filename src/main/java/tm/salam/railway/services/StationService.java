package tm.salam.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.dtos.StationDTO;
import tm.salam.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.List;

public interface StationService {

    @Transactional
    ResponseTransfer loadNewStationsInExcelFiles(MultipartFile excelFile);

    @Transactional
    ResponseTransfer addNewStation(StationDTO stationDTO);

    @Transactional
    ResponseTransfer editStationById(StationDTO stationDTO);

    @Transactional
    ResponseTransfer removeStationById(int id);

    List<StationDTO> getAllStationDTOS();

    StationDTO getStationDTOById(int id);

    StationDTO getStationDTOBYShortName(String shortName);
}
