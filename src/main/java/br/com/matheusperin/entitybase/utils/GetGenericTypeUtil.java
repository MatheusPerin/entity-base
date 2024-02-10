package br.com.matheusperin.entitybase.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GetGenericTypeUtil {

    public static GetGenericTypeUtil create() {
        return new GetGenericTypeUtil();
    }

    public <T> Class<T> forClass(Class<?> c, int position) {
        return forType(c.getGenericSuperclass(), position);
    }

    public <T> Class<T> forField(Field field, int position) {
        return forType(field.getGenericType(), position);
    }

    public <T> Class<T> forType(Type type, int position) {
        if (!(type instanceof ParameterizedType)) {
            type = ((Class<?>) type).getGenericSuperclass();
        }

        return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[position];
    }

}
