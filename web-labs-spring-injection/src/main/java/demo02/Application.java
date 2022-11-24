package demo02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context and observe it creates different instances when the scope is prototype
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Country firstCountry = context.getBean(Country.class);
			System.out.println("First country: " + firstCountry);
			Country secondCountry = context.getBean(Country.class);
			secondCountry.resetTo("Hungary", "Europe", "Budapest");
			System.out.println("Second country: " + secondCountry);
			System.out.println("First country: " + firstCountry);
		}
	}

}
