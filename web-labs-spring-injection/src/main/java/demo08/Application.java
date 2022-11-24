package demo08;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context with qualifiers based on field names
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Country country = context.getBean(Country.class);
			System.out.println(country);
		}
	}

}
