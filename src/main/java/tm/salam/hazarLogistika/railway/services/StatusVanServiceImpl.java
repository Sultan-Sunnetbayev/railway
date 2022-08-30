package tm.salam.hazarLogistika.railway.services;

import com.sun.source.tree.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.daos.StatusVanRepository;
import tm.salam.hazarLogistika.railway.dtos.StatusVanDTO;
import tm.salam.hazarLogistika.railway.helper.FileUploadUtil;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.StatusVan;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class StatusVanServiceImpl implements StatusVanService{

    private final StatusVanRepository statusVanRepository;
    private final ExcelReaderService excelService;

    @Autowired
    public StatusVanServiceImpl(StatusVanRepository statusVanRepository, ExcelReaderService excelService) {
        this.statusVanRepository = statusVanRepository;
        this.excelService = excelService;
    }

    @Override
    @Transactional
    public ResponseTransfer loadNewStatusVansInExcelFiles(final MultipartFile excelFile){

        final String uploadDir="src/main/resources/excelFiles/statusVan/";
        final String fileName= StringUtils.cleanPath(excelFile.getOriginalFilename());
        List<TreeMap<Integer,List<Object>>>data=null;

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

        for(TreeMap<Integer,List<Object>> helper:data){

            for(Integer key:helper.keySet()){

                List<Object>dataList=helper.get(key);
                StatusVanDTO statusVanDTO=null;

                for(int i=0;i<dataList.size();i++){

                    System.out.println(dataList.get(i));

                    switch (dataList.get(i).toString()){

                        case "Код Номер":
                            indexValues.put(i,"code");
                            break;
                        case "Код_ID":
                            indexValues.put(i,"shortName");
                            break;
                        case "Код":
                            indexValues.put(i,"fullName");
                            break;
                        default:
                            if(indexValues.containsKey(i)){

                                statusVanDTO=setValueStatusVanDTO(indexValues.get(i),dataList.get(i),statusVanDTO);
                            }else{
                                break;
                            }
                    }
                }
                if(statusVanDTO!=null && statusVanRepository.findStatusVanByFullName(statusVanDTO.getFullName())==null &&
                        statusVanRepository.findStatusVanByShortName(statusVanDTO.getShortName())==null &&
                        statusVanRepository.findStatusVanByCode(statusVanDTO.getCode())==null){

                    StatusVan statusVan= StatusVan.builder()
                            .code(statusVanDTO.getCode())
                            .fullName(statusVanDTO.getFullName())
                            .shortName(statusVanDTO.getShortName())
                            .build();

                    statusVanRepository.save(statusVan);
                }
            }
        }

        return new ResponseTransfer("status van's successfull saved",true);
    }

    private StatusVanDTO setValueStatusVanDTO(final String variableName, final Object value, StatusVanDTO statusVanDTO){

//        System.out.println(variableName+" "+value.toString()+" ");
//        System.out.println(statusVanDTO);

        if(statusVanDTO==null){

            statusVanDTO=new StatusVanDTO();
        }
        switch (variableName){

            case "code":
                statusVanDTO.setCode(value.toString());
                break;
            case "shortName":
                statusVanDTO.setShortName(value.toString());
                break;
            case "fullName":
                statusVanDTO.setFullName(value.toString());
                break;
        }

        return statusVanDTO;
    }

    @Override
    @Transactional
    public ResponseTransfer addNewStatusVan(final StatusVanDTO statusVanDTO){

        StatusVan statusVan=statusVanRepository.findStatusVanByFullName(statusVanDTO.getFullName());

        if(statusVan!=null){

            return new ResponseTransfer("full name status van's already added",false);
        }
        statusVan=statusVanRepository.findStatusVanByShortName(statusVanDTO.getShortName());
        if(statusVan!=null){

            return new ResponseTransfer("short name status van's already added",false);
        }
        statusVan=statusVanRepository.findStatusVanByCode(statusVanDTO.getCode());
        if(statusVan!=null){

            return new ResponseTransfer("code status van's already added",false);
        }

        StatusVan savedStatusVan= StatusVan.builder()
                .code(statusVanDTO.getCode())
                .shortName(statusVanDTO.getShortName())
                .fullName(statusVanDTO.getFullName())
                .build();

        statusVanRepository.save(savedStatusVan);
        if(statusVanRepository.findStatusVanByCodeAndShortNameAndFullName(statusVanDTO.getCode(),statusVanDTO.getShortName(),
                statusVanDTO.getFullName())!=null){

            return new ResponseTransfer("new status van successful saved",true);
        }else{

            return new ResponseTransfer("new status van don't saved",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer editStatusVanById(final StatusVanDTO statusVanDTO){

        final StatusVan statusVan= statusVanRepository.findStatusVanById(statusVanDTO.getId());

        if(statusVan==null){

            return new ResponseTransfer("status van not found with this id",false);
        }
        StatusVan helper=statusVanRepository.findStatusVanByFullName(statusVanDTO.getFullName());

        if(helper!=null && helper.getId()!=statusVan.getId()){

            return new ResponseTransfer("new full name status van already added",false);
        }
        helper=statusVanRepository.findStatusVanByShortName(statusVanDTO.getShortName());
        if(helper!=null && helper.getId()!=statusVan.getId()){

            return new ResponseTransfer("new short name status van already added",false);
        }
        helper=statusVanRepository.findStatusVanByCode(statusVanDTO.getCode());
        if(helper!=null && helper.getId()!=statusVan.getId()){

            return new ResponseTransfer("new code status van already added",false);
        }
        statusVan.setCode(statusVanDTO.getCode());
        statusVan.setFullName(statusVanDTO.getFullName());
        statusVan.setShortName(statusVanDTO.getShortName());

        if(statusVanRepository.findStatusVanByCodeAndShortNameAndFullName(statusVanDTO.getCode(),statusVanDTO.getShortName(),
                                                                            statusVanDTO.getFullName())!=null){

            return new ResponseTransfer("status van successful edited",true);
        }else{

            return new ResponseTransfer("status van don't saved",false);
        }

    }

    @Override
    @Transactional
    public ResponseTransfer removeStatusVanById(final int id){

        StatusVan statusVan=statusVanRepository.findStatusVanById(id);

        if(statusVan!=null){

            statusVanRepository.deleteById(id);
        }else{

            return new ResponseTransfer("status van not found with this id",false);
        }
        if(statusVanRepository.findStatusVanById(id)!=null){

            return new ResponseTransfer("status van don't deleted",false);
        }else{

            return new ResponseTransfer("status van successful removed",true);
        }
    }

    @Override
    public List<StatusVanDTO> getAllStatusVanDTO(){

        return statusVanRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private StatusVanDTO toDTO(StatusVan statusVan) {

        StatusVanDTO statusVanDTO= StatusVanDTO.builder()
                .id(statusVan.getId())
                .code(statusVan.getCode())
                .fullName(statusVan.getFullName())
                .shortName(statusVan.getShortName())
                .build();

        return statusVanDTO;
    }

    @Override
    public StatusVanDTO getStatusVanDTOById(final int id){

        StatusVan statusVan=statusVanRepository.findStatusVanById(id);

        if(statusVan==null){

            return null;
        }else{

            return toDTO(statusVan);
        }
    }

    @Override
    public StatusVanDTO getStatusVanDTOByShortName(final String shortName){

        StatusVan statusVan=statusVanRepository.findStatusVanByShortName(shortName);

        if(statusVan==null){

            return null;
        }else{

            return toDTO(statusVan);
        }
    }

}
