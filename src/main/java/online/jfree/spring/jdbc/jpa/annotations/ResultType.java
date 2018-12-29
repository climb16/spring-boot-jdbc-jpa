package online.jfree.spring.jdbc.jpa.annotations;

import java.lang.annotation.*;

/**
 * @author Guo Lixiao
 * @description
 * @date 2018/12/28 14:34
 * @sign 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResultType {
    Class<?> value();
}
