package demo06;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

	@After("execution(* *(..))")
	public void afterExecution(JoinPoint joinPoint) {
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		System.out.println(String.format("Class '%s', Method '%s', Thread '%s', User '%s'",
				method.getDeclaringClass().getName(),
				method.getName(),
				Thread.currentThread().getName(),
				System.getProperty("user.name")));
	}
}
