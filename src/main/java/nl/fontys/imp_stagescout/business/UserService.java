package nl.fontys.imp_stagescout.business;

import nl.fontys.imp_stagescout.DTO.BandDTO;
import nl.fontys.imp_stagescout.DTO.MemberDTO;
import nl.fontys.imp_stagescout.DTO.RegisterUserDTO;
import nl.fontys.imp_stagescout.DTO.UpdateProfileDTO;
import nl.fontys.imp_stagescout.domain.User;
import nl.fontys.imp_stagescout.persistence.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = new BCryptPasswordEncoder(); //freaky Bcrypt
    }

    @Override
    public void addUser(RegisterUserDTO registerUserDTO) throws Exception {

        boolean emailExists = userRepo.existsByEmail(registerUserDTO.getEmail());
        boolean usernameExists = userRepo.existsByUsername(registerUserDTO.getUsername());

        if (emailExists && usernameExists) {
            throw new Exception("Both email and username are already taken.");
        } else if (emailExists) {
            throw new Exception("Email is already being used with another account.");
        } else if (usernameExists) {
            throw new Exception("Username is already taken.");
        }

        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setEmail(registerUserDTO.getEmail());

        String hashed = passwordEncoder.encode(registerUserDTO.getPassword());
        user.setHashedPassword(hashed);

        user.setPhoneNumber(registerUserDTO.getPhoneNumber());
        user.setNationality(registerUserDTO.getNationality());
        user.setPlaceOfResidence(registerUserDTO.getPlaceOfResidence());
        user.setProfileConfig(registerUserDTO.getProfileConfig());
        user.setExperience(registerUserDTO.getExperience());
        user.setDescription(registerUserDTO.getDescription());

        User savedUser = userRepo.save(user);

        if (savedUser.getUserId() == null) {
            throw new Exception("Could not create account due to an error.");
        }
    }

    @Override
    public User login(String email, String password) {
        User user = userRepo.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getHashedPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<MemberDTO> getBandMembers(BandDTO bandDTO) {
        return userRepo.findByBands_BandId(bandDTO.getBandId())
                .stream()
                .map(user -> new MemberDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getNationality(),
                        user.getPlaceOfResidence(),
                        user.getProfileConfig(),
                        user.getExperience(),
                        user.getDescription()
                ))
                .toList();
    }

    public MemberDTO findCreatorByID(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new MemberDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getNationality(),
                user.getPlaceOfResidence(),
                user.getProfileConfig(),
                user.getExperience(),
                user.getDescription()
        );
    }

    @Override
    public void updateProfile(String email, UpdateProfileDTO dto) {
        User user = userRepo.findByEmail(email);
        if (user == null) throw new RuntimeException("User not found");

        if (dto.getDescription() != null) user.setDescription(dto.getDescription());
        if (dto.getPlaceOfResidence() != null) user.setPlaceOfResidence(dto.getPlaceOfResidence());
        if (dto.getExperience() != null) user.setExperience(dto.getExperience());
        if (dto.getProfileConfig() != null) user.setProfileConfig(dto.getProfileConfig());

        userRepo.save(user);
    }

}