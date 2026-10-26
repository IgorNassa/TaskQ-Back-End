
package uniamerica.tasksq_back_end.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import uniamerica.tasksq_back_end.dto.mapper.RoleMapper;
import uniamerica.tasksq_back_end.dto.request.RoleRequest;
import uniamerica.tasksq_back_end.dto.response.RoleResponse;
import uniamerica.tasksq_back_end.entity.Role;
import uniamerica.tasksq_back_end.repository.RoleRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository repositorioCargo;

    @Mock
    private UserRepository repositorioUsuario;

    @Mock
    private RoleMapper mapeadorCargo;

    @InjectMocks
    private RoleService roleService;

    private Role cargo;
    private RoleResponse resposta;

    @BeforeEach
    void configurar() {
        cargo = new Role(1L, "ADMIN");
        resposta = new RoleResponse(1L, "ADMIN");
    }

    @Test
    void deveSalvarCargoComSucesso() {
        RoleRequest dados = new RoleRequest(" ADMIN ");

        when(repositorioCargo.existsByNomeIgnoreCase("ADMIN"))
                .thenReturn(false);
        when(repositorioCargo.save(any(Role.class)))
                .thenReturn(cargo);
        when(mapeadorCargo.toResponse(cargo))
                .thenReturn(resposta);

        RoleResponse resultado = roleService.save(dados);

        assertEquals(resposta, resultado);
        verify(repositorioCargo).save(any(Role.class));
    }

    @Test
    void deveRejeitarCargoComNomeDuplicado() {
        RoleRequest dados = new RoleRequest("ADMIN");

        when(repositorioCargo.existsByNomeIgnoreCase("ADMIN"))
                .thenReturn(true);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> roleService.save(dados)
        );

        assertEquals(HttpStatus.CONFLICT, excecao.getStatusCode());
        verify(repositorioCargo, never()).save(any(Role.class));
    }

    @Test
    void deveListarCargosOrdenadosPorNome() {
        List<Role> cargos = List.of(cargo);
        List<RoleResponse> respostas = List.of(resposta);

        when(repositorioCargo.findAll(Sort.by("nome")))
                .thenReturn(cargos);
        when(mapeadorCargo.toResponseList(cargos))
                .thenReturn(respostas);

        List<RoleResponse> resultado = roleService.listAll();

        assertEquals(respostas, resultado);
        verify(repositorioCargo).findAll(Sort.by("nome"));
    }

    @Test
    void deveBuscarCargoPorId() {
        when(repositorioCargo.findById(1L))
                .thenReturn(Optional.of(cargo));
        when(mapeadorCargo.toResponse(cargo))
                .thenReturn(resposta);

        RoleResponse resultado = roleService.findById(1L);

        assertEquals(resposta, resultado);
    }

    @Test
    void deveLancarErroQuandoCargoNaoExisteNaBusca() {
        when(repositorioCargo.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> roleService.findById(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
    }

    @Test
    void deveAtualizarCargoComSucesso() {
        RoleRequest dados = new RoleRequest("GERENTE");

        when(repositorioCargo.findById(1L))
                .thenReturn(Optional.of(cargo));
        when(repositorioCargo.existsByNomeIgnoreCaseAndIdNot("GERENTE", 1L))
                .thenReturn(false);
        when(repositorioCargo.save(cargo))
                .thenReturn(cargo);
        when(mapeadorCargo.toResponse(cargo))
                .thenReturn(new RoleResponse(1L, "GERENTE"));

        RoleResponse resultado = roleService.update(1L, dados);

        assertEquals("GERENTE", resultado.nome());
        verify(repositorioCargo).save(cargo);
    }

    @Test
    void deveRejeitarAtualizacaoComNomeDuplicado() {
        RoleRequest dados = new RoleRequest("GERENTE");

        when(repositorioCargo.findById(1L))
                .thenReturn(Optional.of(cargo));
        when(repositorioCargo.existsByNomeIgnoreCaseAndIdNot("GERENTE", 1L))
                .thenReturn(true);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> roleService.update(1L, dados)
        );

        assertEquals(HttpStatus.CONFLICT, excecao.getStatusCode());
        verify(repositorioCargo, never()).save(any(Role.class));
    }

    @Test
    void deveExcluirCargoSemUsuariosVinculados() {
        when(repositorioCargo.findById(1L))
                .thenReturn(Optional.of(cargo));
        when(repositorioUsuario.existsByCargoId(1L))
                .thenReturn(false);

        roleService.delete(1L);

        verify(repositorioCargo).delete(cargo);
    }

    @Test
    void deveRejeitarExclusaoDeCargoComUsuariosVinculados() {
        when(repositorioCargo.findById(1L))
                .thenReturn(Optional.of(cargo));
        when(repositorioUsuario.existsByCargoId(1L))
                .thenReturn(true);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> roleService.delete(1L)
        );

        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT,
                excecao.getStatusCode());
        verify(repositorioCargo, never()).delete(any(Role.class));
    }

    @Test
    void deveLancarErroAoExcluirCargoInexistente() {
        when(repositorioCargo.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> roleService.delete(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
        verify(repositorioCargo, never()).delete(any(Role.class));
    }
}
