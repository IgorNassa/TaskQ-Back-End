
package uniamerica.tasksq_back_end.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import uniamerica.tasksq_back_end.dto.request.UserRequest;
import uniamerica.tasksq_back_end.dto.response.UserResponse;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;
import uniamerica.tasksq_back_end.repository.UserRepository;
import uniamerica.tasksq_back_end.repository.RoleRepository;
import uniamerica.tasksq_back_end.dto.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import uniamerica.tasksq_back_end.entity.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repositorioUsuario;

    @Mock
    private RoleRepository repositorioCargo;

    @Mock
    private UserMapper mapeadorUsuario;

    @Mock
    private PasswordEncoder codificadorSenha;

    @InjectMocks
    private UserService userService;

    @Test
    void deveEncontrarUsuarioQuandoIdExistir() {
        Long id = 1L;
        User usuario = new User();
        usuario.setId(id);

        when(repositorioUsuario.findById(id))
                .thenReturn(Optional.of(usuario));

        User resultado = userService.findUser(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        verify(repositorioUsuario).findById(id);
    }

    @Test
    void deveLancarErroQuandoUsuarioNaoExistir() {
        Long id = 999L;

        when(repositorioUsuario.findById(id))
                .thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> userService.findUser(id)
        );

        assertEquals(
                HttpStatus.NOT_FOUND,
                excecao.getStatusCode()
        );

        verify(repositorioUsuario).findById(id);
    }


    @Test
    void deveCadastrarUsuarioComSucesso() {

        UserRequest dados = new UserRequest(
                "  Pedro Batista ",
                " PEDROVWPARAGUACU@GMAIL.COM ",
                null,
                "Senha123",
                1L,
                UserStatus.ATIVO
        );

        Role cargo = new Role(1L, "Desenvolvedor");

        when(repositorioUsuario.existsByEmailIgnoreCase("pedrovwparaguacu@gmail.com"))
                .thenReturn(false);

        when(repositorioCargo.findById(1L))
                .thenReturn(Optional.of(cargo));

        when(codificadorSenha.encode("Senha123"))
                .thenReturn("senha_criptografada");

        when(repositorioUsuario.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse resposta = new UserResponse(
                1L, "Pedro Batista", "pedrovwparaguacu@gmail.com", null,
                null, UserStatus.ATIVO, 0L, null, null, null
        );

        when(mapeadorUsuario.toResponse(any(User.class)))
                .thenReturn(resposta);

        UserResponse resultado = userService.save(dados);

        assertSame(resposta, resultado);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repositorioUsuario).save(captor.capture());

        User usuarioSalvo = captor.getValue();

        assertEquals("Pedro Batista", usuarioSalvo.getNome());
        assertEquals("pedrovwparaguacu@gmail.com", usuarioSalvo.getEmail());
        assertEquals("senha_criptografada", usuarioSalvo.getSenhaHash());

        verify(codificadorSenha).encode("Senha123");
    }

    @Test
    void deveRejeitarCadastroComEmailDuplicado() {

        UserRequest dados = new UserRequest(
                "Pedro Batista",
                "PEDROVWPARAGUACU@GMAIL.COM",
                null,
                "Senha123",
                1L,
                UserStatus.ATIVO
        );

        when(repositorioUsuario.existsByEmailIgnoreCase("pedrovwparaguacu@gmail.com"))
                .thenReturn(true);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> userService.save(dados)
        );

        assertEquals(HttpStatus.CONFLICT, excecao.getStatusCode());

        verify(repositorioUsuario, never()).save(any(User.class));
        verifyNoInteractions(repositorioCargo);
        verifyNoInteractions(codificadorSenha);
    }

    @Test
    void deveRejeitarSenhaComMaisDe72Bytes() {

        String senhaLonga = "a".repeat(73);

        UserRequest dados = new UserRequest(
                "Pedro Batista",
                "pedrovwparaguacu@gmail.com",
                null,
                senhaLonga,
                1L,
                UserStatus.ATIVO
        );

        when(repositorioUsuario.existsByEmailIgnoreCase("pedrovwparaguacu@gmail.com"))
                .thenReturn(false);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> userService.save(dados)
        );

        assertEquals(HttpStatus.BAD_REQUEST, excecao.getStatusCode());

        verify(codificadorSenha, never()).encode(anyString());
        verify(repositorioUsuario, never()).save(any(User.class));
    }

}
