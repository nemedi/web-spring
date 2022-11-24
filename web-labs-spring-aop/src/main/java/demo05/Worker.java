package demo05;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class Worker {

	public void doSomething(int step) throws Exception {
		if (new Random().nextBoolean()) {
			throw new Exception("Something went wrong.");
		}
	}
}
