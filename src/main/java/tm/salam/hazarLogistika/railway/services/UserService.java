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
    ResponseTransfer removeLogistById(final int id) throws Exception;

    List<UserDTO> getAllUserDTO();

    User getUserByEmail(final String email);

    @Transactional
    ResponseTransfer editProfile(final UserDTO userDTO,final int id, final MultipartFile image) throws IOException;

    UserDTO getUserDTOById(final int id);

    UserDTO getUserDTOByEmail(final String email);
}
