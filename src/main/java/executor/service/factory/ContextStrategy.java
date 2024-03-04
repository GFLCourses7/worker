package executor.service.factory;

import java.util.Map;
import java.util.function.Supplier;

public interface ContextStrategy {

    Object initWithinContext(Supplier<?> supplier, Map<Class<?>, Object> context, Class<?> clazz);

    ContextStrategy SINGLETON = (supplier, context, clazz) -> {

        Object object;
        if (context.containsKey(clazz)) {
            object = context.get(clazz);
        } else {
            object = supplier.get();
            context.put(clazz, object);
        }

        return object;
    };

}
