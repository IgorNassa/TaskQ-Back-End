package uniamerica.tasksq_back_end.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uniamerica.tasksq_back_end.dto.request.ProjectRequest;
import uniamerica.tasksq_back_end.dto.response.ProjectResponse;
import uniamerica.tasksq_back_end.entity.Project;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toEntity(ProjectRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    void updateEntity(ProjectRequest request, @MappingTarget Project project);

    ProjectResponse toResponse(Project project);

    List<ProjectResponse> responseList(List<Project> projects);

}
