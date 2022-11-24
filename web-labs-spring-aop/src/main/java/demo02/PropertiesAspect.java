package demo02;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PropertiesAspect {

	@Before("execution(* get*(..))")
	public void beforeGetter(JoinPoint joinPoint) {
		try {
			Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
			Properties annotation = method.getDeclaringClass().getAnnotation(Properties.class);
			String name = method.getName().substring("get".length());
			name = Character.toLowerCase(name.charAt(0))
					+ (name.length() > 1 ? name.substring(1) : "");
			Field field = method.getDeclaringClass().getDeclaredField(name);
			field.setAccessible(true);
			if (field.get(joinPoint.getTarget()) == null) {
				field.set(joinPoint.getTarget(),
						ResourceBundle.getBundle(annotation.value()).getString(name));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
