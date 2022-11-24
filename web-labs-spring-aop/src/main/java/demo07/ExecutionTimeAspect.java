package demo07;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionTimeAspect {

	@Around("execution(* *(..))")
	public void afterExecution(ProceedingJoinPoint joinPoint) {
		long start = System.currentTimeMillis();
		try {
			joinPoint.proceed();
		} catch (Throwable t) {
		}
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		long stop = System.currentTimeMillis();
		System.out.println(String.format("Execution of method %s::%s took %d milliseconds.",
				method.getDeclaringClass().getName(),
				method.getName(),
				stop - start));
	}
}
