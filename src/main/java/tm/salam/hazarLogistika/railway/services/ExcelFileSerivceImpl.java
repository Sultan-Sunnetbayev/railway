package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.salam.hazarLogistika.railway.daos.ExcelFileRepository;
import tm.salam.hazarLogistika.railway.dtos.ExcelFileDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ExcelFileSerivceImpl implements ExcelFileService{

    private final ExcelFileRepository excelFileRepository;
    private final DataFixingService dataFixingService;

    @Autowired
    public ExcelFileSerivceImpl(ExcelFileRepository excelFileRepository,
                                DataFixingService dataFixingService) {
        this.excelFileRepository = excelFileRepository;
        this.dataFixingService = dataFixingService;
    }

    @Override
    public List<ExcelFileDTO> getAllExcelFileDTO(){

        return excelFileRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ExcelFileDTO toDTO(ExcelFile excelFile) {

        return ExcelFileDTO.builder()
                .id(excelFile.getId())
                .name(excelFile.getName())
                .path(excelFile.getPath())
                .build();
    }


    @Override
    public ExcelFile getExcelFileByName(final String name){

        ExcelFile excelFile=excelFileRepository.findExcelFileByName(name);

        if(excelFile==null){

            return null;
        }else{

            return excelFile;
        }
    }

    @Override
    public ExcelFileDTO getExcelFileDTOByName(final String name){

        ExcelFile excelFile=excelFileRepository.findExcelFileByName(name);

        if(excelFile==null){

            return null;
        }else{

            return toDTO(excelFile);
        }
    }


    @Override
    @Transactional
    public ResponseTransfer saveExcelFile(final ExcelFileDTO excelFileDTO, final Integer idDataFixing){

        ExcelFile excelFile= ExcelFile.builder()
                .name(excelFileDTO.getName())
                .path(excelFileDTO.getPath())
                .dataFixing(dataFixingService.getDataFixingById(idDataFixing))
                .build();

        excelFileRepository.save(excelFile);

        if(excelFileRepository.findExcelFileByName(excelFileDTO.getName())!=null){

            return new ResponseTransfer("name excel file successful saved",true);
        }else{

            return new ResponseTransfer("name excel file don't saved",false);
        }
    }

    @Override
    public ExcelFileDTO getExcelFileDTOById(final int id){

        ExcelFile excelFile=excelFileRepository.findExcelFileById(id);

        if(excelFile==null){

            return null;
        }else{

            return toDTO(excelFile);
        }
    }

    @Override
    public List<Integer>getIdExcelFileDTOSByDataFixingId(Integer idDataFixing){

        List<ExcelFile>excelFiles=excelFileRepository.findExcelFilesByDataFixing_IdOrderByCreatedDescLastAddedFiveFiles(idDataFixing);
        List<Integer>idExcelFiles=new ArrayList<>();

        for(int i=excelFiles.size()-1;i>=0;i--){
            idExcelFiles.add(excelFiles.get(i).getId());
        }

        return idExcelFiles;
    }

    @Override
    public List<ExcelFileDTO>getAllExcelFilesByIdDataFixing(Integer idDataFixing){

        List<ExcelFile>excelFiles=excelFileRepository.findExcelFilesByDataFixing_Id(idDataFixing);
        int sz=0;
        while(excelFiles!=null && excelFiles.size()>sz){

            String str=excelFiles.get(sz).getName().substring(0,11);

            if(str=="Акты_Ремонт"){

                excelFiles.remove(sz);
            }else{
                sz++;
            }
        }
        return excelFiles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer>getIdExcelFilesByIdDataFixingAndBetweenDate(final Integer idDataFixing, final Date initialDate,
                                                                    final Date finalDate){

        List<ExcelFile>excelFiles=excelFileRepository.findExcelFilesByDataFixing_IdAndCreatedBetween(idDataFixing,initialDate,
                finalDate);
        List<Integer>idExcelFiles=new ArrayList<>();

        excelFiles.forEach(excelFile -> {
            if(excelFile.getName().length()>=11 && !Objects.equals(excelFile.getName().substring(0,11),"Акты_Ремонт")) {
                idExcelFiles.add(excelFile.getId());
            }
        });

        return idExcelFiles;
    }

    @Override
    public String getNameExcelFileById(Integer id){

        ExcelFile excelFile=excelFileRepository.findExcelFileById(id);

        if(excelFile!=null){

            return excelFile.getName();
        }else{

            return null;
        }
    }

    @Override
    @Transactional
    public ResponseTransfer removeExcelFileById(final Integer idExcelFile){

        ExcelFile excelFile=excelFileRepository.findExcelFileById(idExcelFile);

        if(excelFile==null){

            return new ResponseTransfer("excel file don't found with this id",false);
        }
        File file=new File(excelFile.getPath()+"/"+excelFile.getName());

        if(file.exists()){

            file.delete();
        }
        excelFileRepository.removeExcelFileById(idExcelFile);
        if(excelFileRepository.findExcelFileById(idExcelFile)==null){

            return new ResponseTransfer("excel file successful removed",true);
        }else{

            return new ResponseTransfer("excel file don't removed",false);
        }
    }

    @Override
    public Date getDateFirstAddedExcelFile(){

        Date date=excelFileRepository.getDateFirstAddedExcelFiles(
                dataFixingService.getIdByNameDataFixing("Hazar logistika"));

        return date;
    }

}
