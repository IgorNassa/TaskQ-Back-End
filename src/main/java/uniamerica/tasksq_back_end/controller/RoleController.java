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
    public ResponseEntity<RoleResponse> save(@Valid @RequestBody RoleRequest dados) {
        RoleResponse resposta = servicoCargo.save(dados);
        return ResponseEntity.created(URI.create("/api/cargos/" + resposta.id())).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> listAll() {
        return ResponseEntity.ok(servicoCargo.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(servicoCargo.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody RoleRequest dados) {
        return ResponseEntity.ok(servicoCargo.update(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        servicoCargo.delete(id);
        return ResponseEntity.noContent().build();
    }
}