package demo08;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Application {

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class);
				WebServer server = new WebServer();
				Scanner scanner = new Scanner(System.in)) {
			server.open(8080);
			while (true) {
				if (scanner.hasNextLine()
						&& "exit".equalsIgnoreCase(scanner.nextLine())) {
					System.exit(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
