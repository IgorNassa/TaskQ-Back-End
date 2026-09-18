package uniamerica.tasksq_back_end.dto.response;

import java.time.LocalDateTime;

import uniamerica.tasksq_back_end.entity.enums.UserRank;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;

public record UserResponse(
    Long id,
    String nome,
    String email,
    String urlAvatar,
    RoleResponse cargo,
    UserStatus status,
    Long xp,
    UserRank elo,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
