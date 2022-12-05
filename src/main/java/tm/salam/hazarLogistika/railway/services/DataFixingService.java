package tm.salam.hazarLogistika.railway.services;

import tm.salam.hazarLogistika.railway.dtos.DataFixingDTO;
import tm.salam.hazarLogistika.railway.models.DataFixing;

import javax.transaction.Transactional;
import java.util.List;

public interface DataFixingService {

    List<DataFixingDTO> getAllDataFixingDTO();

    Integer getIdByNameDataFixing(final String name);

    String getNameDataFixingById(final Integer idDataFixing);

    DataFixing getDataFixingById(final Integer idDataFixing);

    @Transactional
    void addDataFixing(DataFixing dataFixing);

    boolean isDataFixingExists(DataFixing dataFixing);

    boolean isDataFixingExistsById(int dataFixingId);

    @Transactional
    boolean editDataFixingById(DataFixing dataFixing);

    @Transactional
    void removeDataFixingById(int dataFixingId);
}
