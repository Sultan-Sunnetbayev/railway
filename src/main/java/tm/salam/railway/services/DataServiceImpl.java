package tm.salam.railway.services;

import org.bouncycastle.est.jcajce.JsseHostnameAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.daos.DataRepository;
import tm.salam.railway.daos.ExcelFileRepository;
import tm.salam.railway.dtos.DataDTO;
import tm.salam.railway.dtos.StationDTO;
import tm.salam.railway.dtos.StatusVanDTO;
import tm.salam.railway.dtos.TypeVanDTO;
import tm.salam.railway.helper.FileUploadUtil;
import tm.salam.railway.helper.ResponseTransfer;
import tm.salam.railway.models.Data;
import tm.salam.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DataServiceImpl implements DataService{

    private final DataRepository dataRepository;
    private final ExcelService excelService;
    private final ExcelFileRepository excelFileRepository;
    private final StationService stationService;
    private final StatusVanService statusVanService;
    private final TypeVanService typeVanService;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository, ExcelService excelService, ExcelFileRepository excelFileRepository, StationService stationService, StatusVanService statusVanService, TypeVanService typeVanService) {
        this.dataRepository = dataRepository;
        this.excelService = excelService;
        this.excelFileRepository = excelFileRepository;
        this.stationService = stationService;
        this.statusVanService = statusVanService;
        this.typeVanService = typeVanService;
    }

    @Override
    @Transactional
    public ResponseTransfer loadDataInExcelFile(final MultipartFile excelFile){

        final String fileName= StringUtils.cleanPath(excelFile.getOriginalFilename());
        final ExcelFile checkFile=excelFileRepository.getExcelFileByName(fileName.toString());

        if(checkFile!=null){

            return new ResponseTransfer("excel file already added with such name",false);
        }
        final String uploadDir="src/main/resources/excelFiles/data/";
        List<HashMap<Integer, List<Object>>>data=null;

        try {

            FileUploadUtil.saveFile(uploadDir,fileName,excelFile);
            data=excelService.read(uploadDir+fileName);

        } catch (IOException e) {
            e.printStackTrace();

            return new ResponseTransfer("error with loading data excel file",false);
        }
        Map<Integer,String>indexValues=new HashMap<>();
        ExcelFile savedExcelFile= ExcelFile.builder()
                .name(fileName)
                .build();

        excelFileRepository.save(savedExcelFile);
        System.out.println(data);
        for(HashMap<Integer,List<Object>>helper:data){

            for(Integer key:helper.keySet()){

                List<Object>dataList=helper.get(key);
                DataDTO dataDTO=null;

                for(int i=0;i<dataList.size();i++){

                    System.out.println(dataList.get(i).toString());
                    switch (dataList.get(i).toString()){

                        case "№вагона":
                            indexValues.put(i,"numberVan");
                            break;
                        case "Код \n" +
                                "собст.":
                            indexValues.put(i,"codeOfTheProperty");
                            break;
                        case "Станция":
                            indexValues.put(i,"currentStation");
                            break;
                        case "Код":
                            indexValues.put(i,"statusVan");
                            break;
                        case "Год":
                            indexValues.put(i,"year");
                            break;
                        case "Дата":
                            indexValues.put(i,"date");
                            break;
                        case "Время":
                            indexValues.put(i,"time");
                            break;
                        case "Состояние":
                            indexValues.put(i,"typeVan");
                            break;
                        case "Станция \n" +
                                "назн.вагона":
                            indexValues.put(i,"setStation");
                            break;
                        case "Индекс поезда":
                            indexValues.put(i,"indexTrain");
                            break;
                        default:
                            if(indexValues.containsKey(i)){

                                if(Objects.equals(indexValues.get(i),"numberVan")){
                                    if(dataList.get(i)==" " || dataList.get(i)==null){
                                        break;
                                    }
                                }else if(dataDTO==null || dataDTO.getNumberVan()==null){
                                    break;
                                }
                                dataDTO=setValueDataDTO(indexValues.get(i),dataList.get(i),dataDTO);
                            }else{
                                break;
                            }
                    }
                }
                if(dataDTO!=null && dataDTO.getNumberVan()!=null && dataDTO.getNumberVan()!=" "){

                    Data saveData=Data.builder()
                            .numberVan(dataDTO.getNumberVan())
                            .codeOfTheProperty(dataDTO.getCodeOfTheProperty())
                            .currentStation(dataDTO.getCurrentStation())
                            .statusVan(dataDTO.getStatusVan())
                            .year(dataDTO.getYear())
                            .date(dataDTO.getDate())
                            .time(dataDTO.getTime())
                            .typeVan(dataDTO.getTypeVan())
                            .setStation(dataDTO.getSetStation())
                            .indexTrain(dataDTO.getIndexTrain())
                            .excelFile(savedExcelFile)
                            .build();

                    dataRepository.save(saveData);
                }
            }
        }

        return new ResponseTransfer("data successful saved",true);
    }

    private DataDTO setValueDataDTO(String variableName, Object value, DataDTO dataDTO) {

        if(dataDTO==null){

            dataDTO=new DataDTO();
        }
        switch (variableName){

            case "numberVan":
                dataDTO.setNumberVan(value.toString());
                break;
            case "codeOfTheProperty":
                dataDTO.setCodeOfTheProperty(value.toString());
                break;
            case "currentStation":
                dataDTO.setCurrentStation(getFullNameStation(value.toString()));
                break;
            case "statusVan":
                dataDTO.setStatusVan(getStatusVanFullName(value.toString()));
                break;
            case "year":
                dataDTO.setYear(Integer.parseInt(value.toString()));
                break;
            case "date":
                dataDTO.setDate(value.toString());
                break;
            case "time":
                dataDTO.setTime(value.toString());
                break;
            case "typeVan":
                dataDTO.setTypeVan(getFullNameTypeVan(value.toString()));
                break;
            case "setStation":
                dataDTO.setSetStation(getFullNameStation(value.toString()));
                break;
            case "indexTrain":
                dataDTO.setIndexTrain(value.toString());
                break;
        }

        return dataDTO;
    }

    private String getStatusVanFullName(String shortName) {

        StatusVanDTO statusVanDTO=statusVanService.getStatusVanDTOByShortName(shortName);

        if(statusVanDTO==null){

            return null;
        }else{

            return statusVanDTO.getFullName();
        }
    }

    private String getFullNameTypeVan(String shortName) {

        TypeVanDTO typeVanDTO=typeVanService.getTypeVanDTOByShortName(shortName);

        if(typeVanDTO==null){

            return null;
        }else{

            return typeVanDTO.getFullName();
        }
    }

    private String getFullNameStation(String shortName) {

        StationDTO stationDTO=stationService.getStationDTOBYShortName(shortName);

        if(stationDTO==null){

            return shortName;
        }else{

            return stationDTO.getFullName();
        }
    }
}