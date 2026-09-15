package uniamerica.tasksq_back_end.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniamerica.tasksq_back_end.dto.mapper.CargoMapper;
import uniamerica.tasksq_back_end.dto.request.CargoRequest;
import uniamerica.tasksq_back_end.dto.response.CargoResponse;
import uniamerica.tasksq_back_end.entity.Cargo;
import uniamerica.tasksq_back_end.exception.BusinessRuleException;
import uniamerica.tasksq_back_end.exception.DuplicateResourceException;
import uniamerica.tasksq_back_end.exception.ResourceNotFoundException;
import uniamerica.tasksq_back_end.repository.CargoRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final UserRepository userRepository;
    private final CargoMapper cargoMapper;

    @Transactional
    public CargoResponse create(CargoRequest request) {
        String name = normalizeName(request.name());
        ensureNameAvailable(name, null);
        Cargo cargo = cargoMapper.toEntity(request);
        cargo.setName(name);
        return cargoMapper.toResponse(cargoRepository.save(cargo));
    }

    @Transactional(readOnly = true)
    public List<CargoResponse> findAll() {
        return cargoMapper.toResponseList(cargoRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    @Transactional(readOnly = true)
    public CargoResponse findById(Long id) {
        return cargoMapper.toResponse(findCargo(id));
    }

    @Transactional
    public CargoResponse update(Long id, CargoRequest request) {
        Cargo cargo = findCargo(id);
        String name = normalizeName(request.name());
        ensureNameAvailable(name, id);
        cargoMapper.updateEntity(request, cargo);
        cargo.setName(name);
        return cargoMapper.toResponse(cargoRepository.save(cargo));
    }

    @Transactional
    public void delete(Long id) {
        Cargo cargo = findCargo(id);
        if (userRepository.existsByCargoId(id)) {
            throw new BusinessRuleException("Não é possível excluir um cargo que possui usuários vinculados");
        }
        cargoRepository.delete(cargo);
    }

    private Cargo findCargo(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
    }

    private void ensureNameAvailable(String name, Long currentCargoId) {
        boolean exists = currentCargoId == null
                ? cargoRepository.existsByNameIgnoreCase(name)
                : cargoRepository.existsByNameIgnoreCaseAndIdNot(name, currentCargoId);
        if (exists) {
            throw new DuplicateResourceException("Já existe um cargo com este nome");
        }
    }

    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }
}
