package uniamerica.tasksq_back_end.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.XpTransaction;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.TaskRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;
import uniamerica.tasksq_back_end.repository.XpTransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;

@Service
@RequiredArgsConstructor
public class XpService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final XpTransactionRepository xpTransactionRepository;

    @Transactional
    public void rewardCompletedTask(Long taskId) {
        Task task = taskRepository.findByIdForUpdate(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));
        if (task.getStatus() != TaskStatus.CONCLUIDO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A tarefa precisa estar concluída para receber XP");
        }
        if (xpTransactionRepository.existsByTipoReferenciaAndReferenciaId("TASK", taskId)) {
            return;
        }

        int totalXp = calculateXp(task.getPriority());
        Long usuarioId = task.getAssigneeId().getId();
        // A soma no banco evita perder XP quando duas tarefas do mesmo usuário são concluídas juntas.
        if (userRepository.incrementXp(usuarioId, totalXp) == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        XpTransaction transaction = new XpTransaction();
        transaction.setUsuarioId(usuarioId);
        transaction.setQuantidade(totalXp);
        transaction.setMotivo("Conclusão de tarefa");
        transaction.setTipoReferencia("TASK");
        transaction.setReferenciaId(taskId);
        xpTransactionRepository.save(transaction);
    }

    private int calculateXp(TaskPriority prioridade) {
        return switch (prioridade) {
            case BAIXA -> 10;
            case MEDIA -> 20;
            case ALTA -> 30;
        };
    }
}
