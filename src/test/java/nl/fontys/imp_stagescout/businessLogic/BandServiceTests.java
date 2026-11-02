package nl.fontys.imp_stagescout.businessLogic;

import nl.fontys.imp_stagescout.DTO.BandDTO;
import nl.fontys.imp_stagescout.DTO.CreateBandDTO;
import nl.fontys.imp_stagescout.DTO.UpdateBandDTO;
import nl.fontys.imp_stagescout.business.BandService;
import nl.fontys.imp_stagescout.domain.Band;
import nl.fontys.imp_stagescout.domain.User;
import nl.fontys.imp_stagescout.persistence.BandRepo;
import nl.fontys.imp_stagescout.persistence.UserRepo;
import org.junit.jupiter.api.BeforeEach;
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
class BandServiceTests {

    @Mock
    private BandRepo bandRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private BandService bandService;

    private CreateBandDTO createBandDTO;

    @BeforeEach
    void setUp() {
        createBandDTO = new CreateBandDTO();
        createBandDTO.setBandName("Test Band");
        createBandDTO.setCreatorUserId(1L);
        createBandDTO.setPlaceOfOperations("Eindhoven");
        createBandDTO.setGenre("Rock");
        createBandDTO.setDescription("Test description");
    }

    // ---------------- ADD BAND ----------------

    @Test
    void addBand_shouldSaveBand_whenValidInput() {
        assertDoesNotThrow(() -> bandService.addBand(createBandDTO));
        verify(bandRepo, times(1)).save(any(Band.class));
    }

    @Test
    void addBand_shouldThrowException_whenBandNameIsNull() {
        createBandDTO.setBandName(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bandService.addBand(createBandDTO)
        );

        assertEquals("Band name is required", exception.getMessage());
    }

    @Test
    void addBand_shouldThrowException_whenBandNameIsBlank() {
        createBandDTO.setBandName("   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bandService.addBand(createBandDTO)
        );

        assertEquals("Band name is required", exception.getMessage());
    }

    // ---------------- GET ALL BANDS ----------------

    @Test
    void getAllBands_shouldReturnBandDTOList() {
        Band band = new Band();
        band.setBandId(1L);
        band.setBandName("Band 1");

        when(bandRepo.findAll()).thenReturn(List.of(band));

        List<BandDTO> result = bandService.getAllBands();

        assertEquals(1, result.size());
        assertEquals("Band 1", result.get(0).getBandName());
    }

    @Test
    void getAllBands_shouldReturnEmptyList_whenNoBandsExist() {
        when(bandRepo.findAll()).thenReturn(new ArrayList<>());

        List<BandDTO> result = bandService.getAllBands();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------- GET BANDS BY CREATOR ----------------

    @Test
    void getBandsByCreatorId_shouldReturnBands() {
        Band band = new Band();
        band.setBandId(1L);
        band.setCreatorUserId(1L);

        when(bandRepo.findByCreatorUserId(1L)).thenReturn(List.of(band));

        List<BandDTO> result = bandService.getBandsByCreatorId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getCreatorUserId());
    }

    @Test
    void getBandsByCreatorId_shouldReturnEmptyList_whenNoneFound() {
        when(bandRepo.findByCreatorUserId(1L)).thenReturn(new ArrayList<>());

        List<BandDTO> result = bandService.getBandsByCreatorId(1L);

        assertTrue(result.isEmpty());
    }

    // ---------------- GET BANDS FOR USER ----------------

    @Test
    void getBandsForUser_shouldReturnBands_whenUserExists() {
        User user = new User();
        Band band = new Band();
        band.setBandId(1L);
        band.setBandName("User Band");

        user.setBands(List.of(band));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        List<BandDTO> result = bandService.getBandsForUser(1L);

        assertEquals(1, result.size());
        assertEquals("User Band", result.get(0).getBandName());
    }

    @Test
    void getBandsForUser_shouldReturnEmptyList_whenUserNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        List<BandDTO> result = bandService.getBandsForUser(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ---------------- UPDATE BAND ----------------

    @Test
    void updateBand_shouldUpdateBand_whenBandExists() {
        Band band = new Band();
        band.setBandId(1L);
        band.setBandName("Old Name");

        when(bandRepo.findById(1L)).thenReturn(Optional.of(band));
        when(bandRepo.save(any(Band.class))).thenReturn(band);

        UpdateBandDTO dto = new UpdateBandDTO();
        dto.setBandId(1L);
        dto.setGenre("Metal");

        BandDTO result = bandService.updateBand("test@test.com", dto);

        assertNotNull(result);
        verify(bandRepo, times(1)).save(band);
    }

    @Test
    void updateBand_shouldThrowException_whenBandNotFound() {
        UpdateBandDTO dto = new UpdateBandDTO();
        dto.setBandId(1L);

        when(bandRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                bandService.updateBand("test@test.com", dto)
        );

        assertEquals("Band not found", exception.getMessage());
    }
}

