package tm.salam.hazarLogistika.railway.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tm.salam.hazarLogistika.railway.daos.DataFixingRepository;
import tm.salam.hazarLogistika.railway.dtos.DataFixingDTO;
import tm.salam.hazarLogistika.railway.models.DataFixing;

import javax.transaction.Transactional;
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

    @Override
    @Transactional
    public void addDataFixing(final DataFixing dataFixing){

        if(dataFixingRepository.findDataFixingByName(dataFixing.getName())!=null){

            return;
        }
        DataFixing savedDataFixing=DataFixing.builder()
                .name(dataFixing.getName())
                .build();
        dataFixingRepository.save(savedDataFixing);

        return;
    }

    @Override
    public boolean isDataFixingExists(final DataFixing dataFixing){

        if(dataFixingRepository.findDataFixingByName(dataFixing.getName())!=null){

            return true;
        }else{

            return false;
        }
    }

    @Override
    public boolean isDataFixingExistsById(final int dataFixingId){

        return dataFixingRepository.isDataFixingExistsById(dataFixingId);
    }

    @Override
    @Transactional
    public boolean editDataFixingById(final DataFixing dataFixing){

        DataFixing editedDataFixing=dataFixingRepository.findDataFixingById(dataFixing.getId());

        if(editedDataFixing==null){

            return false;
        }
        editedDataFixing.setName(dataFixing.getName());
        dataFixingRepository.save(editedDataFixing);

        return true;
    }

    @Override
    @Transactional
    public void removeDataFixingById(final int dataFixingId){

        if(dataFixingRepository.findDataFixingById(dataFixingId)!=null){

            dataFixingRepository.removeDataFixingById(dataFixingId);
        }

        return;
    }

}
