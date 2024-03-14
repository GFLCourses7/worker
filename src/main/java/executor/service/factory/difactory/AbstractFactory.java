package executor.service.factory.difactory;

public interface AbstractFactory {

    <T> T create(Class<T> clazz);
}
