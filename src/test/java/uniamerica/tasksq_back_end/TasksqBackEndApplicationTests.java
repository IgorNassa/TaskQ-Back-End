package uniamerica.tasksq_back_end;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class TasksqBackEndApplicationTests {

	@Test
	void contextLoads() {
		// O carregamento do contexto sem exceção é a verificação deste teste.
	}

	@Test
	void mainTest() {
		assertDoesNotThrow(() -> TaskqBackEndApplication.main(new String[]{}));
	}

}
