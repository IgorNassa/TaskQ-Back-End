
package uniamerica.tasksq_back_end.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.servlet.resource.NoResourceFoundException;
import uniamerica.tasksq_back_end.dto.response.ApiErrorResponse;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();

        request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/users/1");
    }

    @Test
    void deveRetornarStatusDaExcecaoDeNegocio() {
        ResponseStatusException exception =
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"
                );

        ResponseEntity<ApiErrorResponse> response =
                handler.handleBusinessError(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "Usuário não encontrado",
                response.getBody().message()
        );
        assertEquals(
                "/api/users/1",
                response.getBody().path()
        );
        assertTrue(response.getBody().fieldErrors().isEmpty());
    }

    @Test
    void deveRetornarConflictQuandoExisteViolacaoDeIntegridade() {
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Registro duplicado");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleDatabaseConflict(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "A operação viola uma regra do banco de dados",
                response.getBody().message()
        );
        assertEquals(
                "/api/users/1",
                response.getBody().path()
        );
        assertTrue(response.getBody().fieldErrors().isEmpty());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorreErroInesperado() {
        Exception exception = new Exception("Erro inesperado");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleUnexpectedError(exception, request);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );
        assertNotNull(response.getBody());
        assertEquals(
                "Erro interno do servidor",
                response.getBody().message()
        );
        assertEquals(
                "/api/users/1",
                response.getBody().path()
        );
        assertTrue(response.getBody().fieldErrors().isEmpty());
    }

    @Test
    void deveRetornarBadRequestQuandoCamposSaoInvalidos() {
        var bindingResult = new org.springframework.validation.BeanPropertyBindingResult(
                new Object(), "userRequest"
        );

        bindingResult.addError(
                new org.springframework.validation.FieldError(
                        "userRequest",
                        "email",
                        "O e-mail é obrigatório"
                )
        );

        var exception = new org.springframework.web.bind.MethodArgumentNotValidException(
                null,
                bindingResult
        );

        ResponseEntity<ApiErrorResponse> response =
                handler.handleValidation(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Dados inválidos", response.getBody().message());
        assertEquals(
                "O e-mail é obrigatório",
                response.getBody().fieldErrors().get("email")
        );
        assertEquals("/api/users/1", response.getBody().path());
    }
    @Test
    void deveRetornarBadRequestQuandoExisteViolacaoDeRestricao() {
        var violation = mock(jakarta.validation.ConstraintViolation.class);

        when(violation.getPropertyPath()).thenReturn(
                mock(jakarta.validation.Path.class)
        );
        when(violation.getPropertyPath().toString()).thenReturn("buscarUsuario.id");
        when(violation.getMessage()).thenReturn("deve ser positivo");

        var exception = mock(jakarta.validation.ConstraintViolationException.class);

        when(exception.getConstraintViolations()).thenReturn(Set.of(violation));

        ResponseEntity<ApiErrorResponse> response =
                handler.handleConstraintViolation(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Parâmetro inválido", response.getBody().message());
        assertEquals(
                "deve ser positivo",
                response.getBody().fieldErrors().get("buscarUsuario.id")
        );
        assertEquals("/api/users/1", response.getBody().path());
    }
    @Test
    void deveRetornarBadRequestQuandoValidacaoDoMetodoFalha() {
        var exception = mock(HandlerMethodValidationException.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleInvalidRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "Parâmetro ou corpo da requisição inválido",
                response.getBody().message()
        );
        assertEquals("/api/users/1", response.getBody().path());
        assertTrue(response.getBody().fieldErrors().isEmpty());
    }

    @Test
    void deveRetornarBadRequestQuandoTipoDoParametroEInvalido() {
        var exception = mock(MethodArgumentTypeMismatchException.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleInvalidRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "Parâmetro ou corpo da requisição inválido",
                response.getBody().message()
        );
        assertEquals("/api/users/1", response.getBody().path());
        assertTrue(response.getBody().fieldErrors().isEmpty());
    }

    @Test
    void deveRetornarBadRequestQuandoCorpoDaRequisicaoEInvalido() {
        var exception = mock(HttpMessageNotReadableException.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleInvalidRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "Parâmetro ou corpo da requisição inválido",
                response.getBody().message()
        );
        assertEquals("/api/users/1", response.getBody().path());
        assertTrue(response.getBody().fieldErrors().isEmpty());
    }

    @Test
    void deveRetornarNotFoundQuandoRotaNaoExiste() {
        var exception = mock(NoResourceFoundException.class);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleRouteNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Rota não encontrada", response.getBody().message());
        assertEquals("/api/users/1", response.getBody().path());
        assertTrue(response.getBody().fieldErrors().isEmpty());
    }



}
