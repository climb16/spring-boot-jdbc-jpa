package online.jfree.spring.jdbc.jpa.io;

import java.util.Set;

/**
 * @author Guo Lixiao
 * @description
 * @date 2018/12/28 14:56
 * @sign 1.0
 */
public interface ClassScanner {

    /**
     * scanner class
     *
     * @param basePackages
     * @return
     */
    Set<Class<?>> scanClass(String[] basePackages);
}
