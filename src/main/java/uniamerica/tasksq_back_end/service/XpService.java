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
    public void rewardCompletedTask(Task task) {
        int totalXp = calculateXp(task.getPriority());

        User usuario = task.getAssigneeId();

        usuario.setXp(usuario.getXp() + totalXp);
    }

    private int calculateXp(TaskPriority prioridade) {
        return switch (prioridade) {
            case BAIXA -> 10;
            case MEDIA -> 20;
            case ALTA -> 30;
        };
    }
}
