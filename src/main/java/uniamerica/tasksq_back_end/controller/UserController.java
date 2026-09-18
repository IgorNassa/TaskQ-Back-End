package uniamerica.tasksq_back_end.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniamerica.tasksq_back_end.dto.request.UserRequest;
import uniamerica.tasksq_back_end.dto.request.UserUpdateRequest;
import uniamerica.tasksq_back_end.dto.request.PasswordUpdateRequest;
import uniamerica.tasksq_back_end.dto.response.UserResponse;
import uniamerica.tasksq_back_end.service.UserService;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService servicoUsuario;

    @PostMapping
    public ResponseEntity<UserResponse> cadastrar(@Valid @RequestBody UserRequest dados) {
        UserResponse resposta = servicoUsuario.cadastrar(dados);
        return ResponseEntity.created(URI.create("/api/usuarios/" + resposta.id())).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listar() {
        return ResponseEntity.ok(servicoUsuario.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> buscarPorId(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(servicoUsuario.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> atualizar(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UserUpdateRequest dados) {
        return ResponseEntity.ok(servicoUsuario.atualizar(id, dados));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable @Positive Long id,
            @Valid @RequestBody PasswordUpdateRequest dados) {
        servicoUsuario.alterarSenha(id, dados);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<UserResponse> ativar(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(servicoUsuario.ativar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable @Positive Long id) {
        servicoUsuario.inativar(id);
        return ResponseEntity.noContent().build();
    }
}