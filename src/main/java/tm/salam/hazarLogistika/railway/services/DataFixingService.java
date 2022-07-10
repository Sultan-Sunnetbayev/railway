package tm.salam.hazarLogistika.railway.services;

import tm.salam.hazarLogistika.railway.dtos.DataFixingDTO;
import tm.salam.hazarLogistika.railway.models.DataFixing;

import java.util.List;

public interface DataFixingService {

    List<DataFixingDTO> getAllDataFixingDTO();

    Integer getIdByNameDataFixing(final String name);

    String getNameDataFixingById(final Integer idDataFixing);

    DataFixing getDataFixingById(final Integer idDataFixing);
}
