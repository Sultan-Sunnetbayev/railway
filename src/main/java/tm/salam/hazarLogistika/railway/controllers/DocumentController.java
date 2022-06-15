package tm.salam.hazarLogistika.railway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tm.salam.hazarLogistika.railway.dtos.DocumentDTO;
import tm.salam.hazarLogistika.railway.services.DocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping(produces = "application/json")
    public List<DocumentDTO>getAllDocument(){

        return documentService.getAllDocumentDTO();
    }
}
