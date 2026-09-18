package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
        @NotBlank @Size(min = 8, max = 72) String senha
) {}
