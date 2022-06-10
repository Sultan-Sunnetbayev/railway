package tm.salam.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.salam.railway.daos.TypeVanRepository;
import tm.salam.railway.dtos.TypeVanDTO;
import tm.salam.railway.helper.ResponseTransfer;
import tm.salam.railway.models.TypeVan;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeVanServiceImpl implements TypeVanService{

    private final TypeVanRepository typeVanRepository;

    @Autowired
    public TypeVanServiceImpl(TypeVanRepository typeVanRepository) {
        this.typeVanRepository = typeVanRepository;
    }

    @Override
    @Transactional
    public ResponseTransfer addNewTypeVan(final TypeVanDTO typeVanDTO){

        TypeVan typeVan=typeVanRepository.findTypeVanByShortName(typeVanDTO.getShortName());

        if(typeVan!=null){

            return new ResponseTransfer("short name type van already added",false);
        }
        typeVan=typeVanRepository.findTypeVanByFullName(typeVanDTO.getFullName());

        if(typeVan!=null){

            return new ResponseTransfer("full name type van already added",false);
        }

        typeVan= TypeVan.builder()
                .fullName(typeVanDTO.getFullName())
                .shortName(typeVan.getShortName())
                .build();

        typeVanRepository.save(typeVan);

        return new ResponseTransfer("new type van successful added",true);
    }

    @Override
    @Transactional
    public ResponseTransfer editTypeVanById(final TypeVanDTO typeVanDTO){

        TypeVan typeVan=typeVanRepository.findTypeVanById(typeVanDTO.getId());

        if(typeVan==null){

            return new ResponseTransfer("type not found with this id",false);
        }
        if(typeVanRepository.findTypeVanByFullName(typeVanDTO.getFullName())!=null ||
            typeVanRepository.findTypeVanByShortName(typeVanDTO.getShortName())!=null){

            return new ResponseTransfer("short name or full name edited type van already added",false);
        }
        typeVan.setShortName(typeVanDTO.getShortName());
        typeVan.setFullName(typeVanDTO.getFullName());

        if(typeVanRepository.findTypeVanByShortNameAndFullName(typeVanDTO.getShortName(),typeVanDTO.getFullName())!=null){

            return new ResponseTransfer("type van successful edited",true);
        }else{

            return new ResponseTransfer("type van don't edited",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer removeTypeVanById(final int id){

        TypeVan typeVan=typeVanRepository.findTypeVanById(id);

        if(typeVan==null){

            return new ResponseTransfer("type not found with this id",false);
        }
        typeVanRepository.deleteById(id);

        if(typeVanRepository.findTypeVanById(id)==null){

            return new ResponseTransfer("type van successful removed",true);
        }else{

            return new ResponseTransfer("type van don't removed",false);
        }
    }

    @Override
    public List<TypeVanDTO> getAllTypeVanDTOS(){

        return typeVanRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TypeVanDTO toDTO(TypeVan typeVan) {

        return TypeVanDTO.builder()
                .id(typeVan.getId())
                .shortName(typeVan.getShortName())
                .fullName(typeVan.getFullName())
                .build();
    }

    @Override
    public TypeVanDTO getTypeVanDTOById(final int id){

        TypeVan typeVan=typeVanRepository.findTypeVanById(id);

        if(typeVan==null){

            return null;
        }else{

            return toDTO(typeVan);
        }
    }

    @Override
    public TypeVanDTO getTypeVanDTOByShortName(final String shortName){

        TypeVan typeVan=typeVanRepository.findTypeVanByShortName(shortName);

        if(typeVan==null){

            return null;
        }else{

            return toDTO(typeVan);
        }
    }

}
