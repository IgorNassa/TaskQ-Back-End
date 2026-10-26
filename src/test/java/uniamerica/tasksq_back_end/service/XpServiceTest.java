
package uniamerica.tasksq_back_end.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import uniamerica.tasksq_back_end.entity.Task;
import uniamerica.tasksq_back_end.entity.User;
import uniamerica.tasksq_back_end.entity.XpTransaction;
import uniamerica.tasksq_back_end.entity.enums.TaskPriority;
import uniamerica.tasksq_back_end.entity.enums.TaskStatus;
import uniamerica.tasksq_back_end.repository.TaskRepository;
import uniamerica.tasksq_back_end.repository.UserRepository;
import uniamerica.tasksq_back_end.repository.XpTransactionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class XpServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private XpTransactionRepository xpTransactionRepository;

    @InjectMocks
    private XpService xpService;

    private Task task;
    private User usuario;

    @BeforeEach
    void configurar() {
        task = mock(Task.class);
        usuario = mock(User.class);

        lenient().when(taskRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(task));

        lenient().when(task.getStatus())
                .thenReturn(TaskStatus.CONCLUIDO);

        lenient().when(task.getAssigneeId())
                .thenReturn(usuario);

        lenient().when(usuario.getId())
                .thenReturn(10L);
    }

    @Test
    void deveLancarErroQuandoTarefaNaoExiste() {
        when(taskRepository.findByIdForUpdate(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> xpService.rewardCompletedTask(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());

        verify(userRepository, never()).incrementXp(anyLong(), anyInt());
        verify(xpTransactionRepository, never()).save(any(XpTransaction.class));
    }

    @Test
    void deveRejeitarTarefaQueNaoEstaConcluida() {
        when(task.getStatus()).thenReturn(TaskStatus.PENDENTE);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> xpService.rewardCompletedTask(1L)
        );

        assertEquals(HttpStatus.BAD_REQUEST, excecao.getStatusCode());

        verify(xpTransactionRepository, never()).save(any(XpTransaction.class));
    }

    @Test
    void naoDeveConcederXpDuasVezesParaAMesmaTarefa() {
        when(xpTransactionRepository
                .existsByTipoReferenciaAndReferenciaId("TASK", 1L))
                .thenReturn(true);

        xpService.rewardCompletedTask(1L);

        verify(userRepository, never()).incrementXp(anyLong(), anyInt());
        verify(xpTransactionRepository, never()).save(any(XpTransaction.class));
    }

    @Test
    void deveConceder10XpParaPrioridadeBaixa() {
        testarRecompensa(TaskPriority.BAIXA, 10);
    }

    @Test
    void deveConceder20XpParaPrioridadeMedia() {
        testarRecompensa(TaskPriority.MEDIA, 20);
    }

    @Test
    void deveConceder30XpParaPrioridadeAlta() {
        testarRecompensa(TaskPriority.ALTA, 30);
    }

    @Test
    void deveLancarErroQuandoUsuarioNaoExiste() {
        when(task.getPriority()).thenReturn(TaskPriority.MEDIA);

        when(xpTransactionRepository
                .existsByTipoReferenciaAndReferenciaId("TASK", 1L))
                .thenReturn(false);

        when(userRepository.incrementXp(10L, 20))
                .thenReturn(0);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> xpService.rewardCompletedTask(1L)
        );

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());

        verify(xpTransactionRepository, never()).save(any(XpTransaction.class));
    }

    private void testarRecompensa(TaskPriority prioridade, int xpEsperado) {
        when(task.getPriority()).thenReturn(prioridade);

        when(xpTransactionRepository
                .existsByTipoReferenciaAndReferenciaId("TASK", 1L))
                .thenReturn(false);

        when(userRepository.incrementXp(10L, xpEsperado))
                .thenReturn(1);

        xpService.rewardCompletedTask(1L);

        verify(userRepository).incrementXp(10L, xpEsperado);

        org.mockito.ArgumentCaptor<XpTransaction> captor =
                org.mockito.ArgumentCaptor.forClass(XpTransaction.class);

        verify(xpTransactionRepository).save(captor.capture());

        XpTransaction transacao = captor.getValue();

        assertEquals(10L, transacao.getUsuarioId());
        assertEquals(xpEsperado, transacao.getQuantidade());
        assertEquals("Conclusão de tarefa", transacao.getMotivo());
        assertEquals("TASK", transacao.getTipoReferencia());
        assertEquals(1L, transacao.getReferenciaId());
    }
}
