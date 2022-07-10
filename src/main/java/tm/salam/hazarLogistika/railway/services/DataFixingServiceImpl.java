package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.salam.hazarLogistika.railway.daos.DataFixingRepository;
import tm.salam.hazarLogistika.railway.dtos.DataFixingDTO;
import tm.salam.hazarLogistika.railway.models.DataFixing;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataFixingServiceImpl implements DataFixingService{

    private final DataFixingRepository dataFixingRepository;

    @Autowired
    public DataFixingServiceImpl(DataFixingRepository dataFixingRepository) {
        this.dataFixingRepository = dataFixingRepository;
    }

    @Override
    public List<DataFixingDTO>getAllDataFixingDTO(){

        return dataFixingRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DataFixingDTO toDTO(DataFixing dataFixing) {

        return DataFixingDTO.builder()
                .id(dataFixing.getId())
                .name(dataFixing.getName())
                .build();
    }

    @Override
    public Integer getIdByNameDataFixing(final String name){

        DataFixing dataFixing=dataFixingRepository.findDataFixingByName(name);

        if(dataFixing!=null) {

            return dataFixing.getId();
        }else{

            return null;
        }
    }

    @Override
    public String getNameDataFixingById(final Integer id){

        DataFixing dataFixing=dataFixingRepository.findDataFixingById(id);

        if(dataFixing==null) {

            return null;
        }else{

            return dataFixing.getName();
        }
    }

    @Override
    public DataFixing getDataFixingById(final Integer id){

        DataFixing dataFixing=dataFixingRepository.findDataFixingById(id);

        if(dataFixing==null){

            return null;
        }else{

            return dataFixing;
        }
    }

}
