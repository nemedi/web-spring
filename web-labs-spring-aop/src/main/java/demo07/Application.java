package demo07;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// measure method execution time
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			context.getBean(Worker.class).doSomething();
		}
	}

}
