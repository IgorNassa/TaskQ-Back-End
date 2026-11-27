package uniamerica.tasksq_back_end.dto.response;

import uniamerica.tasksq_back_end.entity.enums.UserProfile;

public record UsuarioLogadoResponse(
    Long id,
    String nome,
    String email,
    String cargo,
    UserProfile perfil
) {}
