package uniamerica.tasksq_back_end.dto.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import uniamerica.tasksq_back_end.dto.response.UserResponse;
import uniamerica.tasksq_back_end.entity.User;

@Mapper(componentModel = "spring", uses = RoleMapper.class, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserResponse paraResposta(User usuario);

    List<UserResponse> paraListaResposta(List<User> usuarios);
}