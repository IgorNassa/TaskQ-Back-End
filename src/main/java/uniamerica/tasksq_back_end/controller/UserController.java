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
    public ResponseEntity<UserResponse> save(@Valid @RequestBody UserRequest dados) {
        UserResponse resposta = servicoUsuario.save(dados);
        return ResponseEntity.created(URI.create("/api/usuarios/" + resposta.id())).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> listAll() {
        return ResponseEntity.ok(servicoUsuario.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(servicoUsuario.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UserUpdateRequest dados) {
        return ResponseEntity.ok(servicoUsuario.update(id, dados));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> updatePassword(
            @PathVariable @Positive Long id,
            @Valid @RequestBody PasswordUpdateRequest dados) {
        servicoUsuario.updatePassword(id, dados);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<UserResponse> activate(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(servicoUsuario.activate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable @Positive Long id) {
        servicoUsuario.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}