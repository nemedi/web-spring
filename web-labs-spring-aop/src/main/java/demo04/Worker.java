package demo04;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Worker {

	@Transactional
	public void doSomething() throws Exception {
		if (new Random().nextBoolean()) {
			throw new Exception("Something went wrong...");
		}
	}
}
