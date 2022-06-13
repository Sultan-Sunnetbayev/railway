package tm.salam.hazarLogistika.railway.services;

import tm.salam.hazarLogistika.railway.dtos.TypeVanDTO;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.List;

public interface TypeVanService {

    @Transactional
    ResponseTransfer addNewTypeVan(final TypeVanDTO typeVanDTO);

    @Transactional
    ResponseTransfer editTypeVanById(final TypeVanDTO typeVanDTO);

    @Transactional
    ResponseTransfer removeTypeVanById(final int id);

    List<TypeVanDTO> getAllTypeVanDTOS();

    TypeVanDTO getTypeVanDTOById(final int id);

    TypeVanDTO getTypeVanDTOByShortName(final String shortName);

    List<String>getAllFullNameTypeVan();
}
