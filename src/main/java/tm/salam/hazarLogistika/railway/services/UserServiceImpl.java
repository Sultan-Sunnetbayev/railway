package tm.salam.hazarLogistika.railway.services;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tm.salam.hazarLogistika.railway.helper.FileUploadUtil;
import tm.salam.hazarLogistika.railway.helper.ResponseTransfer;
import tm.salam.hazarLogistika.railway.models.Role;
import tm.salam.hazarLogistika.railway.models.User;
import tm.salam.hazarLogistika.railway.daos.RoleRepository;
import tm.salam.hazarLogistika.railway.daos.UserRepository;
import tm.salam.hazarLogistika.railway.dtos.UserDTO;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private  RoleRepository roleRepository;
    private final String imagePath="src/main/resources/static/imageUsers";
    private final String defaultImagePath="src/main/resources/static/imageUsers/image.png";

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public ResponseTransfer addNewLogist(final UserDTO userDTO, MultipartFile image) throws IOException {

        if(userRepository.findUserByEmail(userDTO.getEmail())!=null){

            return new ResponseTransfer("email new logist already added",false);
        }
        List<Role> roles=new ArrayList<>();
        Role role=roleRepository.findRoleByName("ROLE_LOGIST");

        roles.add(role);

        User user=User.builder()
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(user);
        User savedUser=userRepository.findUserByEmail(userDTO.getEmail());

        if(savedUser!=null){

            String uuid= UUID.randomUUID().toString();
            String fileName="";

            if(image!=null && !image.isEmpty()) {

                fileName = uuid + "_" + image.getOriginalFilename();
                try {

                    FileUploadUtil.saveFile(imagePath, fileName, image);
                    savedUser.setImagePath(imagePath + "/" + fileName);

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }else{

                File defaultImage=new File(defaultImagePath);
                fileName=uuid+"_"+"image.png";
                Path path=Paths.get(imagePath+"/"+fileName);
                Files.createFile(path);

                if(defaultImage.exists()){

                    File file=new File(imagePath+"/"+fileName);
                    FileUtils.copyFile(defaultImage,file);
                    savedUser.setImagePath(imagePath + "/" + fileName);

                }
            }

            return new ResponseTransfer("Logist successful added",true);
        }else{

            return new ResponseTransfer("Logist don't added",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer removeLogistById(final int id) throws Exception {

        User logist=userRepository.findUserById(id);
        String imagePath="";
        if(logist==null){

            return new ResponseTransfer("logist not found with this id",false);
        }else{

            for(Role role:logist.getRoles()){

                if(Objects.equals(role.getName(),"ROLE_ADMIN")){

                    throw new Exception("delete admin impossible");
                }
            }
            imagePath=logist.getImagePath();
            userRepository.deleteById(id);
        }
        User check=userRepository.findUserById(id);

        if(check==null){

            if(imagePath!=null) {
                File file = new File(imagePath);

                if (file.exists()) {

                    file.delete();
                }
            }
            return new ResponseTransfer("logist successful removed",true);
        }else{

            return new ResponseTransfer("logist dont' removed",false);
        }
    }

    @Override
    public List<UserDTO> getAllUserDTO(){

        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private UserDTO toDTO(final User user) {

        final List<Role>roles=user.getRoles();
        List<String>nameRoles=new ArrayList<>();

        for(final Role role:roles){

            nameRoles.add(role.getName());
        }

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .imagePath(user.getImagePath())
                .roles(nameRoles)
                .build();
    }

    @Override
    public UserDTO getUserDTOById(final int id){

        User user=userRepository.findUserById(id);

        if(user==null){

            return null;
        }else{

            return toDTO(user);
        }
    }

    @Override
    public User getUserByEmail(final String email){

        User user=userRepository.findUserByEmail(email);

        return user;
    }

    @Override
    @Transactional
    public ResponseTransfer editProfile(final UserDTO userDTO, final MultipartFile image){

        User user=userRepository.findUserById(userDTO.getId());

        if(user==null){

            return new ResponseTransfer("user not found",false);
        }
        if(userDTO!=null) {
            if (userDTO.getName() != null && !userDTO.getName().isEmpty()) {
                user.setName(userDTO.getName());
            }
            if (userDTO.getSurname() != null && !userDTO.getSurname().isEmpty()) {
                user.setSurname(userDTO.getSurname());
            }
            if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
                user.setEmail(userDTO.getEmail());
            }
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
        }
        if(image!=null && !image.isEmpty()){

            if(user.getImagePath()!=null){

                File file=new File(user.getImagePath());

                if(file.exists()){

                    file.delete();
                }
            }
            String uuid= UUID.randomUUID().toString();
            String fileName=uuid+"_"+image.getOriginalFilename();

            try {

                FileUploadUtil.saveFile(imagePath,fileName,image);
                user.setImagePath(imagePath+"/"+fileName);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if(userRepository.findUserByEmail(user.getEmail())!=null){

            return new ResponseTransfer("profile user successful edited ",true);
        }else{

            return new ResponseTransfer("profile user don't edited",false);
        }
    }

    @Override
    public UserDTO getUserDTOByEmail(final String email){

        User user=userRepository.findUserByEmail(email);

        if(user==null){

            return null;
        }else{

            return toDTO(user);
        }
    }

    @Override
    public List<UserDTO>getAllLogistDTO(){

        Role role=roleRepository.findRoleByName("ROLE_LOGIST");
        List<User>users=userRepository.findUsersByRoles(role);
        List<UserDTO>userDTOS=new ArrayList<>();

        users.forEach(user -> {
            userDTOS.add(toDTO(user));
        });

        return userDTOS;
    }

    @Override
    public User getUserById(final Integer id){

        User user=userRepository.findUserById(id);

        return user;
    }

}
