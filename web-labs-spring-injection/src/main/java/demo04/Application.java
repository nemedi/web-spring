package demo04;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context injecting dependencies in constructor call
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Country country = context.getBean(Country.class);
			System.out.println(country.getCapital());
			System.out.println(country.toString());
		}
	}

}
