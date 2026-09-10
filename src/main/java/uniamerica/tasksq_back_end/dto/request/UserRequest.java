package uniamerica.tasksq_back_end.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import uniamerica.tasksq_back_end.entity.Cargo;
import uniamerica.tasksq_back_end.entity.enums.EloUser;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

import java.net.URI;

public record UserRequest (
        @NotEmpty @Min(5)
        String name,

        @Email
        @NotEmpty
        Email email,

        @URL
        @NotEmpty
        URI avatarUrl,

        @NotEmpty
        Cargo cargo,

        @NotEmpty
        UserStatus status,

        @Positive
        Long xp,

        @NotEmpty
        EloUser elo
    ) {

    }