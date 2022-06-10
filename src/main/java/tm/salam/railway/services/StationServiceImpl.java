package tm.salam.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.daos.StationRepository;
import tm.salam.railway.dtos.StationDTO;
import tm.salam.railway.helper.FileUploadUtil;
import tm.salam.railway.helper.ResponseTransfer;
import tm.salam.railway.models.Station;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StationServiceImpl implements StationService{

    private final StationRepository stationRepository;
    private final ExcelService excelService;

    @Autowired
    public StationServiceImpl(StationRepository stationRepository, ExcelService excelService) {
        this.stationRepository = stationRepository;
        this.excelService = excelService;
    }

    @Override
    @Transactional
    public ResponseTransfer loadNewStationsInExcelFiles(final MultipartFile excelFile){

        String uploadDir="src/main/resources/excelFiles/stations/";
        String fileName= StringUtils.cleanPath(excelFile.getOriginalFilename());
        List<HashMap<Integer,List<Object>>>data=null;

        try {

            FileUploadUtil.saveFile(uploadDir,fileName,excelFile);
            data=excelService.read(uploadDir + fileName);

        } catch (IOException e) {

            e.printStackTrace();

            return new ResponseTransfer("error with loading excel file",false);
        }
        Map<Integer,String>indexValues=new HashMap<>();

//        stationRepository.deleteAll();
        for(HashMap<Integer,List<Object>> helper:data){

            for(Integer key:helper.keySet()) {

                List<Object> dataList = helper.get(key);
                StationDTO stationDTO = null;

                for (int i = 0; i < dataList.size(); i++) {

                    switch (dataList.get(i).toString()) {
                        case "Станция_ID":
                            indexValues.put(i, "shortName");
                            break;
                        case "Станция":
                            indexValues.put(i, "fullName");
                            break;
                        case "Тип станции":
                            indexValues.put(i, "status");
                            break;
                        default:
                            if (indexValues.containsKey(i)) {
                                stationDTO = setValueStationDTO(indexValues.get(i), dataList.get(i).toString(), stationDTO);
                            } else {
                                break;
                            }
                    }
                }
                if (stationDTO != null && stationRepository.findStationByShortName(stationDTO.getShortName())==null &&
                        stationRepository.findStationByFullName(stationDTO.getFullName())==null) {

//                    System.out.println(stationDTO);

                    Station station = Station.builder()
                            .fullName(stationDTO.getFullName())
                            .shortName(stationDTO.getShortName())
                            .status(stationDTO.getStatus())
                            .build();

                    stationRepository.save(station);
                }
            }
        }

        return new ResponseTransfer("all station successful saved",true);
    }

    private StationDTO setValueStationDTO(final String variableStation, final Object valueStationDTO, StationDTO stationDTO){

        if(stationDTO==null){
            stationDTO=new StationDTO();
        }
        switch(variableStation){
            case "fullName":
                stationDTO.setFullName(valueStationDTO.toString());
                break;
            case "shortName":
                stationDTO.setShortName(valueStationDTO.toString());
                break;
            case "status":
                stationDTO.setStatus(valueStationDTO.toString());
                break;
        }

        return stationDTO;
    }

    @Override
    @Transactional
    public ResponseTransfer addNewStation(final StationDTO stationDTO){

        Station station=stationRepository.findStationByFullName(stationDTO.getFullName());

        if(station!=null){

            return new ResponseTransfer("full name station already added",false);
        }
        station=stationRepository.findStationByShortName(station.getShortName());
        if(station!=null){

            return new ResponseTransfer("short name station already added",false);
        }

        Station saveStation= Station.builder()
                .fullName(stationDTO.getFullName())
                .shortName(stationDTO.getShortName())
                .build();
        stationRepository.save(saveStation);

        return new ResponseTransfer("station successful added",true);
    }

    @Override
    @Transactional
    public ResponseTransfer editStationById(final StationDTO stationDTO){

        Station station=stationRepository.findStationById(stationDTO.getId());
        ResponseTransfer responseTransfer;

        if(station==null){

            return new ResponseTransfer("station is not found with this id",false);
        }
        Station temporal=stationRepository.findStationByFullName(stationDTO.getFullName());
        if(temporal!=null && temporal.getId()!=station.getId()){

            return new ResponseTransfer("full name station's already added",false);
        }
        temporal=stationRepository.findStationByShortName(stationDTO.getShortName());
        if(temporal!=null && temporal.getId()!=station.getId()){

            return new ResponseTransfer("short name station's already added",false);
        }
        station.setFullName(stationDTO.getFullName());
        station.setShortName(stationDTO.getShortName());
        station.setStatus(stationDTO.getStatus());
        if(stationRepository.findStationByShortNameAndFullName(stationDTO.getShortName(),stationDTO.getFullName())!=null) {

            responseTransfer=new ResponseTransfer("station successful edited",true);
        }else{

            responseTransfer=new ResponseTransfer("station don't edited",false);
        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer removeStationById(int id){

        Station station=stationRepository.findStationById(id);
        ResponseTransfer responseTransfer;
        if(station==null){

            return new ResponseTransfer("station is not found with this id",false);
        }else{

            stationRepository.deleteById(id);
        }
        if(stationRepository.findStationById(id)==null){

            responseTransfer=new ResponseTransfer("station successful removed",true);
        }else{

            responseTransfer=new ResponseTransfer("station don't removed",false);
        }

        return responseTransfer;
    }

    @Override
    public List<StationDTO> getAllStationDTOS(){

        return stationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private StationDTO toDTO(Station station) {

        return StationDTO.builder()
                .id(station.getId())
                .fullName(station.getFullName())
                .shortName(station.getShortName())
                .status(station.getStatus())
                .build();
    }

    @Override
    public StationDTO getStationDTOById(int id){

        Station station=stationRepository.findStationById(id);

        if(station==null){

            return null;
        }else {

            return toDTO(station);
        }

    }

    @Override
    public StationDTO getStationDTOBYShortName(String shortName){

        Station station=stationRepository.findStationByShortName(shortName);

        if(station==null){

            return null;
        }else{

            return toDTO(station);
        }
    }

}
