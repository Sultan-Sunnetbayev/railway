package tm.salam.hazarLogistika.railway.services;

import tm.salam.hazarLogistika.railway.dtos.DocumentDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface DocumentService {

    @Transactional
    void saveDocument(String name, Integer userId);

    @Transactional
    void exportDocument(String name);

    List<DocumentDTO> getAllDocumentDTO(final Integer userId, final Date initialDate, final Date finalDate);

    @Transactional
    void changeStatusById(Integer id);

    String getDocumentNameByDocumentId(int documentId);
}
