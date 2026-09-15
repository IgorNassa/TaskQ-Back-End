package uniamerica.tasksq_back_end.controller;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniamerica.tasksq_back_end.dto.request.CargoRequest;
import uniamerica.tasksq_back_end.dto.response.CargoResponse;
import uniamerica.tasksq_back_end.service.CargoService;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Cargos", description = "Cadastro e manutenção básica de cargos")
public class CargoController {

    private final CargoService cargoService;

    @PostMapping
    @Operation(summary = "Cadastrar cargo")
    public ResponseEntity<CargoResponse> create(@Valid @RequestBody CargoRequest request) {
        CargoResponse response = cargoService.create(request);
        return ResponseEntity.created(URI.create("/api/cargos/" + response.id())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar cargos")
    public ResponseEntity<List<CargoResponse>> findAll() {
        return ResponseEntity.ok(cargoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar cargo por ID")
    public ResponseEntity<CargoResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(cargoService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cargo")
    public ResponseEntity<CargoResponse> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CargoRequest request
    ) {
        return ResponseEntity.ok(cargoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cargo sem usuários vinculados")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        cargoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
