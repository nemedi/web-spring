package demo01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// check method arguments before calling it
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			Person person = context.getBean(Person.class);
			person.setFirstName("Silvia");
			person.setLastName("Ilie-Nemedi");
			person.setGender('f');
		}
	}

}
