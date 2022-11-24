package demo06;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// log audit information about method execution
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			context.getBean(Worker.class).doSomething();
		}
	}

}
