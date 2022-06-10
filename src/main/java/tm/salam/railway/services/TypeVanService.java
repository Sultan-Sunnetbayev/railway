package tm.salam.railway.services;

import tm.salam.railway.dtos.TypeVanDTO;
import tm.salam.railway.helper.ResponseTransfer;

import javax.transaction.Transactional;
import java.util.List;

public interface TypeVanService {

    @Transactional
    ResponseTransfer addNewTypeVan(TypeVanDTO typeVanDTO);

    @Transactional
    ResponseTransfer editTypeVanById(TypeVanDTO typeVanDTO);

    @Transactional
    ResponseTransfer removeTypeVanById(int id);

    List<TypeVanDTO> getAllTypeVanDTOS();

    TypeVanDTO getTypeVanDTOById(int id);

    TypeVanDTO getTypeVanDTOByShortName(String shortName);
}
