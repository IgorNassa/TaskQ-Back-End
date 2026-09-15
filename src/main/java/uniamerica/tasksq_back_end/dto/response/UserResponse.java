package uniamerica.tasksq_back_end.dto.response;

import java.time.LocalDateTime;

import uniamerica.tasksq_back_end.entity.enums.EloUser;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

public record UserResponse(
    Long id,
    String name,
    String email,
    String avatarUrl,
    CargoResponse cargo,
    UserStatus status,
    Long xp,
    EloUser elo,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
