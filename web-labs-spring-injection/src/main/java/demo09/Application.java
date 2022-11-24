package demo09;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context injecting a list of beans as field of a component
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Country country = context.getBean(Country.class);
			System.out.println(country);
		}
	}

}
