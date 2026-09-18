package uniamerica.tasksq_back_end.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class XpService {

    private final UserRepository repositorioUsuario;

    @Transactional
    public void recompensaTaskCompleta(Task task) {
        int totalXp = calculoXp(task.getPriority());

        User usuario = repositorioUsuario.findById(task.getAssigneeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setXp(usuario.getXp() + totalXp);
    }

    private int calculoXp(TaskPriority prioridade) {
        return switch (prioridade) {
            case BAIXA -> 10;
            case MEDIA -> 20;
            case ALTA  -> 30;
        };
    }
}