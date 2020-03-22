package utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * <p>
 * foreach循环帮助类
 * </p>
 *
 * @Authoe: diudiu
 * @Date: 2020/3/22
 **/
public class ForEachUtils {

    public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> biConsumer) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            biConsumer.accept(t, index);
        };
    }
}
