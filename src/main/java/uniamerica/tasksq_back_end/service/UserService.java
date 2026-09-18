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
import uniamerica.tasksq_back_end.dto.mappers.UserMapper;
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
public class UserService {

    private final UserRepository repositorioUsuario;
    private final RoleRepository repositorioCargo;
    private final UserMapper mapeadorUsuario;
    private final PasswordEncoder codificadorSenha;

    @Transactional
    public UserResponse cadastrar(UserRequest dados) {
        String email = dados.email().trim().toLowerCase(Locale.ROOT);
        if (repositorioUsuario.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail");
        }

        User usuario = new User();
        usuario.setNome(dados.nome().trim().replaceAll("\\s+", " "));
        usuario.setEmail(email);
        usuario.setUrlAvatar(dados.urlAvatar());
        usuario.setSenhaHash(codificadorSenha.encode(dados.senha()));
        usuario.setCargo(buscarCargo(dados.cargoId()));
        usuario.setStatus(dados.status());

        return mapeadorUsuario.paraResposta(repositorioUsuario.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listar() {
        List<User> usuarios = repositorioUsuario.findAll(Sort.by("nome"));
        return mapeadorUsuario.paraListaResposta(usuarios);
    }

    @Transactional(readOnly = true)
    public UserResponse buscarPorId(Long id) {
        return mapeadorUsuario.paraResposta(buscarUsuario(id));
    }

    @Transactional
    public UserResponse atualizar(Long id, UserUpdateRequest dados) {
        User usuario = buscarUsuario(id);
        String email = dados.email().trim().toLowerCase(Locale.ROOT);
        if (repositorioUsuario.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail");
        }

        usuario.setNome(dados.nome().trim().replaceAll("\\s+", " "));
        usuario.setEmail(email);
        usuario.setUrlAvatar(dados.urlAvatar());
        usuario.setCargo(buscarCargo(dados.cargoId()));
        usuario.setStatus(dados.status());

        return mapeadorUsuario.paraResposta(repositorioUsuario.save(usuario));
    }

    @Transactional
    public void alterarSenha(Long id, PasswordUpdateRequest dados) {
        User usuario = buscarUsuario(id);
        usuario.setSenhaHash(codificadorSenha.encode(dados.senha()));
    }

    @Transactional
    public void inativar(Long id) {
        User usuario = buscarUsuario(id);
        usuario.setStatus(UserStatus.INATIVO);
    }

    @Transactional
    public UserResponse ativar(Long id) {
        User usuario = buscarUsuario(id);
        usuario.setStatus(UserStatus.ATIVO);
        return mapeadorUsuario.paraResposta(usuario);
    }

    public User buscarUsuario(Long id) {
        return repositorioUsuario.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private Role buscarCargo(Long id) {
        return repositorioCargo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado"));
    }
}