package demo05;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionAspect {

	@AfterThrowing(pointcut = "execution(* *(..)) && args(step)", throwing = "exception")
	public void aroundMethod(JoinPoint joinPoint, int step, Exception exception) throws Throwable {
		System.out.println(String.format("%s at step %d.",
				exception.getMessage(), step));
	}
}
