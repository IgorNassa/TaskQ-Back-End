
package uniamerica.tasksq_back_end.dto.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import uniamerica.tasksq_back_end.dto.response.UserResponse;
import uniamerica.tasksq_back_end.entity.Role;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.UserRank;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final RoleMapper roleMapper =
            Mappers.getMapper(RoleMapper.class);

    private final UserMapper userMapper =
            Mappers.getMapper(UserMapper.class);

    @BeforeEach
    void configurarMappers() {
        ReflectionTestUtils.setField(
                userMapper,
                "roleMapper",
                roleMapper
        );
    }

    @Test
    void deveConverterUsuarioParaResponse() {
        Role cargo = new Role(1L, "Desenvolvedor");

        User usuario = new User();
        usuario.setId(10L);
        usuario.setNome("Pedro");
        usuario.setEmail("pedrovwparaguacu@gmail.com");
        usuario.setUrlAvatar("https://exemplo.com/avatar.png");
        usuario.setCargo(cargo);
        usuario.setStatus(UserStatus.values()[0]);
        usuario.setXp(100L);
        usuario.setElo(UserRank.INICIANTE);

        UserResponse response = userMapper.toResponse(usuario);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("Pedro", response.nome());
        assertEquals("pedrovwparaguacu@gmail.com", response.email());
        assertEquals(
                "https://exemplo.com/avatar.png",
                response.urlAvatar()
        );

        assertNotNull(response.cargo());
        assertEquals(1L, response.cargo().id());
        assertEquals("Desenvolvedor", response.cargo().nome());

        assertEquals(usuario.getStatus(), response.status());
        assertEquals(100L, response.xp());
        assertEquals(UserRank.INICIANTE, response.elo());
    }

    @Test
    void deveConverterListaDeUsuariosParaResponse() {
        User usuario1 = new User();
        usuario1.setId(1L);
        usuario1.setNome("Pedro");

        User usuario2 = new User();
        usuario2.setId(2L);
        usuario2.setNome("João");

        List<UserResponse> responses =
                userMapper.toResponseList(
                        List.of(usuario1, usuario2)
                );

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Pedro", responses.get(0).nome());
        assertEquals("João", responses.get(1).nome());
    }

    @Test
    void deveRetornarListaVaziaQuandoReceberListaVazia() {
        List<UserResponse> responses =
                userMapper.toResponseList(List.of());

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}
