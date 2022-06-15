package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import tm.salam.hazarLogistika.railway.daos.ExcelFileRepository;
import tm.salam.hazarLogistika.railway.dtos.ExcelFileDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelFileSerivceImpl implements ExcelFileService{

    private final ExcelFileRepository excelFileRepository;

    @Autowired
    public ExcelFileSerivceImpl(ExcelFileRepository excelFileRepository) {
        this.excelFileRepository = excelFileRepository;
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
    public List<Integer> getNameAllExcelFiles(){

        List<ExcelFile>excelFiles=excelFileRepository.findAll();
        List<Integer>nameExcelFiles=new ArrayList<>();

        for(ExcelFile excelFile:excelFiles){

            nameExcelFiles.add(excelFile.getId());
        }

        return nameExcelFiles;
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
    public ResponseTransfer saveExcelFile(final ExcelFileDTO excelFileDTO){

        ExcelFile excelFile= ExcelFile.builder()
                .name(excelFileDTO.getName())
                .path(excelFileDTO.getPath())
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
}
