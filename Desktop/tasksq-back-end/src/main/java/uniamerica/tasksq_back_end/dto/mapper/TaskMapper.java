package uniamerica.tasksq_back_end.dto.mapper;

import org.mapstruct.Mapper;
import uniamerica.tasksq_back_end.dto.request.TaskRequest;
import uniamerica.tasksq_back_end.dto.response.TaskResponse;
import uniamerica.tasksq_back_end.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskRequest request);

    TaskResponse toResponse(Task task);
}