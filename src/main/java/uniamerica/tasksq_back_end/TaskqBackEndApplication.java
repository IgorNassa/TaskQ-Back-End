package uniamerica.tasksq_back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskqBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskqBackEndApplication.class, args);
	}

}
