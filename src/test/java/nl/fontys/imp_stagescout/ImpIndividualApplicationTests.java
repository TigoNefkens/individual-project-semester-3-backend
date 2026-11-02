package nl.fontys.imp_stagescout;

import nl.fontys.imp_stagescout.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootTest
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class ImpIndividualApplicationTests {

    @Configuration
    static class TestConfig {

        // Mock JWT filter
        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return Mockito.mock(JwtAuthenticationFilter.class);
        }

        // Mock UserDetailsService (Spring Security)
        @Bean
        public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
            return Mockito.mock(org.springframework.security.core.userdetails.UserDetailsService.class);
        }

        // Mock andere services als je wilt dat de context volledig laadt
        @Bean
        public nl.fontys.imp_stagescout.business.UserServiceInterface userService() {
            return Mockito.mock(nl.fontys.imp_stagescout.business.UserServiceInterface.class);
        }

        @Bean
        public nl.fontys.imp_stagescout.business.BandServiceInterface bandService() {
            return Mockito.mock(nl.fontys.imp_stagescout.business.BandServiceInterface.class);
        }

        @Bean
        public nl.fontys.imp_stagescout.business.ApplicationServiceInterface applicationService() {
            return Mockito.mock(nl.fontys.imp_stagescout.business.ApplicationServiceInterface.class);
        }
    }

    @Test
    void contextLoads() {
        // Niets nodig, als de context start is de test geslaagd
    }
}