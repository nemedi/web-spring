package demo07;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// get bean from context injecting implementation based on bean qualifier
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Country republic = context.getBean(Republic.class);
			System.out.println(republic);
			Country kingdom = context.getBean(Kingdom.class);
			System.out.println(kingdom);
		}
	}

}
