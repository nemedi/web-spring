package demo07;

import org.springframework.stereotype.Component;

@Component
public class Worker {

	public void doSomething() {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
		}
	}
}
