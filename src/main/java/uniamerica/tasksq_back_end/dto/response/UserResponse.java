package uniamerica.tasksq_back_end.dto.response;

import java.net.URI;

import jakarta.validation.constraints.Email;
import uniamerica.tasksq_back_end.entity.Cargo;
import uniamerica.tasksq_back_end.entity.enums.EloUser;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

public record UserResponse(
    String name,

    Email email,

    URI avatarUrl,

    Cargo cargo,

    UserStatus status,

    Long xp,

    EloUser elo
) {
    
}