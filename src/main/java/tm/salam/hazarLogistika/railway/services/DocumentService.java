package tm.salam.hazarLogistika.railway.services;

import tm.salam.hazarLogistika.railway.dtos.DocumentDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.List;

public interface DocumentService {

    @Transactional
    void saveDocument(String name);

    @Transactional
    void exportDocument(String name);

    List<DocumentDTO> getAllDocumentDTO();
}
