package nl.fontys.imp_stagescout.businessLogic;

import nl.fontys.imp_stagescout.DTO.BandDTO;
import nl.fontys.imp_stagescout.DTO.MemberDTO;
import nl.fontys.imp_stagescout.DTO.RegisterUserDTO;
import nl.fontys.imp_stagescout.DTO.UpdateProfileDTO;
import nl.fontys.imp_stagescout.business.UserService;
import nl.fontys.imp_stagescout.domain.User;
import nl.fontys.imp_stagescout.persistence.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private RegisterUserDTO registerUserDTO;

    @BeforeEach
    void setUp() {
        registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("testuser");
        registerUserDTO.setEmail("test@test.com");
        registerUserDTO.setPassword("password123");
        registerUserDTO.setPhoneNumber("0612345678");
        registerUserDTO.setNationality("NL");
        registerUserDTO.setPlaceOfResidence("Eindhoven");
        registerUserDTO.setProfileConfig("default");
        registerUserDTO.setExperience(2);
        registerUserDTO.setDescription("Test description");
    }

    // ---------------- ADD USER ----------------

    @Test
    void addUser_shouldThrowException_whenEmailAndUsernameExist() {
        when(userRepo.existsByEmail(registerUserDTO.getEmail())).thenReturn(true);
        when(userRepo.existsByUsername(registerUserDTO.getUsername())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () ->
                userService.addUser(registerUserDTO)
        );

        assertEquals("Both email and username are already taken.", exception.getMessage());
    }

    @Test
    void addUser_shouldThrowException_whenEmailExists() {
        when(userRepo.existsByEmail(registerUserDTO.getEmail())).thenReturn(true);
        when(userRepo.existsByUsername(registerUserDTO.getUsername())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () ->
                userService.addUser(registerUserDTO)
        );

        assertEquals("Email is already being used with another account.", exception.getMessage());
    }

    @Test
    void addUser_shouldThrowException_whenUsernameExists() {
        when(userRepo.existsByEmail(registerUserDTO.getEmail())).thenReturn(false);
        when(userRepo.existsByUsername(registerUserDTO.getUsername())).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () ->
                userService.addUser(registerUserDTO)
        );

        assertEquals("Username is already taken.", exception.getMessage());
    }

    @Test
    void addUser_shouldSaveUser_whenValidInput() {
        when(userRepo.existsByEmail(registerUserDTO.getEmail())).thenReturn(false);
        when(userRepo.existsByUsername(registerUserDTO.getUsername())).thenReturn(false);

        User savedUser = new User();
        savedUser.setUserId(1L);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        assertDoesNotThrow(() -> userService.addUser(registerUserDTO));
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void addUser_shouldThrowException_whenSaveFails() {
        when(userRepo.existsByEmail(registerUserDTO.getEmail())).thenReturn(false);
        when(userRepo.existsByUsername(registerUserDTO.getUsername())).thenReturn(false);

        User savedUser = new User(); // userId = null
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        Exception exception = assertThrows(Exception.class, () ->
                userService.addUser(registerUserDTO)
        );

        assertEquals("Could not create account due to an error.", exception.getMessage());
    }

    // ---------------- LOGIN ----------------

    @Test
    void login_shouldReturnUser_whenCredentialsAreCorrect() {
        User user = new User();
        user.setEmail(registerUserDTO.getEmail());
        user.setHashedPassword(
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                        .encode("password123")
        );

        when(userRepo.findByEmail(registerUserDTO.getEmail())).thenReturn(user);

        User result = userService.login(registerUserDTO.getEmail(), "password123");

        assertNotNull(result);
    }

    @Test
    void login_shouldReturnNull_whenPasswordIsIncorrect() {
        User user = new User();
        user.setEmail(registerUserDTO.getEmail());
        user.setHashedPassword(
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                        .encode("correctpassword")
        );

        when(userRepo.findByEmail(registerUserDTO.getEmail())).thenReturn(user);

        User result = userService.login(registerUserDTO.getEmail(), "wrongpassword");

        assertNull(result);
    }

    @Test
    void login_shouldReturnNull_whenUserNotFound() {
        when(userRepo.findByEmail(registerUserDTO.getEmail())).thenReturn(null);

        User result = userService.login(registerUserDTO.getEmail(), "password123");

        assertNull(result);
    }

    // ---------------- FIND BY EMAIL ----------------

    @Test
    void findByEmail_shouldReturnUser_whenUserExists() {
        User user = new User();
        when(userRepo.findByEmail("test@test.com")).thenReturn(user);

        User result = userService.findByEmail("test@test.com");

        assertNotNull(result);
    }

    // ---------------- GET BAND MEMBERS ----------------

    @Test
    void getBandMembers_shouldReturnMemberDTOList() {
        BandDTO bandDTO = new BandDTO();
        bandDTO.setBandId(1L);

        User user = new User();
        user.setUserId(1L);
        user.setUsername("member");
        user.setEmail("member@test.com");

        when(userRepo.findByBands_BandId(1L)).thenReturn(List.of(user));

        List<MemberDTO> result = userService.getBandMembers(bandDTO);

        assertEquals(1, result.size());
        assertEquals("member", result.get(0).getUsername());
    }

    // ---------------- FIND CREATOR ----------------

    @Test
    void findCreatorByID_shouldReturnMemberDTO_whenUserExists() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("creator");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        MemberDTO result = userService.findCreatorByID(1L);

        assertEquals("creator", result.getUsername());
    }

    @Test
    void findCreatorByID_shouldThrowException_whenUserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.findCreatorByID(1L)
        );

        assertEquals("User not found", exception.getMessage());
    }

    // ---------------- UPDATE PROFILE ----------------

    @Test
    void updateProfile_shouldUpdateUser_whenUserExists() {
        User user = new User();
        when(userRepo.findByEmail("test@test.com")).thenReturn(user);

        UpdateProfileDTO dto = new UpdateProfileDTO();
        dto.setDescription("Updated description");

        assertDoesNotThrow(() ->
                userService.updateProfile("test@test.com", dto)
        );

        verify(userRepo, times(1)).save(user);
        assertEquals("Updated description", user.getDescription());
    }

    @Test
    void updateProfile_shouldThrowException_whenUserNotFound() {
        when(userRepo.findByEmail("test@test.com")).thenReturn(null);

        UpdateProfileDTO dto = new UpdateProfileDTO();

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.updateProfile("test@test.com", dto)
        );

        assertEquals("User not found", exception.getMessage());
    }
}
