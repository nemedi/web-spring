package demo04;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// commit or rollback transaction
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Worker worker = context.getBean(Worker.class);
			for (int i = 0; i < 10; i++) {
				try {
					worker.doSomething();
				} catch (Exception e) {
				}
			}
		}
	}

}
