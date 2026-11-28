package uniamerica.tasksq_back_end.integration;

import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import uniamerica.tasksq_back_end.entity.Role;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.UserProfile;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;
import uniamerica.tasksq_back_end.repository.RoleRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthenticationIntegrationTest {
    private static final String EMAIL = "jwt-test@example.test";
    private static final String PASSWORD = "test-only-password";

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper mapper;
    @Autowired private UserRepository users;
    @Autowired private RoleRepository roles;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String testSecret;

    private User user;

    @BeforeEach
    void createMember() {
        Role role = new Role();
        role.setNome("Cargo do teste JWT");
        roles.save(role);

        user = new User();
        user.setNome("Usuário do teste JWT");
        user.setEmail(EMAIL);
        user.setSenhaHash(passwordEncoder.encode(PASSWORD));
        user.setCargo(role);
        user.setStatus(UserStatus.ATIVO);
        user.setPerfil(UserProfile.MEMBRO);
        users.saveAndFlush(user);
    }

    @Test
    void memberCanLogInAndReadCurrentIdentity() throws Exception {
        String token = loginAndReadToken();
        currentUser(token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.perfil").value("MEMBRO"));
    }

    @Test
    void privateRoutesRejectMissingOrInvalidTokens() throws Exception {
        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/usuarios")).andExpect(status().isUnauthorized());
        currentUser("invalid-token").andExpect(status().isUnauthorized());
    }

    @Test
    void expiredTokenIsRejected() throws Exception {
        String expiredToken = Jwts.builder()
                .claim("id", user.getId())
                .setSubject(EMAIL)
                .setIssuer("task-q")
                .setAudience("task-q-api")
                .setExpiration(Date.from(Instant.now().minusSeconds(60)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(testSecret)), SignatureAlgorithm.HS256)
                .compact();

        currentUser(expiredToken).andExpect(status().isUnauthorized());
    }

    @Test
    void tokenForAnotherAudienceIsRejected() throws Exception {
        String token = Jwts.builder()
                .claim("id", user.getId())
                .setSubject(EMAIL)
                .setIssuer("task-q")
                .setAudience("other-api")
                .setExpiration(Date.from(Instant.now().plusSeconds(60)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(testSecret)), SignatureAlgorithm.HS256)
                .compact();

        currentUser(token).andExpect(status().isUnauthorized());
    }

    @Test
    void invalidLoginBodyReturnsBadRequest() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid\",\"senha\":\" \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.senha").exists());
    }

    @Test
    void corsAllowsConfiguredOriginAndRejectsOtherOrigins() throws Exception {
        mvc.perform(options("/api/auth/me")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200"));

        mvc.perform(options("/api/usuarios")
                        .header(HttpHeaders.ORIGIN, "https://other.example.test")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Authorization"))
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidPasswordIsRejected() throws Exception {
        login("wrong-password").andExpect(status().isUnauthorized());
    }

    @Test
    void deactivationBlocksLoginAndPreviouslyIssuedTokens() throws Exception {
        String token = loginAndReadToken();

        user.setStatus(UserStatus.INATIVO);
        users.saveAndFlush(user);

        login(PASSWORD).andExpect(status().isUnauthorized());
        currentUser(token).andExpect(status().isUnauthorized());
    }

    private ResultActions login(String password) throws Exception {
        return mvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"%s","senha":"%s"}
                        """.formatted(EMAIL, password)));
    }

    private String loginAndReadToken() throws Exception {
        String response = login(PASSWORD)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario.perfil").value("MEMBRO"))
                .andReturn().getResponse().getContentAsString();
        return mapper.readTree(response).get("token").asText();
    }

    private ResultActions currentUser(String token) throws Exception {
        return mvc.perform(get("/api/auth/me").header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
    }
}
