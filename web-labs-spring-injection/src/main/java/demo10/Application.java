package demo10;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context by calling a factory bean to create a new instance each time
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			for (int i = 0; i < 10; i++) {
				System.out.println(context.getBean(Integer.class));
			}
		}
	}

}
