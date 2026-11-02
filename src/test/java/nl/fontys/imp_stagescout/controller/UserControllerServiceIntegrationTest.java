package nl.fontys.imp_stagescout.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.fontys.imp_stagescout.DTO.LoginUserDTO;
import nl.fontys.imp_stagescout.DTO.RegisterUserDTO;
import nl.fontys.imp_stagescout.business.UserServiceInterface;
import nl.fontys.imp_stagescout.domain.User;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceInterface userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final String bearerToken = "Bearer test-token";

    @BeforeEach
    void setupMocks() {
        // Mock de extractEmail methode van JwtUtil zoals jouw project gebruikt
        when(jwtUtil.extractEmail("test-token")).thenReturn("testuser@example.com");
    }

    @Test
    void addUser_shouldCallServiceAndReturnOk() throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername("testuser");
        dto.setEmail("test@test.com");
        dto.setPassword("password123");

        doNothing().when(userService).addUser(any(RegisterUserDTO.class));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Your account has been created"));

        verify(userService).addUser(any(RegisterUserDTO.class));
    }

}