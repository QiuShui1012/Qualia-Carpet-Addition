package zh.qiushui.mod.qca.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;

public class UnsafeUtil {
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericClass(Class<?> genericHostClass) {
        return (Class<T>) ((ParameterizedType) genericHostClass.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> T cast(@NotNull Object o) {
        return (T) o;
    }
}
