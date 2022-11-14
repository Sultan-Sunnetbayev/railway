package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.daos.DataRepository;
import tm.salam.hazarLogistika.railway.daos.VanRepository;
import tm.salam.hazarLogistika.railway.dtos.ExcelFileDTO;
import tm.salam.hazarLogistika.railway.dtos.VanDTO;
import tm.salam.hazarLogistika.railway.helper.FileUploadUtil;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.Van;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VanServiceImpl implements VanService{

    private final VanRepository vanRepository;
    private final ExcelReaderService excelReaderService;
    private final DataRepository dataRepository;
    private final ExcelFileService excelFileService;
    private final DocumentService documentService;

    @Autowired
    public VanServiceImpl(VanRepository vanRepository, ExcelReaderService excelService,
                          DataRepository dataRepository, ExcelFileService excelFileService,
                          DocumentService documentService) {
        this.vanRepository = vanRepository;
        this.excelReaderService = excelService;
        this.dataRepository = dataRepository;
        this.excelFileService = excelFileService;
        this.documentService = documentService;
    }

    @Override
    @Transactional
    public ResponseTransfer loadVanByExcelFile(final MultipartFile excelFile, final Integer userId, final Integer dataFixingId){

        final String uploadDir="/home/sultan/data/excelFiles/vans/";
        String fileName= "Акты_Ремонт "+new Timestamp(new Date().getTime())+excelFile.getOriginalFilename().substring(
                excelFile.getOriginalFilename().lastIndexOf(".")).toLowerCase();
//        String extension="";
//
//        for(int i=fileName.length()-1;i>=0;i--){
//
//            extension=fileName.charAt(i)+extension;
//            if(fileName.charAt(i)=='.'){
//                break;
//            }
//        }
//        Thread.sleep(1);
//        fileName="Акты_Ремонт "+new Timestamp(new Date().getTime())+extension;

        if(excelFileService.getExcelFileByName(fileName)!=null){

            return new ResponseTransfer("excel file exists with this name",false);
        }
        List<TreeMap<Integer,List<Object>>>data=null;

        try {

            FileUploadUtil.saveFile(uploadDir,fileName,excelFile);

        } catch (IOException e) {

            e.printStackTrace();

            return new ResponseTransfer("error with saving excel file",false);
        }

        try {

            data=excelReaderService.read(uploadDir+fileName);
            ExcelFileDTO excelFileDTO= ExcelFileDTO.builder()
                    .name(fileName)
                    .path(uploadDir)
                    .build();
            excelFileService.saveExcelFile(excelFileDTO,dataFixingId);
            documentService.saveDocument(fileName,userId);

        } catch (IOException e) {

            e.printStackTrace();
            File file=new File(uploadDir+fileName);

            if(file.exists()){

                file.delete();
            }

            return new ResponseTransfer("error with reading excel file",false);
        }
        for(TreeMap<Integer,List<Object>> helper:data){

            int src=0;
            for(Integer key:helper.keySet()){

                if(src==0){

                    src++;
                    continue;
                }
                VanDTO vanDTO=toVanDTO(helper.get(key));

                if(vanDTO!=null && vanDTO.getCode()!=null && vanDTO.getCode()!=" ") {

                    Van van = vanRepository.findVanByCode(vanDTO.getCode());
//                    List<Data>listData=dataRepository.findDataByNumberVan(vanDTO.getCode());
//
//                    for(Data temporalData:listData){
//
//                        if(vanDTO.getDateAct()!=null){
//
//                            temporalData.setAct(true);
//                        }else{
//
//                            temporalData.setAct(false);
//                        }
//                        if(vanDTO.getDateNextRepear()!=null){
//
//                            long diff=vanDTO.getDateNextRepear().getTime()-(new Date()).getTime();
//
//                            temporalData.setDayForRepair(TimeUnit.MILLISECONDS.toDays(diff));
//                        }
//
//                        dataRepository.save(temporalData);
//                    }
                    if (van != null) {

                        van.setYearBuilding(vanDTO.getYearBuilding());
                        van.setPeriodDuty(vanDTO.getPeriodDuty());
                        van.setEndOfTheDuty(vanDTO.getEndOfTheDuty());
                        van.setDateRepear(vanDTO.getDateRepear());
                        van.setDateNextRepear(vanDTO.getDateNextRepear());
                        van.setDateAct(vanDTO.getDateAct());
                        van.setPeriodLease(vanDTO.getPeriodLease());
                        van.setComment(vanDTO.getComment());

                    } else {

                        Van savedVan = Van.builder()
                                .code(vanDTO.getCode())
                                .yearBuilding(vanDTO.getYearBuilding())
                                .periodDuty(vanDTO.getPeriodDuty())
                                .endOfTheDuty(vanDTO.getEndOfTheDuty())
                                .dateRepear(vanDTO.getDateRepear())
                                .dateNextRepear(vanDTO.getDateNextRepear())
                                .dateAct(vanDTO.getDateAct())
                                .periodLease(vanDTO.getPeriodLease())
                                .comment(vanDTO.getComment())
                                .excelFile(excelFileService.getExcelFileByName(fileName))
                                .build();

                        vanRepository.save(savedVan);
                    }
                }

            }
        }

        return new ResponseTransfer("vans successful loaded in excel file",true);
    }

    private VanDTO toVanDTO(final List<Object> objects) {

        if(objects == null || objects.isEmpty()){

            return null;
        }
        while(!objects.isEmpty()){

            if(valueIsNumericType(objects.get(0).toString())){

                Double value=Double.parseDouble(objects.get(0).toString());

                if(value<1000000.0){
                    objects.remove(0);
                }else{

                    break;
                }
            }else{

                break;
            }
        }
        VanDTO vanDTO=new VanDTO();
        final SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");

        for(int i=0;i<objects.size();i++){

            switch (i){

                case 0:
                    if(!valueIsCharacter(objects.get(i).toString())) {

                        vanDTO.setCode(objects.get(i).toString());
                    }
                    break;
                case 1:
                    if(valueIsNumericType(objects.get(i).toString())){

                        vanDTO.setYearBuilding(Double.parseDouble(objects.get(i).toString()));
                    }
                    break;
                case 2:
                    if(valueIsNumericType(objects.get(i).toString())){

                        vanDTO.setPeriodDuty(Double.parseDouble(objects.get(i).toString()));
                    }
                    break;
                case 3:
                    if(valueIsNumericType(objects.get(i).toString())) {

                        vanDTO.setEndOfTheDuty(Double.parseDouble(objects.get(i).toString()));
                    }
                    break;
                case 4:
                    try {

                        vanDTO.setDateRepear(formatter.parse(objects.get(i).toString()));

                    } catch (ParseException e) {

                        e.printStackTrace();

                    }
                    break;
                case 5:
                    try {

                        vanDTO.setDateNextRepear(formatter.parse(objects.get(i).toString()));

                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {

                        vanDTO.setDateAct(formatter.parse(objects.get(i).toString()));

                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    break;
                case 7:
                    vanDTO.setPeriodLease(objects.get(i).toString());
                    break;
                case 8:
                    vanDTO.setComment(objects.get(i).toString());
                    break;
            }
        }

        return vanDTO;
    }

    private boolean valueIsCharacter(String value) {

        for(int j=0; j<value.length(); j++){
            if(Character.isAlphabetic(value.charAt(j))){

                return true;
            }
        }

        return false;

    }

    private boolean valueIsNumericType(String value) {

        try {

            Double.parseDouble(value);

            return true;
        }catch (NumberFormatException exception){

            return false;
        }
    }

    @Override
    public List<VanDTO> getAllVanDTOS(){

        return vanRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private VanDTO toDTO(Van van) {

        return VanDTO.builder()
                .id(van.getId())
                .code(van.getCode())
                .yearBuilding(van.getYearBuilding())
                .periodDuty(van.getPeriodDuty())
                .endOfTheDuty(van.getEndOfTheDuty())
                .dateRepear(van.getDateRepear())
                .dateNextRepear(van.getDateNextRepear())
                .dateAct(van.getDateAct())
                .periodLease(van.getPeriodLease())
                .comment(van.getComment())
                .build();

    }

    @Override
    public VanDTO getVanDTOById(final int id){

        Van van=vanRepository.findVanById(id);

        if(van==null){

            return null;
        }else{

            return toDTO(van);
        }
    }

    @Override
    public VanDTO getVanDTOByCode(final String code){

        Van van=vanRepository.findVanByCode(code);

        if(van==null){

            return null;
        }else{

            return toDTO(van);
        }
    }
}
