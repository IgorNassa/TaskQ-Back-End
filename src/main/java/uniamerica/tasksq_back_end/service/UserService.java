package uniamerica.tasksq_back_end.service;

import java.util.List;
import java.util.Locale;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.dto.mapper.UserMapper;
import uniamerica.tasksq_back_end.dto.request.PasswordUpdateRequest;
import uniamerica.tasksq_back_end.dto.request.UserRequest;
import uniamerica.tasksq_back_end.dto.request.UserUpdateRequest;
import uniamerica.tasksq_back_end.dto.response.UserResponse;
import uniamerica.tasksq_back_end.entity.Role;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.UserStatus;
import uniamerica.tasksq_back_end.repository.RoleRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class
UserService {

    private final UserRepository repositorioUsuario;
    private final RoleRepository repositorioCargo;
    private final UserMapper mapeadorUsuario;
    private final PasswordEncoder codificadorSenha;

    @Transactional
    public UserResponse save(UserRequest dados) {
        String email = dados.email().trim().toLowerCase(Locale.ROOT);
        if (repositorioUsuario.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail");
        }

        User usuario = new User();
        usuario.setNome(dados.nome().trim().replaceAll("\\s+", " "));
        usuario.setEmail(email);
        usuario.setUrlAvatar(dados.urlAvatar());
        usuario.setSenhaHash(encodePassword(dados.senha()));
        usuario.setCargo(findRole(dados.cargoId()));
        usuario.setStatus(dados.status());

        return mapeadorUsuario.toResponse(repositorioUsuario.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAll() {
        List<User> usuarios = repositorioUsuario.findAll(Sort.by("nome"));
        return mapeadorUsuario.toResponseList(usuarios);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return mapeadorUsuario.toResponse(findUser(id));
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest dados) {
        User usuario = findUser(id);
        String email = dados.email().trim().toLowerCase(Locale.ROOT);
        if (repositorioUsuario.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail");
        }

        usuario.setNome(dados.nome().trim().replaceAll("\\s+", " "));
        usuario.setEmail(email);
        usuario.setUrlAvatar(dados.urlAvatar());
        usuario.setCargo(findRole(dados.cargoId()));
        usuario.setStatus(dados.status());

        return mapeadorUsuario.toResponse(repositorioUsuario.save(usuario));
    }

    @Transactional
    public void updatePassword(Long id, PasswordUpdateRequest dados) {
        User usuario = findUser(id);
        usuario.setSenhaHash(encodePassword(dados.senha()));
    }

    @Transactional
    public void deactivate(Long id) {
        User usuario = findUser(id);
        usuario.setStatus(UserStatus.INATIVO);
    }

    @Transactional
    public UserResponse activate(Long id) {
        User usuario = findUser(id);
        usuario.setStatus(UserStatus.ATIVO);
        return mapeadorUsuario.toResponse(usuario);
    }

    public User findUser(Long id) {
        return repositorioUsuario.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private String encodePassword(String senha) {
        if (senha.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ocupar no máximo 72 bytes em UTF-8");
        }
        return codificadorSenha.encode(senha);
    }

    private Role findRole(Long id) {
        return repositorioCargo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado"));
    }
}
