package demo03;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context registered via component scan and component annotations
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Country country = context.getBean(Country.class);
			System.out.println(country);
		}
	}

}
