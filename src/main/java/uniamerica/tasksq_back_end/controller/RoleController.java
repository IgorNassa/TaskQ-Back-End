package uniamerica.tasksq_back_end.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniamerica.tasksq_back_end.dto.request.RoleRequest;
import uniamerica.tasksq_back_end.dto.response.RoleResponse;
import uniamerica.tasksq_back_end.service.RoleService;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RoleController {

    private final RoleService servicoCargo;

    @PostMapping
    public ResponseEntity<RoleResponse> cadastrar(@Valid @RequestBody RoleRequest dados) {
        RoleResponse resposta = servicoCargo.cadastrar(dados);
        return ResponseEntity.created(URI.create("/api/cargos/" + resposta.id())).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> listar() {
        return ResponseEntity.ok(servicoCargo.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> buscarPorId(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(servicoCargo.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> atualizar(
            @PathVariable @Positive Long id,
            @Valid @RequestBody RoleRequest dados) {
        return ResponseEntity.ok(servicoCargo.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable @Positive Long id) {
        servicoCargo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}