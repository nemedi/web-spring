package demo04;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TransactionalAspect {

	@Around("execution(* *(..)) && @annotation(annotation)")
	public void aroundMethod(ProceedingJoinPoint joinPoint, Transactional annotation) throws Throwable {
		try {
			Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
			System.out.println("Beginning transaction...");
			System.out.println(String.format("Calling %s::%s...",
					method.getDeclaringClass().getName(),
					method.getName()));
			joinPoint.proceed();
			System.out.println("Committing transaction...");
		} catch (Throwable e) {
			System.err.println("Rolling-back transaction...");
		}
	}
}
