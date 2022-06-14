package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.dtos.*;
import tm.salam.hazarLogistika.railway.helper.FileUploadUtil;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.Data;
import tm.salam.hazarLogistika.railway.daos.DataRepository;
import tm.salam.hazarLogistika.railway.daos.ExcelFileRepository;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService{

    private final DataRepository dataRepository;
    private final ExcelReaderService excelService;
    private final ExcelFileService excelFileService;
    private final StationService stationService;
    private final StatusVanService statusVanService;
    private final TypeVanService typeVanService;
    private final VanService vanService;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository, ExcelReaderService excelService,
                           ExcelFileService excelFileService, StationService stationService,
                           StatusVanService statusVanService, TypeVanService typeVanService, VanService vanService) {
        this.dataRepository = dataRepository;
        this.excelService = excelService;
        this.excelFileService=excelFileService;
        this.stationService = stationService;
        this.statusVanService = statusVanService;
        this.typeVanService = typeVanService;
        this.vanService = vanService;
    }

    @Override
    @Transactional
    public ResponseTransfer loadDataInExcelFile(final MultipartFile excelFile){

        final String fileName= StringUtils.cleanPath(excelFile.getOriginalFilename());
        final ExcelFileDTO checkFile=excelFileService.getExcelFileDTOByName(fileName.toString());

        if(checkFile!=null){

            return new ResponseTransfer("excel file already added with such name",false);
        }
        final String uploadDir="src/main/resources/excelFiles/data/";
        List<HashMap<Integer, List<Object>>>data=null;


        try {

            FileUploadUtil.saveFile(uploadDir,fileName,excelFile);

        } catch (IOException e) {
            e.printStackTrace();

            return new ResponseTransfer("error with saving excel file",false);
        }
        try {

            data=excelService.read(uploadDir+fileName);

        } catch (IOException e) {
            e.printStackTrace();

            return new ResponseTransfer("error with reading excel file",false);
        }

        Map<Integer,String>indexValues=new HashMap<>();
        final ExcelFileDTO excelFileDTO=ExcelFileDTO.builder()
                .name(fileName)
                .build();

        if(!excelFileService.saveExcelFile(excelFileDTO).getStatus()){

            return new ResponseTransfer("error with saving name excel file",false);
        }
        ExcelFile savedExcelFile =excelFileService.getExcelFileByName(excelFileDTO.getName());

        for(HashMap<Integer,List<Object>>helper:data){

            for(Integer key:helper.keySet()){

                List<Object>dataList=helper.get(key);
                DataDTO dataDTO=null;

                for(int i=0;i<dataList.size();i++){

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

                    Data temporal=dataRepository.getLastDataByNumberVan(dataDTO.getNumberVan());

                    if((dataDTO.getYear()!=null && dataDTO.getDate()!=null) || dataDTO.getTime()!=null){
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy HH-mm");

                        if(dataDTO.getTime()==null){

                            dataDTO.setTime("00-00");
                        }
                        dataDTO.setYear(2000+dataDTO.getYear());
                        try {

                            String s=dataDTO.getDate()+"."+dataDTO.getYear().intValue()+" "+dataDTO.getTime();
                            dataDTO.setYearDateTime(simpleDateFormat.parse(dataDTO.getDate() + "." +
                                                                dataDTO.getYear().intValue()+" " + dataDTO.getTime()));

                        }catch (ParseException exception){

                            exception.printStackTrace();
                        }

                    }
                    if(temporal==null){

                        dataDTO.setLastStation(dataDTO.getCurrentStation());
                        dataDTO.setHourForPassedWay(0L);
                    }else{

                        dataDTO.setLastStation(temporal.getCurrentStation());
                        if(dataDTO.getYearDateTime()!=null){

                            long diff=dataDTO.getYearDateTime().getTime()-temporal.getYearDateTime().getTime();
                            dataDTO.setHourForPassedWay(TimeUnit.MILLISECONDS.toHours(diff));
                        }
                    }
                    VanDTO vanDTO=vanService.getVanDTOByCode(dataDTO.getNumberVan());

                    if(vanDTO==null || vanDTO.getDateNextRepear()==null){

                        dataDTO.setDayForRepair(null);
                    }else{
                        long diff=vanDTO.getDateNextRepear().getTime()-(new Date()).getTime();
                        dataDTO.setDayForRepair(TimeUnit.MILLISECONDS.toDays(diff));
                    }
                    Data saveData=Data.builder()
                            .numberVan(dataDTO.getNumberVan())
                            .codeOfTheProperty(dataDTO.getCodeOfTheProperty())
                            .lastStation(dataDTO.getLastStation())
                            .currentStation(dataDTO.getCurrentStation())
                            .statusVan(dataDTO.getStatusVan())
                            .year(dataDTO.getYear())
                            .date(dataDTO.getDate())
                            .time(dataDTO.getTime())
                            .yearDateTime(dataDTO.getYearDateTime())
                            .typeVan(dataDTO.getTypeVan())
                            .setStation(dataDTO.getSetStation())
                            .hourForPassedWay(dataDTO.getHourForPassedWay())
                            .dayForRepair(dataDTO.getDayForRepair())
                            .indexTrain(dataDTO.getIndexTrain())
                            .excelFile(savedExcelFile)
                            .build();

                    dataRepository.save(saveData);
                }
            }
        }

        return new ResponseTransfer("data successful saved",true);
    }

    private DataDTO setValueDataDTO(final String variableName, final Object value, DataDTO dataDTO) {

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
                if(valueIsNumericType(value.toString())){

                    dataDTO.setYear(Double.parseDouble(value.toString()));
                }
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

    private boolean valueIsNumericType(String value) {

        try {

            Double.parseDouble(value.toString());

            return true;
        }catch (NumberFormatException exception){

            return false;
        }
    }

    private String getStatusVanFullName(final String shortName) {

        StatusVanDTO statusVanDTO=statusVanService.getStatusVanDTOByShortName(shortName);

        if(statusVanDTO==null){

            return null;
        }else{

            return statusVanDTO.getFullName();
        }
    }

    private String getFullNameTypeVan(final String shortName) {

        TypeVanDTO typeVanDTO=typeVanService.getTypeVanDTOByShortName(shortName);

        if(typeVanDTO==null){

            return null;
        }else{

            return typeVanDTO.getFullName();
        }
    }

    private String getFullNameStation(final String shortName) {

        StationDTO stationDTO=stationService.getStationDTOBYShortName(shortName);

        if(stationDTO==null){

            return shortName;
        }else{

            return stationDTO.getFullName();
        }
    }

    @Override
    public List<OutputDataDTO> getAllData(List<Integer> idExcelFiles, List<String> currentStation, List<String> setStation,
                                          List<String> typeStation, List<String> typeVan, Date initialDate, Date finalDate){

        if(idExcelFiles==null || idExcelFiles.isEmpty()){

            idExcelFiles=excelFileService.getNameAllExcelFiles();
        }
        if(currentStation==null || currentStation.isEmpty()){
            currentStation=stationService.getFullNameAllStations().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }else{

            currentStation=currentStation.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        if(setStation==null || setStation.isEmpty()){
            setStation=stationService.getFullNameAllStations().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }else{
            setStation=setStation.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        if(typeVan==null || typeVan.isEmpty()){
            typeVan=typeVanService.getAllFullNameTypeVan().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }else{
            typeVan=typeVan.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        List<Data>data=null;

        if(initialDate == null && finalDate == null){

            data=dataRepository.getAllDataByExcelFileIdsAndCurrentStationsAndSetStationsAndTypeVans(idExcelFiles,
                                                                                                    currentStation,
                                                                                                    setStation,
                                                                                                    typeVan);
        }else{

            data=dataRepository.
                    getAllDataByExcelFileIdsAndCurrentStationsAndSetStationsAndTypeVansAndBetweenDates(idExcelFiles,
                                                                                                        currentStation,
                                                                                                        setStation,
                                                                                                        typeVan,
                                                                                                        initialDate,
                                                                                                        finalDate);
        }
        List<OutputDataDTO>outputDataDTOList=new ArrayList<>();

        for(Data helper:data){

            outputDataDTOList.add(
                    OutputDataDTO.builder()
                            .id(helper.getId())
                            .numberVan(helper.getNumberVan())
                            .lastStation(helper.getLastStation())
                            .currentStation(helper.getCurrentStation())
                            .statusVan(helper.getStatusVan())
                            .date(helper.getYearDateTime())
                            .typeVan(helper.getTypeVan())
                            .setStation(helper.getSetStation())
                            .hourForPassedWay(helper.getHourForPassedWay())
                            .dayForRepair(helper.getDayForRepair())
                            .build()
            );
        }

        return outputDataDTOList;
    }
}