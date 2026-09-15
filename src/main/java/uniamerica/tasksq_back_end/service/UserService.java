package uniamerica.tasksq_back_end.service;

import java.util.List;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniamerica.tasksq_back_end.dto.mapper.UserMapper;
import uniamerica.tasksq_back_end.dto.request.PasswordUpdateRequest;
import uniamerica.tasksq_back_end.dto.request.UserRequest;
import uniamerica.tasksq_back_end.dto.request.UserUpdateRequest;
import uniamerica.tasksq_back_end.dto.response.UserResponse;
import uniamerica.tasksq_back_end.entity.Cargo;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.EloUser;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;
import uniamerica.tasksq_back_end.exception.DuplicateResourceException;
import uniamerica.tasksq_back_end.exception.ResourceNotFoundException;
import uniamerica.tasksq_back_end.repository.CargoRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CargoRepository cargoRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserRequest request) {
        String email = normalizeEmail(request.email());
        ensureEmailAvailable(email, null);

        User user = userMapper.toEntity(request);
        user.setName(normalizeName(request.name()));
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCargo(findCargo(request.cargoId()));
        user.setXp(0L);
        user.setElo(EloUser.INICIANTE);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userMapper.toResponseList(userRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = findUser(id);
        String email = normalizeEmail(request.email());
        ensureEmailAvailable(email, id);

        userMapper.updateEntity(request, user);
        user.setName(normalizeName(request.name()));
        user.setEmail(email);
        user.setCargo(findCargo(request.cargoId()));

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void updatePassword(Long id, PasswordUpdateRequest request) {
        User user = findUser(id);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public void inactivate(Long id) {
        User user = findUser(id);
        user.setStatus(UserStatus.INATIVO);
    }

    @Transactional
    public UserResponse activate(Long id) {
        User user = findUser(id);
        user.setStatus(UserStatus.ATIVO);
        return userMapper.toResponse(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    private Cargo findCargo(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado"));
    }

    private void ensureEmailAvailable(String email, Long currentUserId) {
        boolean exists = currentUserId == null
                ? userRepository.existsByEmailIgnoreCase(email)
                : userRepository.existsByEmailIgnoreCaseAndIdNot(email, currentUserId);
        if (exists) {
            throw new DuplicateResourceException("Já existe um usuário com este e-mail");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }
}
