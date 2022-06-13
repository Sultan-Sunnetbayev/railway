package tm.salam.hazarLogistika.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.StationDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.List;

public interface StationService {

    @Transactional
    ResponseTransfer loadNewStationsInExcelFiles(final MultipartFile excelFile);

    @Transactional
    ResponseTransfer addNewStation(final StationDTO stationDTO);

    @Transactional
    ResponseTransfer editStationById(final StationDTO stationDTO);

    @Transactional
    ResponseTransfer removeStationById(final int id);

    List<StationDTO> getAllStationDTOS();

    StationDTO getStationDTOById(final int id);

    StationDTO getStationDTOBYShortName(final String shortName);

    List<String>getFullNameAllStations();
}
