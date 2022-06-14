package tm.salam.hazarLogistika.railway.services;

import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;
import tm.salam.hazarLogistika.railway.models.User;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

public interface UserService{

    @Transactional
    ResponseTransfer addNewLogist(final UserDTO userDTO, final MultipartFile image) throws IOException;

    @Transactional
    ResponseTransfer removeLogistById(int id) throws Exception;

    List<UserDTO> getAllUserDTO();

    User getUserByEmail(String email);

    @Transactional
    ResponseTransfer editProfile(UserDTO userDTO,int id, MultipartFile image) throws IOException;

    UserDTO getUserDTOById(int id);

    UserDTO getUserDTOByEmail(String email);
}
