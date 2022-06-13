package tm.salam.hazarLogistika.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

public interface UserService {

    @Transactional
    ResponseTransfer addNewLogist(final UserDTO userDTO, final MultipartFile image) throws IOException;

    @Transactional
    ResponseTransfer removeLogistById(int id) throws Exception;

    List<UserDTO> getAllUserDTO();

    UserDTO getUserById(int id);
}
