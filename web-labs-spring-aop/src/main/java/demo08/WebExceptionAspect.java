package demo08;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpExchange;

@Aspect
@Component
public class WebExceptionAspect {

	@AfterThrowing(pointcut = "execution(* *(..))",
			throwing = "exception")
	public void afterThrowingWebException(JoinPoint joinPoint, Exception exception) throws Throwable {
		if (exception instanceof WebException) {
			HttpExchange exchange = (HttpExchange) joinPoint.getArgs()[0];
			exchange.sendResponseHeaders(((WebException) exception).getStatus(), 0);
			exchange.close();
		}
	}
}
