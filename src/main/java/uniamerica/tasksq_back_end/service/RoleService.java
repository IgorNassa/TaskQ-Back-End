package uniamerica.tasksq_back_end.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.dto.mapper.RoleMapper;
import uniamerica.tasksq_back_end.dto.request.RoleRequest;
import uniamerica.tasksq_back_end.dto.response.RoleResponse;
import uniamerica.tasksq_back_end.entity.Role;
import uniamerica.tasksq_back_end.repository.RoleRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repositorioCargo;
    private final UserRepository repositorioUsuario;
    private final RoleMapper mapeadorCargo;

    @Transactional
    public RoleResponse cadastrar(RoleRequest dados) {
        String nome = dados.nome().trim().replaceAll("\\s+", " ");
        if (repositorioCargo.existsByNomeIgnoreCase(nome)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um cargo com este nome");
        }

        Role cargo = new Role();
        cargo.setNome(nome);
        return mapeadorCargo.paraResposta(repositorioCargo.save(cargo));
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> listar() {
        List<Role> cargos = repositorioCargo.findAll(Sort.by("nome"));
        return mapeadorCargo.paraListaResposta(cargos);
    }

    @Transactional(readOnly = true)
    public RoleResponse buscarPorId(Long id) {
        return mapeadorCargo.paraResposta(buscarCargo(id));
    }

    @Transactional
    public RoleResponse atualizar(Long id, RoleRequest dados) {
        Role cargo = buscarCargo(id);
        String nome = dados.nome().trim().replaceAll("\\s+", " ");
        if (repositorioCargo.existsByNomeIgnoreCaseAndIdNot(nome, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um cargo com este nome");
        }

        cargo.setNome(nome);
        return mapeadorCargo.paraResposta(repositorioCargo.save(cargo));
    }

    @Transactional
    public void excluir(Long id) {
        Role cargo = buscarCargo(id);
        if (repositorioUsuario.existsByCargoId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_CONTENT, "Não é possível excluir um cargo que possui usuários vinculados");
        }
        repositorioCargo.delete(cargo);
    }

    private Role buscarCargo(Long id) {
        return repositorioCargo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cargo não encontrado"));
    }
}