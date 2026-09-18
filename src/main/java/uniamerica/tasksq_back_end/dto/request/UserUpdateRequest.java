package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

public record UserUpdateRequest(
        @NotBlank @Size(min = 3, max = 120) String nome,
        @NotBlank @Email @Size(max = 160) String email,
        @URL @Size(max = 500) String urlAvatar,
        @NotNull Long cargoId,
        @NotNull UserStatus status
) {}
