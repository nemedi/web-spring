package demo03;

import java.lang.ref.WeakReference;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	// collect all instances of a type
	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				ApplicationConfiguration.class)) {
			for (int i = 0; i < 10; i++) {
				context.getBean(RandomNumber.class);
			}
			for (WeakReference<Object> randomNumberReference : context.getBean(InstanceCollectorAspect.class)
					.getInstancesByType(RandomNumber.class)) {
				System.out.println(RandomNumber.class.cast(randomNumberReference.get()).getValue());
			}
		}
	}

}
