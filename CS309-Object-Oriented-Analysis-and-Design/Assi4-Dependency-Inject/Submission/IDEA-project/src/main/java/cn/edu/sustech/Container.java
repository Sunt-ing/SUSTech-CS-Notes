package main.java.cn.edu.sustech;

public interface Container {

    <T> void register(Class<T> serviceType);

    <T> void register(Class<T> serviceType, Class<? extends T> implementationType);

    <T> T resolve(Class<T> serviceType);
}
