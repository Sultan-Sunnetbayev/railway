package tm.salam.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.railway.daos.VanRepository;
import tm.salam.railway.dtos.VanDTO;
import tm.salam.railway.helper.FileUploadUtil;
import tm.salam.railway.helper.ResponseTransfer;
import tm.salam.railway.models.Van;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VanServiceImpl implements VanService{

    private final VanRepository vanRepository;
    private final ExcelService excelService;

    @Autowired
    public VanServiceImpl(VanRepository vanRepository, ExcelService excelService) {
        this.vanRepository = vanRepository;
        this.excelService = excelService;
    }

    @Override
    @Transactional
    public ResponseTransfer loadVanByExcelFile(final MultipartFile excelFile){

        final String uploadDir="src/main/resources/excelFiles/vans/";
        final String fileName= StringUtils.cleanPath(excelFile.getOriginalFilename());
        List<HashMap<Integer,List<Object>>>data=null;

        try {

            FileUploadUtil.saveFile(uploadDir,fileName,excelFile);
            data=excelService.read(uploadDir+fileName);

        } catch (IOException e) {
            e.printStackTrace();

            return new ResponseTransfer("error with loading excel file vans",false);
        }
        Map<Integer,String> indexValues=new HashMap<>();

        for(HashMap<Integer,List<Object>> helper:data){

            for(Integer key:helper.keySet()){

                List<Object>dataList=helper.get(key);
                VanDTO vanDTO=null;

                for(int i=0;i<dataList.size();i++){

                    switch (dataList.get(i).toString()){

                        case "№ вагона":
                            indexValues.put(i,"code");
                            break;
                        case "Год постр":
                            indexValues.put(i,"yearBuilding");
                            break;
                        case "Срок службы":
                            indexValues.put(i,"periodDuty");
                            break;
                        case "Конец срока службы":
                            indexValues.put(i,"endOfTheDuty");
                            break;
                        case "Ремонт":
                            indexValues.put(i,"dateRepear");
                            break;
                        case "След ремонт":
                            indexValues.put(i,"dateNextRepear");
                            break;
                        case "Дата подписания акта приема передачи":
                            indexValues.put(i,"dateAct");
                            break;
                        case "Срок аренды":
                            indexValues.put(i,"periodLease");
                            break;
                        case "Примечание":
                            indexValues.put(i,"comment");
                            break;
                        default:
                            if(indexValues.containsKey(i)){

                                try {
                                    vanDTO=setValueVanDTO(indexValues.get(i), dataList.get(i),vanDTO);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                break;
                            }
                    }
                }
                if(vanDTO!=null){

                    Van van=vanRepository.findVanByCode(vanDTO.getCode());

                    if(van!=null){

                        van.setYearBuilding(vanDTO.getYearBuilding());
                        van.setPeriodDuty(vanDTO.getPeriodDuty());
                        van.setEndOfTheDuty(vanDTO.getEndOfTheDuty());
                        van.setDateRepear(vanDTO.getDateRepear());
                        van.setDateNextRepear(vanDTO.getDateNextRepear());
                        van.setDateAct(vanDTO.getDateAct());
                        van.setPeriodLease(vanDTO.getPeriodLease());
                        van.setComment(vanDTO.getComment());

                    }else{

                        Van savedVan=Van.builder()
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

    private VanDTO setValueVanDTO(final String variableName, final Object value, VanDTO vanDTO) throws ParseException {

        if(vanDTO==null){

            vanDTO=new VanDTO();
        }
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");

        switch (variableName){

            case "code":
                vanDTO.setCode(value.toString());
                break;
            case "yearBuilding":
                vanDTO.setYearBuilding(Integer.parseInt(value.toString()));
                break;
            case "periodDuty":
                vanDTO.setPeriodDuty(Double.parseDouble(value.toString()));
                break;
            case "endOfTheDuty":
                vanDTO.setEndOfTheDuty(Double.parseDouble(value.toString()));
                break;
            case "dateRepear":
                vanDTO.setDateRepear(formatter.parse(value.toString()));
                break;
            case "dateNextRepear":
                vanDTO.setDateNextRepear(formatter.parse(value.toString()));
                break;
            case "dateAct":
                vanDTO.setDateAct(formatter.parse(value.toString()));
                break;
            case "periodLease":
                vanDTO.setPeriodLease(value.toString());
                break;
            case "comment":
                vanDTO.setComment(value.toString());
                break;
        }

        return vanDTO;
    }

    @Override
    @Transactional
    public ResponseTransfer addNewVan(VanDTO vanDTO){

        Van van=vanRepository.findVanByCode(vanDTO.getCode());

        if(van==null){

            return new ResponseTransfer("new van already added",false);
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
    public ResponseTransfer editVanById(VanDTO vanDTO){

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

            return new ResponseTransfer("van successful eddited",false);
        }else{

            return new ResponseTransfer("van don't edited",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer removeVanById(int id){

        Van van=vanRepository.findVanById(id);

        if(van!=null){

            vanRepository.deleteById(id);
        }else{

            return new ResponseTransfer("van don't found with this id",false);
        }
        if(vanRepository.findVanById(id)!=null){

            return new ResponseTransfer("van don't removed",false);
        }else{

            return new ResponseTransfer("van successful removed",false);
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
    public VanDTO getVanDTOById(int id){

        Van van=vanRepository.findVanById(id);

        if(van==null){

            return null;
        }else{

            return toDTO(van);
        }
    }
}
