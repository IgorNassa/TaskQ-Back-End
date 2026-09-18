package uniamerica.tasksq_back_end;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@EnableScheduling
public class TaskqBackEndApplication {

	public static void main(String[] argumentos) {
		SpringApplication.run(TaskqBackEndApplication.class, argumentos);
	}

}

