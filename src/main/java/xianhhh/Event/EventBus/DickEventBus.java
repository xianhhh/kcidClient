package xianhhh.Event.EventBus;
import xianhhh.Event.Event;
import xianhhh.Event.EventBus.Annotation.EventTarget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DickEventBus {
    private final Map<Class<?>, List<Method>> eventMethodMap = new HashMap<>();

    public void register(Object obj) {
        Class<?> listenerClass = obj.getClass();
        for (Method method : listenerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventTarget.class)) {
                method.setAccessible(true);
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("Method " + method.getName() + " in class " + listenerClass.getName() + " must have exactly one parameter.");
                }
                eventMethodMap.computeIfAbsent(parameterTypes[0], k -> new ArrayList<>()).add(method);
            }
        }
    }

    public void unregister(Object obj) {
        Class<?> listenerClass = obj.getClass();
        for (Method method : listenerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventTarget.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                eventMethodMap.getOrDefault(parameterTypes[0], new ArrayList<>()).remove(method);
            }
        }
    }

    public void post(Event event) {
        if (event.isCancelled()) {
            return; // 如果事件已经被取消，跳过当前事件
        }

        List<Method> methods = eventMethodMap.get(event.getClass());
        if (methods != null) {
            for (Method method : methods) {
                try {
                    method.invoke(method.getDeclaringClass().newInstance(), event);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    throw new RuntimeException("Failed to invoke event handler method.", e);
                }
            }
        }
    }
}
