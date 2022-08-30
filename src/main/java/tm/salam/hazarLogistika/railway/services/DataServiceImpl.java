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
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DataServiceImpl implements DataService{

    private final DataRepository dataRepository;
    private final ExcelReaderService excelService;
    private final ExcelFileService excelFileService;
    private final StationService stationService;
    private final StatusVanService statusVanService;
    private final TypeVanService typeVanService;
    private final VanService vanService;
    private final DocumentService documentService;
    private final DataFixingService dataFixingService;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository, ExcelReaderService excelService,
                           ExcelFileService excelFileService, StationService stationService,
                           StatusVanService statusVanService, TypeVanService typeVanService,
                           VanService vanService, DocumentService documentService, DataFixingService dataFixingService) {
        this.dataRepository = dataRepository;
        this.excelService = excelService;
        this.excelFileService=excelFileService;
        this.stationService = stationService;
        this.statusVanService = statusVanService;
        this.typeVanService = typeVanService;
        this.vanService = vanService;
        this.documentService = documentService;
        this.dataFixingService = dataFixingService;
    }

    @Override
    @Transactional
    public ResponseTransfer loadDataInExcelFile(final Integer idDataFixing,final MultipartFile excelFile,
                                                final Integer userId) throws InterruptedException {

        String fileName= StringUtils.cleanPath(excelFile.getOriginalFilename());
        if(excelFileService.getExcelFileByName(fileName)!=null){

            return new ResponseTransfer("excel file exists with name",false);
        }
        final String uploadDir="/home/sultan/data/excelFiles/data/";
        final ExcelFileDTO excelFileDTO=ExcelFileDTO.builder()
                .name(fileName)
                .path(uploadDir)
                .build();

        try {

            FileUploadUtil.saveFile(uploadDir,fileName,excelFile);

        } catch (IOException e) {
            e.printStackTrace();

            return new ResponseTransfer("error with loading excel file",false);
        }
        List<TreeMap<Integer, List<Object>>>data=null;

        try {

            data=excelService.read(uploadDir+fileName);
            excelFileService.saveExcelFile(excelFileDTO,idDataFixing);
            documentService.saveDocument(fileName,userId);

        } catch (IOException e) {

            e.printStackTrace();
            File file=new File(uploadDir+fileName);

            if(file.exists()){

                file.delete();
            }

            return new ResponseTransfer("error with reading excel file",false);
        }
        ExcelFile savedExcelFile =excelFileService.getExcelFileByName(excelFileDTO.getName());

        for(TreeMap<Integer,List<Object>>helper:data){

            int src=0;

            for(Integer key:helper.keySet()){

                src++;
                if(src<=2){

                    continue;
                }
                DataDTO dataDTO=toDataDTO(helper.get(key));

                if(dataDTO!=null && dataDTO.getNumberVan()!=null && dataDTO.getNumberVan()!=" "){

                    if(dataDTO.getYear()!=null && dataDTO.getDate()!=null && dataDTO.getTime()!=null){
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd.MM.yyyy HH-mm");

                        if(dataDTO.getTime()==null){

                            dataDTO.setTime("00-00");
                        }
                        dataDTO.setYear(2000+dataDTO.getYear());
                        try {

                            dataDTO.setYearDateTime(simpleDateFormat.parse(dataDTO.getDate() + "." +
                                                                dataDTO.getYear().intValue()+" " + dataDTO.getTime()));

                        }catch (ParseException exception){

                            exception.printStackTrace();
                        }

                    }
                    Data temporal=dataRepository.getLastDataByNumberVan(dataDTO.getNumberVan());

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
                    Boolean act=false;

                    if(vanDTO!=null && vanDTO.getDateAct()!=null){

                        act=true;
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
                            .act(act)
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
    private DataDTO toDataDTO(final List<Object>data){

        if(data == null || data.isEmpty() || data.get(0)==null || data.get(0)==" "){

            return null;
        }
        while(!data.isEmpty()){

            if(valueIsNumericType(data.get(0).toString())){

                Double value=Double.parseDouble(data.get(0).toString());

                if(value<1000000.0){
                    data.remove(0);
                }else{

                    break;
                }
            }else{

                break;
            }
        }
        DataDTO dataDTO=new DataDTO();

        for(int i=0;i<data.size();i++){

            switch (i){

                case 0:
                    if(!isAlphabeticString(data.get(i).toString())) {
                        dataDTO.setNumberVan(data.get(i).toString());
                    }
                    break;
                case 1:
                    dataDTO.setCodeOfTheProperty(data.get(i).toString());
                    break;
                case 2:
                    dataDTO.setCurrentStation(getFullNameStation(data.get(i).toString()));
                    break;
                case 3:
                    dataDTO.setStatusVan(getStatusVanFullName(data.get(i).toString()));
                    break;
                case 4:
                    if(valueIsNumericType(data.get(i).toString())){

                        dataDTO.setYear(Double.parseDouble(data.get(i).toString()));
                    }
                    break;
                case 5:
                    if(!isAlphabeticString(data.get(i).toString())){

                        dataDTO.setDate(data.get(i).toString());
                    }
                    break;
                case 6:
                    if(!isAlphabeticString(data.get(i).toString())){

                        dataDTO.setTime(data.get(i).toString());
                    }
                    break;
                case 7:
                    dataDTO.setTypeVan(getFullNameTypeVan(data.get(i).toString()));
                    break;
                case 8:
                    dataDTO.setSetStation(getFullNameStation(data.get(i).toString()));
                    break;
                case 9:
                    dataDTO.setIndexTrain(data.get(i).toString());
                    break;
            }
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

    private boolean isAlphabeticString (String str){

        for(int j=0; j<str.length(); j++){
            if(Character.isAlphabetic(str.charAt(j))){

                return true;
            }
        }

        return false;
    }

    @Override
    public Map<String,List<OutputDataDTO>> getAllData(Integer idDataFixing, Date initialDate, Date finalDate){

        List<Integer>idExcelFiles;
        Map<String,List<OutputDataDTO>>response=new TreeMap<>();

        if(idDataFixing==null){

            idDataFixing=dataFixingService.getIdByNameDataFixing("Hazar logistika");
        }
        if(initialDate==null && finalDate==null){

            idExcelFiles=excelFileService.getIdExcelFileDTOSByDataFixingId(idDataFixing);

        }else{
            if(initialDate!=null && finalDate==null){

                finalDate=new Date();
            }
            if(initialDate==null && finalDate!=null){

                initialDate=excelFileService.getDateFirstAddedExcelFile();
            }
                finalDate.setHours(23);
                finalDate.setMinutes(59);
                finalDate.setSeconds(59);
                idExcelFiles = excelFileService.getIdExcelFilesByIdDataFixingAndBetweenDate(idDataFixing, initialDate, finalDate);
        }
        for(Integer id:idExcelFiles) {

            List<Data> data = dataRepository.findDataByExcelFile_Id(id);
            List<OutputDataDTO> outputDataDTOList = new ArrayList<>();

            for (Data helper : data) {

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
                                .act(helper.getAct())
                                .hourForPassedWay(helper.getHourForPassedWay())
                                .dayForRepair(helper.getDayForRepair())
                                .build()
                );
            }

            response.put(excelFileService.getNameExcelFileById(id),outputDataDTOList);
        }

        return response;
    }

    @Override
    public List<String> getCurrentStationsFromData(Integer idDataFixing, List<Integer>idExcelFiles){

        if(idDataFixing==null){
            idDataFixing=dataFixingService.getIdByNameDataFixing("Hazar logistika");
        }
        if(idExcelFiles==null || idExcelFiles.isEmpty()){

            List<Integer>helper=new ArrayList<>();

            excelFileService.getAllExcelFilesByIdDataFixing(idDataFixing).forEach(excelFileDTO ->
            {
                helper.add(excelFileDTO.getId());
            });
            return dataRepository.getCurrentStationsFromData(helper);
        }else {
            return dataRepository.getCurrentStationsFromData(idExcelFiles);
        }
    }

    @Override
    public List<String>getSetStationsFromData(Integer idDataFixing, List<Integer>idExcelFiles){

        if(idDataFixing==null){
            idDataFixing=dataFixingService.getIdByNameDataFixing("Hazar logistika");
        }
        if(idExcelFiles==null || idExcelFiles.isEmpty()){

            List<Integer>helper=new ArrayList<>();

            excelFileService.getAllExcelFilesByIdDataFixing(idDataFixing).forEach(excelFileDTO ->
            {
                helper.add(excelFileDTO.getId());
            });

            return dataRepository.getSetStationsFromData(helper);
        }else {

            return dataRepository.getSetStationsFromData(idExcelFiles);
        }
    }

    @Override
    public File getExcelFileById(final int id){

        ExcelFileDTO excelFileDTO=excelFileService.getExcelFileDTOById(id);
        File file=new File(excelFileDTO.getPath()+excelFileDTO.getName());

        if(file.exists()) {

            documentService.exportDocument(excelFileDTO.getName());

            return file;
        }else{

            return null;
        }
    }

}