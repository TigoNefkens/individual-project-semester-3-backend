package nl.fontys.imp_stagescout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.imp_stagescout.DTO.*;
import nl.fontys.imp_stagescout.business.BandServiceInterface;
import nl.fontys.imp_stagescout.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BandController.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
@AutoConfigureMockMvc(addFilters = false)
class BandControllerServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BandServiceInterface bandService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private nl.fontys.imp_stagescout.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String bearerToken = "Bearer test-token";

    @BeforeEach
    void setupMocks() {
        when(jwtUtil.extractEmail("test-token")).thenReturn("test@example.com");
    }

    @Test
    void addBand_shouldReturnOk_whenValidInput() throws Exception {
        CreateBandDTO dto = new CreateBandDTO();
        dto.setBandName("Test Band");
        dto.setCreatorUserId(1L);
        dto.setGenre("Rock");

        doNothing().when(bandService).addBand(any(CreateBandDTO.class));

        mockMvc.perform(post("/bands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully created band! , you can view it on your profile"));

        verify(bandService).addBand(any(CreateBandDTO.class));
    }

    @Test
    void getAllBands_shouldReturnListOfBands() throws Exception {
        BandDTO bandDto = new BandDTO(1L, "My Band", 1L, null, null, null, null, null);
        when(bandService.getAllBands()).thenReturn(List.of(bandDto));

        mockMvc.perform(get("/bands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bandName").value("My Band"));

        verify(bandService).getAllBands();
    }

    @Test
    void getBandsByCreator_shouldReturnBands() throws Exception {
        GetBandsByCreatorDTO dto = new GetBandsByCreatorDTO();
        dto.setUserId(1L);

        BandDTO bandDto = new BandDTO(1L, "Creator Band", 1L, null, null, null, null, null);
        when(bandService.getBandsByCreatorId(1L)).thenReturn(List.of(bandDto));

        mockMvc.perform(post("/bands/owned")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bandName").value("Creator Band"));

        verify(bandService).getBandsByCreatorId(1L);
    }

    @Test
    void getBandsForUser_shouldReturnBands() throws Exception {
        GetUserBandsDTO dto = new GetUserBandsDTO();
        dto.setUserId(1L);

        BandDTO bandDto = new BandDTO(1L, "User Band", 1L, null, null, null, null, null);
        when(bandService.getBandsForUser(1L)).thenReturn(List.of(bandDto));

        mockMvc.perform(post("/bands/member-of")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bandName").value("User Band"));

        verify(bandService).getBandsForUser(1L);
    }

    @Test
    void updateBand_shouldReturnUpdatedBand() throws Exception {
        UpdateBandDTO dto = new UpdateBandDTO();
        dto.setBandId(1L);
        dto.setLargedescription("Updated description");

        BandDTO updatedBand = new BandDTO(
                1L, "Band Name", 1L, null, null, null, null, "Updated description"
        );

        when(bandService.updateBand("test@example.com", dto)).thenReturn(updatedBand);

        mockMvc.perform(put("/bands")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.largedescription").value("Updated description"));

        verify(bandService).updateBand("test@example.com", dto);
    }
}