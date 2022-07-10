package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.daos.DataRepository;
import tm.salam.hazarLogistika.railway.daos.VanRepository;
import tm.salam.hazarLogistika.railway.dtos.DataDTO;
import tm.salam.hazarLogistika.railway.dtos.VanDTO;
import tm.salam.hazarLogistika.railway.helper.FileUploadUtil;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.Data;
import tm.salam.hazarLogistika.railway.models.Van;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class VanServiceImpl implements VanService{

    private final VanRepository vanRepository;
    private final ExcelReaderService excelService;
    private final DataRepository dataRepository;

    @Autowired
    public VanServiceImpl(VanRepository vanRepository, ExcelReaderService excelService, DataRepository dataRepository) {
        this.vanRepository = vanRepository;
        this.excelService = excelService;
        this.dataRepository = dataRepository;
    }

    @Override
    @Transactional
    public ResponseTransfer loadVanByExcelFile(final MultipartFile excelFile){

        final String uploadDir="src/main/resources/excelFiles/vans/";
        final String fileName= StringUtils.cleanPath(excelFile.getOriginalFilename());
        List<HashMap<Integer,List<Object>>>data=null;

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

        Map<Integer,String> indexValues=new HashMap<>();

        for(HashMap<Integer,List<Object>> helper:data){

            int src=0;
            for(Integer key:helper.keySet()){

                if(src==0){

                    src++;
                    continue;
                }
                VanDTO vanDTO=toVanDTO(helper.get(key));

                if(vanDTO!=null && vanDTO.getCode()!=null && vanDTO.getCode()!=" ") {

                    Van van = vanRepository.findVanByCode(vanDTO.getCode());
                    List<Data>listData=dataRepository.findDataByNumberVan(vanDTO.getCode());

                    for(Data temporalData:listData){

                        if(vanDTO.getDateAct()!=null){

                            temporalData.setAct(true);
                        }else{

                            temporalData.setAct(false);
                        }
                        if(vanDTO.getDateNextRepear()!=null){

                            long diff=vanDTO.getDateNextRepear().getTime()-(new Date()).getTime();

                            temporalData.setDayForRepair(TimeUnit.MILLISECONDS.toDays(diff));
                        }

                        dataRepository.save(temporalData);
                    }
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
                                .build();

                        vanRepository.save(savedVan);
                    }
                }

            }
        }

        return new ResponseTransfer("vans successful loaded in excel file",true);
    }

    private VanDTO toVanDTO(final List<Object> objects) {

        VanDTO vanDTO=new VanDTO();
        final SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");

        for(int i=0;i<objects.size();i++){

            switch (i){

                case 1:
                    vanDTO.setCode(objects.get(i).toString());
                    break;
                case 2:
                    if(valueIsNumericType(objects.get(i).toString())){

                        vanDTO.setYearBuilding(Double.parseDouble(objects.get(i).toString()));
                    }
                    break;
                case 3:
                    if(valueIsNumericType(objects.get(i).toString())){

                        vanDTO.setPeriodDuty(Double.parseDouble(objects.get(i).toString()));
                    }
                    break;
                case 4:
                    if(valueIsNumericType(objects.get(i).toString())) {

                        vanDTO.setEndOfTheDuty(Double.parseDouble(objects.get(i).toString()));
                    }
                    break;
                case 5:
                    try {

                        vanDTO.setDateRepear(formatter.parse(objects.get(i).toString()));

                    } catch (ParseException e) {

                        e.printStackTrace();

                    }
                    break;
                case 6:
                    try {

                        vanDTO.setDateNextRepear(formatter.parse(objects.get(i).toString()));

                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {

                        vanDTO.setDateAct(formatter.parse(objects.get(i).toString()));

                    } catch (ParseException e) {

                        e.printStackTrace();
                    }
                    break;
                case 8:
                    vanDTO.setPeriodLease(objects.get(i).toString());
                    break;
                case 9:
                    vanDTO.setComment(objects.get(i).toString());
                    break;
            }
        }

        return vanDTO;
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
    @Transactional
    public ResponseTransfer addNewVan(final VanDTO vanDTO){

        Van van=vanRepository.findVanByCode(vanDTO.getCode());

        if(van!=null){

            return new ResponseTransfer("code new van's already added",false);
        }
        van= Van.builder()
                .code(vanDTO.getCode())
                .yearBuilding(vanDTO.getYearBuilding())
                .periodDuty(vanDTO.getPeriodDuty())
                .endOfTheDuty(vanDTO.getEndOfTheDuty())
                .dateRepear(vanDTO.getDateRepear())
                .dateNextRepear(vanDTO.getDateNextRepear())
                .dateAct(vanDTO.getDateAct())
                .periodLease(vanDTO.getPeriodLease())
                .comment(vanDTO.getComment())
                .build();

        vanRepository.save(van);

        if(vanRepository.findVanByCode(vanDTO.getCode())!=null){

            return new ResponseTransfer("new van successful added",true);
        }else{

            return new ResponseTransfer("new van don't added",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer editVanById(final VanDTO vanDTO){

        Van van=vanRepository.findVanById(vanDTO.getId());

        if(van==null){

            return new ResponseTransfer("van don't found with this id",false);
        }
        Van check=vanRepository.findVanByCode(vanDTO.getCode());

        if(van.getId()!=check.getId()){

            return new ResponseTransfer("code van's already added other van",false);
        }
        van.setCode(vanDTO.getCode());
        van.setYearBuilding(vanDTO.getYearBuilding());
        van.setPeriodDuty(vanDTO.getPeriodDuty());
        van.setEndOfTheDuty(vanDTO.getEndOfTheDuty());
        van.setDateRepear(vanDTO.getDateRepear());
        van.setDateNextRepear(vanDTO.getDateNextRepear());
        van.setDateAct(vanDTO.getDateAct());
        van.setPeriodLease(vanDTO.getPeriodLease());
        van.setComment(vanDTO.getComment());

        if(vanRepository.findVanByCodeAndYearBuildingAndPeriodDutyAndEndOfTheDutyAndDateRepearAndDateNextRepearAndDateActAndPeriodLeaseAndComment(
                vanDTO.getCode(),vanDTO.getYearBuilding(),vanDTO.getPeriodDuty(),vanDTO.getEndOfTheDuty(),vanDTO.getDateRepear(),
                vanDTO.getDateNextRepear(),vanDTO.getDateAct(),vanDTO.getPeriodLease(),vanDTO.getComment())!=null){

            return new ResponseTransfer("van successful eddited",true);
        }else{

            return new ResponseTransfer("van don't edited",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer removeVanById(final int id){

        Van van=vanRepository.findVanById(id);

        if(van!=null){

            vanRepository.deleteById(id);
        }else{

            return new ResponseTransfer("van don't found with this id",false);
        }
        if(vanRepository.findVanById(id)!=null){

            return new ResponseTransfer("van don't removed",false);
        }else{

            return new ResponseTransfer("van successful removed",true);
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
