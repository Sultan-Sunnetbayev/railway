package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.salam.hazarLogistika.railway.daos.DocumentRepository;
import tm.salam.hazarLogistika.railway.dtos.DocumentDTO;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.models.Document;
import tm.salam.hazarLogistika.railway.models.ExcelFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService{

    private final DocumentRepository documentRepository;
    private final UserService userService;
    private final ExcelFileService excelFileService;
    private final String importt="ИМП";
    private final String export="ЭКС";

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserService userService, ExcelFileService excelFileService) {
        this.documentRepository = documentRepository;
        this.userService = userService;
        this.excelFileService = excelFileService;
    }

    @Override
    @Transactional
    public void saveDocument(final String name, final Integer userId){

        UserDTO userDTO=userService.getUserDTOById(userId);
        Document document=null;
        if(userDTO!=null) {
            document = Document.builder()
                    .name(name)
                    .importt(importt)
                    .userId(userId)
                    .logistName(userDTO.getName())
                    .logistSurname(userDTO.getSurname())
                    .build();
        }else{
            document= Document.builder()
                    .name(name)
                    .importt(importt)
                    .userId(userId)
                    .build();
        }

        documentRepository.save(document);
    }

    @Override
    @Transactional
    public void exportDocument(final String name){

        Document document= Document.builder()
                .name(name)
                .export(export)
                .build();

        documentRepository.save(document);
    }

    @Override
    public List<DocumentDTO> getAllDocumentDTO(final Integer userId){

        if(userId==null) {
            return documentRepository.findAll().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }

        return documentRepository.findDocumentsByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DocumentDTO toDTO(Document document) {

        ExcelFile excelFile=excelFileService.getExcelFileByName(document.getName());
        Integer idDataFixing=null;
        if(excelFile!=null){
            idDataFixing=excelFile.getDataFixing().getId();
        }
        return DocumentDTO.builder()
                .id(document.getId())
                .name(document.getName())
                .importt(document.getImportt())
                .export(document.getExport())
                .instruction(document.getInstruction())
                .dispatch(document.getDispatch())
                .created(document.getCreated())
                .userId(document.getUserId())
                .logistName(document.getLogistName())
                .logistSurname(document.getLogistSurname())
                .idDataFixing(idDataFixing)
                .build();
    }

}
