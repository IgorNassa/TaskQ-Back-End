package uniamerica.tasksq_back_end.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import uniamerica.tasksq_back_end.dto.response.RoleResponse;
import uniamerica.tasksq_back_end.entity.Role;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RoleMapper {

    RoleResponse paraResposta(Role cargo);

    List<RoleResponse> paraListaResposta(List<Role> cargos);
}