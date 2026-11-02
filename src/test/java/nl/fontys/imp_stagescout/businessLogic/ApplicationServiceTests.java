package nl.fontys.imp_stagescout.businessLogic;

import jakarta.persistence.EntityNotFoundException;
import nl.fontys.imp_stagescout.DTO.AddApplicationResponseDTO;
import nl.fontys.imp_stagescout.DTO.ApplicationDTO;
import nl.fontys.imp_stagescout.business.ApplicationService;
import nl.fontys.imp_stagescout.domain.Application;
import nl.fontys.imp_stagescout.domain.Band;
import nl.fontys.imp_stagescout.domain.User;
import nl.fontys.imp_stagescout.exception.ConflictException;
import nl.fontys.imp_stagescout.persistence.ApplicationRepo;
import nl.fontys.imp_stagescout.persistence.BandRepo;
import nl.fontys.imp_stagescout.persistence.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTests {

    @Mock
    private ApplicationRepo applicationRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private BandRepo bandRepo;

    @InjectMocks
    private ApplicationService applicationService;

    // ---------------- CREATE APPLICATION ----------------

    @Test
    void createApplication_shouldThrowException_whenBandNotFound() {
        ApplicationDTO dto = new ApplicationDTO(null, 1L, 2L, "content");

        when(bandRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                applicationService.createApplication(dto)
        );
    }

    @Test
    void createApplication_shouldThrowException_whenCreatorAppliesToOwnBand() {
        Band band = new Band();
        band.setBandId(1L);
        band.setCreatorUserId(2L);

        ApplicationDTO dto = new ApplicationDTO(null, 1L, 2L, "content");

        when(bandRepo.findById(1L)).thenReturn(Optional.of(band));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                applicationService.createApplication(dto)
        );

        assertEquals("Band creator cannot apply to their own band", exception.getMessage());
    }

    @Test
    void createApplication_shouldThrowException_whenUserAlreadyMember() {
        Band band = new Band();
        band.setBandId(1L);
        band.setCreatorUserId(99L);

        ApplicationDTO dto = new ApplicationDTO(null, 1L, 2L, "content");

        when(bandRepo.findById(1L)).thenReturn(Optional.of(band));
        when(userRepo.isUserMemberOfBand(2L, 1L)).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                applicationService.createApplication(dto)
        );

        assertEquals("User is already a member of this band", exception.getMessage());
    }

    @Test
    void createApplication_shouldThrowException_whenUserAlreadyApplied() {
        Band band = new Band();
        band.setBandId(1L);
        band.setCreatorUserId(99L);

        ApplicationDTO dto = new ApplicationDTO(null, 1L, 2L, "content");

        when(bandRepo.findById(1L)).thenReturn(Optional.of(band));
        when(userRepo.isUserMemberOfBand(2L, 1L)).thenReturn(false);
        when(applicationRepo.existsByBandIdAndApplicantUserId(1L, 2L)).thenReturn(true);

        ConflictException exception = assertThrows(ConflictException.class, () ->
                applicationService.createApplication(dto)
        );

        assertEquals("User already applied to this band", exception.getMessage());
    }

    @Test
    void createApplication_shouldCreateApplication_whenValid() {
        Band band = new Band();
        band.setBandId(1L);
        band.setCreatorUserId(99L);

        ApplicationDTO dto = new ApplicationDTO(null, 1L, 2L, "content");

        Application saved = new Application();
        saved.setApplicationId(10L);
        saved.setBandId(1L);
        saved.setApplicantUserId(2L);
        saved.setContent("content");

        when(bandRepo.findById(1L)).thenReturn(Optional.of(band));
        when(userRepo.isUserMemberOfBand(2L, 1L)).thenReturn(false);
        when(applicationRepo.existsByBandIdAndApplicantUserId(1L, 2L)).thenReturn(false);
        when(applicationRepo.save(any(Application.class))).thenReturn(saved);

        AddApplicationResponseDTO response = applicationService.createApplication(dto);

        assertTrue(response.isSuccess());
        assertEquals(1L, response.getApplication().getBandId());
        verify(applicationRepo, times(1)).save(any(Application.class));
    }

    // ---------------- GET APPLICATIONS ----------------

    @Test
    void getApplicationsByBand_shouldReturnApplications() {
        Application app = new Application();
        app.setApplicationId(1L);
        app.setBandId(1L);
        app.setApplicantUserId(2L);
        app.setContent("test");

        when(applicationRepo.findByBandId(1L)).thenReturn(List.of(app));

        List<ApplicationDTO> result = applicationService.getApplicationsByBand(1L);

        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getContent());
    }

    @Test
    void getApplicationsByBand_shouldReturnEmptyList_whenNoneExist() {
        when(applicationRepo.findByBandId(1L)).thenReturn(new ArrayList<>());

        List<ApplicationDTO> result = applicationService.getApplicationsByBand(1L);

        assertTrue(result.isEmpty());
    }

    // ---------------- DENY APPLICATION ----------------

    @Test
    void denyApplication_shouldDeleteApplication_whenFound() {
        Application app = new Application();
        app.setApplicationId(1L);

        when(applicationRepo.findById(1L)).thenReturn(Optional.of(app));

        assertDoesNotThrow(() -> applicationService.denyApplication(1L));
        verify(applicationRepo, times(1)).delete(app);
    }

    @Test
    void denyApplication_shouldThrowException_whenNotFound() {
        when(applicationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                applicationService.denyApplication(1L)
        );
    }

    // ---------------- ACCEPT APPLICATION ----------------

    @Test
    void acceptApplication_shouldAddUserToBand_whenNotMember() {
        Application app = new Application();
        app.setApplicationId(1L);
        app.setApplicantUserId(2L);
        app.setBandId(3L);

        User user = new User();
        user.setUserId(2L);
        user.setBands(new ArrayList<>());

        Band band = new Band();
        band.setBandId(3L);

        when(applicationRepo.findById(1L)).thenReturn(Optional.of(app));
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));
        when(bandRepo.findById(3L)).thenReturn(Optional.of(band));
        when(userRepo.isUserMemberOfBand(2L, 3L)).thenReturn(false);

        applicationService.acceptApplication(1L);

        assertEquals(1, user.getBands().size());
        verify(userRepo, times(1)).save(user);
        verify(applicationRepo, times(1)).deleteById(1L);
    }

    @Test
    void acceptApplication_shouldOnlyDeleteApplication_whenUserAlreadyMember() {
        Application app = new Application();
        app.setApplicationId(1L);
        app.setApplicantUserId(2L);
        app.setBandId(3L);

        User user = new User();
        user.setUserId(2L);
        user.setBands(new ArrayList<>());

        Band band = new Band();
        band.setBandId(3L);

        when(applicationRepo.findById(1L)).thenReturn(Optional.of(app));
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));
        when(bandRepo.findById(3L)).thenReturn(Optional.of(band));
        when(userRepo.isUserMemberOfBand(2L, 3L)).thenReturn(true);

        applicationService.acceptApplication(1L);

        verify(userRepo, never()).save(any());
        verify(applicationRepo, times(1)).deleteById(1L);
    }

    @Test
    void acceptApplication_shouldThrowException_whenApplicationNotFound() {
        when(applicationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                applicationService.acceptApplication(1L)
        );
    }
}
