package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleRequest(
        @NotBlank @Size(min = 2, max = 80)
        String nome
) {}
