package demo03;

import java.util.Random;

public class RandomNumber {

	private static final Random random = new Random();
	private int value;

	public RandomNumber() {
		value = random.nextInt();
	}

	public int getValue() {
		return value;
	}
}
