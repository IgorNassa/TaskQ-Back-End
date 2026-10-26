
package uniamerica.tasksq_back_end.dto.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import uniamerica.tasksq_back_end.dto.response.RoleResponse;
import uniamerica.tasksq_back_end.entity.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleMapperTest {

    private final RoleMapper roleMapper =
            Mappers.getMapper(RoleMapper.class);

    @Test
    void deveConverterCargoParaResponse() {
        Role cargo = new Role(1L, "Desenvolvedor");

        RoleResponse response = roleMapper.toResponse(cargo);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Desenvolvedor", response.nome());
    }

    @Test
    void deveConverterListaDeCargosParaResponse() {
        Role cargo1 = new Role(1L, "Administrador");
        Role cargo2 = new Role(2L, "Desenvolvedor");

        List<RoleResponse> responses =
                roleMapper.toResponseList(List.of(cargo1, cargo2));

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Administrador", responses.get(0).nome());
        assertEquals("Desenvolvedor", responses.get(1).nome());
    }

    @Test
    void deveRetornarListaVaziaQuandoReceberListaVazia() {
        List<RoleResponse> responses =
                roleMapper.toResponseList(List.of());

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }
}
