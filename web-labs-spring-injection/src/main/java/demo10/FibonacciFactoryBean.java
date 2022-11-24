package demo10;

import org.springframework.beans.factory.FactoryBean;

public class FibonacciFactoryBean implements FactoryBean<Integer> {
	
	private int a = 0;
	private int b = 1;
	
	public FibonacciFactoryBean() {
		System.out.println("DEBUG: FibonacciFactoryBean::.ctor");
	}

	@Override
	public Integer getObject() throws Exception {
		int c = a + b;
		a = b;
		b = c;
		System.out.println("DEBUG: FibonacciFactoryBean::getObject");
		return c;
	}

	@Override
	public Class<?> getObjectType() {
		System.out.println("DEBUG: FibonacciFactoryBean::getObjectType");
		return Integer.class;
	}
	
	@Override
	public boolean isSingleton() {
		System.out.println("DEBUG: FibonacciFactoryBean::isSingleton");
		return false;
	}

}
