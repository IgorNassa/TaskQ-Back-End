package uniamerica.tasksq_back_end.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uniamerica.tasksq_back_end.dto.request.PasswordUpdateRequest;
import uniamerica.tasksq_back_end.dto.request.UserRequest;
import uniamerica.tasksq_back_end.dto.request.UserUpdateRequest;
import uniamerica.tasksq_back_end.dto.response.UserResponse;
import uniamerica.tasksq_back_end.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    void saveTest() {
        UserRequest request = mock(UserRequest.class);
        UserResponse response = mock(UserResponse.class);

        when(response.id()).thenReturn(1L);
        when(userService.save(request)).thenReturn(response);

        ResponseEntity<UserResponse> result = userController.save(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        assertEquals("/api/usuarios/1", result.getHeaders().getLocation().toString());
        verify(userService).save(request);
    }

    @Test
    void listAllTest() {
        UserResponse response = mock(UserResponse.class);
        when(userService.listAll()).thenReturn(List.of(response));

        ResponseEntity<List<UserResponse>> result = userController.listAll();

        assertEquals(1, result.getBody().size());
        verify(userService).listAll();
    }

    @Test
    void findByIdTest() {
        Long id = 1L;
        UserResponse response = mock(UserResponse.class);
        when(userService.findById(id)).thenReturn(response);

        ResponseEntity<UserResponse> result = userController.findById(id);

        assertEquals(response, result.getBody());
        verify(userService).findById(id);
    }

    @Test
    void updateTest() {
        Long id = 1L;
        UserUpdateRequest request = mock(UserUpdateRequest.class);
        UserResponse response = mock(UserResponse.class);
        when(userService.update(id, request)).thenReturn(response);

        ResponseEntity<UserResponse> result = userController.update(id, request);

        assertEquals(response, result.getBody());
        verify(userService).update(id, request);
    }

    @Test
    void updatePasswordTest() {
        Long id = 1L;
        PasswordUpdateRequest request = mock(PasswordUpdateRequest.class);

        ResponseEntity<Void> result = userController.updatePassword(id, request);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).updatePassword(id, request);
    }

    @Test
    void activateTest() {
        Long id = 1L;
        UserResponse response = mock(UserResponse.class);
        when(userService.activate(id)).thenReturn(response);

        ResponseEntity<UserResponse> result = userController.activate(id);

        assertEquals(response, result.getBody());
        verify(userService).activate(id);
    }

    @Test
    void deactivateTest() {
        Long id = 1L;

        ResponseEntity<Void> result = userController.deactivate(id);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService).deactivate(id);
    }
}
