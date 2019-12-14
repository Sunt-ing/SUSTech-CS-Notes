package main.java.cn.edu.sustech;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ContainerImpl implements Container {
    public Map<Class<?>, Class<?>> map = new HashMap<>();

    @Override
    public <T> void register(Class<T> serviceType) {
        register(serviceType, serviceType);
    }

    @Override
    public <T> void register(Class<T> serviceType, Class<? extends T> implementationType) {
        if (serviceType == null || implementationType == null)
            throw new IllegalArgumentException("ServiceType or implementationType should not be null.");
        if (Modifier.isAbstract(implementationType.getModifiers()))
            throw new IllegalArgumentException("ImplementationType should not be an abstract class.");
        if (Modifier.isInterface(implementationType.getModifiers()))
            throw new IllegalArgumentException("ImplementationType should not be an interface.");
        if (implementationType.getDeclaredConstructors().length > 1)
            throw new IllegalArgumentException("ImplementationType should not have multiple constrictors.");

        map.put(serviceType, implementationType);
        if (!map.containsKey(implementationType))
            map.put(implementationType, implementationType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> serviceType) {
        if (serviceType == null)
            throw new IllegalArgumentException("ServiceType should not be null.");
        if (!map.containsKey(serviceType))
            throw new ServiceNotFoundException();

        return (T) getInstance(map.get(serviceType));
    }

    @SuppressWarnings("unchecked")
    private <T> T getInstance(Class<T> serviceType) {
        Constructor[] constructors = serviceType.getDeclaredConstructors();
        Constructor<T> constructor = constructors[0];
        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] objects = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++
        ) {
            if (map.get(parameterTypes[i]) == null) {
                throw new ServiceNotFoundException();
            }
            objects[i] = this.resolve(map.get(parameterTypes[i]));
        }
        try {
            return this.injectInstance(constructor.newInstance(objects));
        } catch (Exception e) {
            throw new ServiceNotFoundException();
        }
    }

    private <T> T injectInstance(T instance) throws NoSuchFieldException, IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getDeclaredAnnotation(Inject.class) != null) {
                if (field.isAccessible())
                    field.set(instance, this.resolve(field.getType()));
                else {
                    field.setAccessible(true);
                    field.set(instance, this.resolve(field.getType()));
                    field.setAccessible(false);
                }
            }

        }

        if (instance.getClass().getSuperclass() != null) {
            Field[] fields2 = instance.getClass().getSuperclass().getDeclaredFields();
            for (Field field : fields2) {
                if (field.getDeclaredAnnotation(Inject.class) != null) {
                    if (field.isAccessible())
                        field.set(instance, this.resolve(field.getType()));
                    else {
                        field.setAccessible(true);
                        field.set(instance, this.resolve(field.getType()));
                        field.setAccessible(false);
                    }
                }

            }

            if (instance.getClass().getSuperclass().getSuperclass() != null) {
                Field[] fields3 = instance.getClass().getSuperclass().getSuperclass().getDeclaredFields();
                for (Field field : fields3) {
                    if (field.getDeclaredAnnotation(Inject.class) != null) {
                        if (field.isAccessible())
                            field.set(instance, this.resolve(field.getType()));
                        else {
                            field.setAccessible(true);
                            field.set(instance, this.resolve(field.getType()));
                            field.setAccessible(false);
                        }
                    }

                }


                if (instance.getClass().getSuperclass().getSuperclass().getSuperclass() != null) {
                    Field[] fields4 = instance.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredFields();
                    for (Field field : fields4) {
                        if (field.getDeclaredAnnotation(Inject.class) != null) {
                            if (field.isAccessible())
                                field.set(instance, this.resolve(field.getType()));
                            else {
                                field.setAccessible(true);
                                field.set(instance, this.resolve(field.getType()));
                                field.setAccessible(false);
                            }
                        }

                    }
                }

            }
        }

        return instance;
    }
}