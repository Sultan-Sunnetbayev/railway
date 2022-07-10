package tm.salam.hazarLogistika.railway.services;

import tm.salam.hazarLogistika.railway.dtos.ExcelFileDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.util.List;

public interface ExcelFileService {
    List<ExcelFileDTO> getAllExcelFileDTO();

//    List<Integer> getNameAllExcelFiles();

    ExcelFileDTO getExcelFileDTOByName(String name);

    ExcelFile getExcelFileByName(String name);

    @Transactional
    ResponseTransfer saveExcelFile(ExcelFileDTO excelFileDTO, Integer idDataFixing);

    ExcelFileDTO getExcelFileDTOById(int id);

    List<Integer>getIdExcelFileDTOSByDataFixingId(Integer idDataFixing);

    List<ExcelFileDTO>getAllExcelFilesByIdDataFixing(Integer idDataFixing);
}
