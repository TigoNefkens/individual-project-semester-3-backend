package nl.fontys.imp_stagescout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.imp_stagescout.DTO.*;
import nl.fontys.imp_stagescout.business.ApplicationServiceInterface;
import nl.fontys.imp_stagescout.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
@AutoConfigureMockMvc(addFilters = false)
class ApplicationControllerServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationServiceInterface applicationService;

    @MockitoBean
    private nl.fontys.imp_stagescout.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final String bearerToken = "Bearer fake-token";

    @BeforeEach
    void setupMocks() {
        // Reset mocks
        reset(applicationService, jwtUtil);
        // Mock JWT email extraction
        when(jwtUtil.extractEmail("fake-token")).thenReturn("testuser@example.com");
    }



}