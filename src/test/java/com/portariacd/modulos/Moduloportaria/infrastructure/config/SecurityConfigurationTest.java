//package com.portariacd.modulos.Moduloportaria.infrastructure.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = SecurityConfigurationTest.TestController.class)
//@Import(SecurityConfiguration.class)
//class SecurityConfigurationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private SecurityFilterChain securityFilterChain;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @MockBean
//    private FiltroValidation filtroValidation;
//
//    @Test
//    void devePermitirLoginSemAutenticacao() throws Exception {
//        mockMvc.perform(post("/portaria/v1/usuario/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void devePermitirEndpointsGetPublicosSemAutenticacao() throws Exception {
//        mockMvc.perform(get("/portaria/v1/entrada/123"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void deveExigirAutenticacaoParaRotasNaoPublicas() throws Exception {
//        mockMvc.perform(get("/rota-protegida"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void deveExigirAutenticacaoParaMetodoNaoPermitidoEmRotaPublica() throws Exception {
//        mockMvc.perform(post("/portaria/v1/avatar/123"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void deveRegistrarFiltroDeValidacaoAntesDoFiltroDeAutenticacao() {
//        assertThat(securityFilterChain.getFilters())
//                .contains(filtroValidation);
//
//        int validationIndex = securityFilterChain.getFilters().indexOf(filtroValidation);
//        int authenticationIndex = securityFilterChain.getFilters().stream()
//                .map(Object::getClass)
//                .toList()
//                .indexOf(AnonymousAuthenticationFilter.class);
//
//        assertThat(validationIndex).isLessThan(authenticationIndex);
//    }
//
//    @Test
//    void deveConfigurarObjectMapperParaDatasISO8601() throws Exception {
//        assertThat(objectMapper.getRegisteredModuleIds())
//                .contains(JavaTimeModule.class.getName());
//        assertThat(objectMapper.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))
//                .isFalse();
//        assertThat(objectMapper.writeValueAsString(LocalDate.of(2026, 8, 24)))
//                .isEqualTo("\"2026-08-24\"");
//    }
//
//    @Test
//    void deveCriarPasswordEncoderQueGeraHashBCrypt() {
//        String senha = "senha-segura";
//        String hash = passwordEncoder.encode(senha);
//
//        assertThat(hash).startsWith("$2");
//        assertThat(passwordEncoder.matches(senha, hash)).isTrue();
//        assertThat(passwordEncoder.matches("senha-incorreta", hash)).isFalse();
//    }
//
//    @Test
//    void deveDelegarCriacaoDoAuthenticationManager() throws Exception {
//        AuthenticationManager expected = mock(AuthenticationManager.class);
//        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
//        when(configuration.getAuthenticationManager()).thenReturn(expected);
//
//        AuthenticationManager actual = new SecurityConfiguration(filtroValidation)
//                .authenticationManager(configuration);
//
//        assertThat(actual).isSameAs(expected);
//    }
//
//    @RestController
//    static class TestController {
//        @PostMapping("/portaria/v1/usuario/login")
//        String login() {
//            return "ok";
//        }
//
//        @GetMapping("/portaria/v1/entrada/{id}")
//        String entrada() {
//            return "ok";
//        }
//    }
//}
