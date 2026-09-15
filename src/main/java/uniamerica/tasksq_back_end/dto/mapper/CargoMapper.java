package uniamerica.tasksq_back_end.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import uniamerica.tasksq_back_end.dto.request.CargoRequest;
import uniamerica.tasksq_back_end.dto.response.CargoResponse;
import uniamerica.tasksq_back_end.entity.Cargo;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CargoMapper {

    @Mapping(target = "id", ignore = true)
    Cargo toEntity(CargoRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(CargoRequest request, @MappingTarget Cargo cargo);

    CargoResponse toResponse(Cargo cargo);

    List<CargoResponse> toResponseList(List<Cargo> cargos);
}
