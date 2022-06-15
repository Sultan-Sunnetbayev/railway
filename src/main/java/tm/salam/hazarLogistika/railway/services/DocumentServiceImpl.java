package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.salam.hazarLogistika.railway.daos.DocumentRepository;
import tm.salam.hazarLogistika.railway.dtos.DocumentDTO;
import tm.salam.hazarLogistika.railway.models.Document;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService{

    private final DocumentRepository documentRepository;
    private final String importt="ИМП";
    private final String export="ЭКС";

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional
    public void saveDocument(final String name){

        Document document=Document.builder()
                .name(name)
                .importt(importt)
                .build();

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
    public List<DocumentDTO> getAllDocumentDTO(){

        return documentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DocumentDTO toDTO(Document document) {

        return DocumentDTO.builder()
                .id(document.getId())
                .name(document.getName())
                .importt(document.getImportt())
                .export(document.getExport())
                .instruction(document.getInstruction())
                .dispatch(document.getDispatch())
                .created(document.getCreated())
                .build();
    }

}
