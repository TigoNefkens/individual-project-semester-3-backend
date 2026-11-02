package nl.fontys.imp_stagescout.business;
import nl.fontys.imp_stagescout.DTO.BandDTO;
import nl.fontys.imp_stagescout.DTO.MemberDTO;
import nl.fontys.imp_stagescout.DTO.UpdateProfileDTO;
import nl.fontys.imp_stagescout.domain.User;
import nl.fontys.imp_stagescout.DTO.RegisterUserDTO;

import java.util.List;

public interface UserServiceInterface {
    void addUser(RegisterUserDTO registerUserDTO) throws Exception;
    User login(String email, String password);
    User findByEmail(String email);
    MemberDTO findCreatorByID(Long userID);
    List<MemberDTO> getBandMembers(BandDTO bandDTO);
    public void updateProfile(String email, UpdateProfileDTO dto);
}
