package uniamerica.tasksq_back_end.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;

@Service
@RequiredArgsConstructor
public class XpService {

    @Transactional
    public void recompensaTaskCompleta(Task task) {
        int totalXp = calculoXp(task.getPriority());

<<<<<<< Updated upstream
        User usuario = task.getAssigneeId();
=======
        int totalXp = calculateXp(task.getPriority());
        Long usuarioId = task.getAssigneeId().getId();
        if (userRepository.incrementXp(usuarioId, totalXp) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
>>>>>>> Stashed changes

        usuario.setXp(usuario.getXp() + totalXp);
    }

    private int calculoXp(TaskPriority prioridade) {
        return switch (prioridade) {
            case BAIXA -> 10;
            case MEDIA -> 20;
            case ALTA -> 30;
        };
    }
}
