package demo03;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InstanceCollectorAspect {
	
	private final Map<Class<?>, List<WeakReference<Object>>> instancesByType =
			new HashMap<Class<?>, List<WeakReference<Object>>>();

	@AfterReturning(pointcut = "execution(* *(..))", returning = "value")
	public void afterReturningConstructor(JoinPoint joinPoint, Object value) {
		if (value != null) {
			Class<?> type = value.getClass();
			if (!instancesByType.containsKey(type)) {
				instancesByType.put(type, new ArrayList<WeakReference<Object>>());
			}
			instancesByType.get(type).add(new WeakReference<Object>(value));
		}
	}
	
	public List<WeakReference<Object>> getInstancesByType(Class<?> type) {
		return instancesByType.get(type);
	}
}
