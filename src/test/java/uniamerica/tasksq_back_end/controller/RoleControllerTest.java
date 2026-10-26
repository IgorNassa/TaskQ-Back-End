package uniamerica.tasksq_back_end.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uniamerica.tasksq_back_end.dto.request.RoleRequest;
import uniamerica.tasksq_back_end.dto.response.RoleResponse;
import uniamerica.tasksq_back_end.service.RoleService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {
    @Mock
    RoleService roleService;

    @InjectMocks
    RoleController roleController;

    @Test
    void saveTest() {
        RoleRequest request = mock(RoleRequest.class);
        RoleResponse response = mock(RoleResponse.class);

        when(response.id()).thenReturn(1L);
        when(roleService.save(request)).thenReturn(response);

        ResponseEntity<RoleResponse> result = roleController.save(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        assertEquals("/api/cargos/1", result.getHeaders().getLocation().toString());
        verify(roleService).save(request);
    }

    @Test
    void listAllTest() {
        RoleResponse response = mock(RoleResponse.class);
        when(roleService.listAll()).thenReturn(List.of(response));

        ResponseEntity<List<RoleResponse>> result = roleController.listAll();

        assertEquals(1, result.getBody().size());
        verify(roleService).listAll();
    }

    @Test
    void findByIdTest() {
        Long id = 1L;
        RoleResponse response = mock(RoleResponse.class);
        when(roleService.findById(id)).thenReturn(response);

        ResponseEntity<RoleResponse> result = roleController.findById(id);

        assertEquals(response, result.getBody());
        verify(roleService).findById(id);
    }

    @Test
    void updateTest() {
        Long id = 1L;
        RoleRequest request = mock(RoleRequest.class);
        RoleResponse response = mock(RoleResponse.class);
        when(roleService.update(id, request)).thenReturn(response);

        ResponseEntity<RoleResponse> result = roleController.update(id, request);

        assertEquals(response, result.getBody());
        verify(roleService).update(id, request);
    }

    @Test
    void deleteTest() {
        Long id = 1L;

        ResponseEntity<Void> result = roleController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(roleService).delete(id);
    }
}
