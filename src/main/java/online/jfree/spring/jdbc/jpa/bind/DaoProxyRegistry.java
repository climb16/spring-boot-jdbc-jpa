package online.jfree.spring.jdbc.jpa.bind;

import online.jfree.spring.jdbc.jpa.exceptions.JdbcJpaException;
import online.jfree.spring.jdbc.jpa.io.ClassScanner;
import online.jfree.spring.jdbc.jpa.io.DefaultClassScanner;

import java.util.*;

/**
 * @author Guo Lixiao
 * @description
 * @date 2018/12/28 13:52
 * @sign 1.0
 */
public class DaoProxyRegistry {

    /**
     * dao interface -> dao proxy class
     */
    private final Map<Class<?>, DaoProxyFactory<?>> knownMappers = new HashMap<>();

    /**
     * get dao proxy of interface.
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> T getDaoProxy(Class<T> type) {
        final DaoProxyFactory<T> daoProxyFactory = (DaoProxyFactory<T>) knownMappers.get(type);
        if (daoProxyFactory == null) {
            throw new JdbcJpaException("Type " + type + " is not known to the DaoProxyRegistry.");
        }
        try {
            return daoProxyFactory.newInstance();
        } catch (Exception e) {
            throw new JdbcJpaException("Error getting dao instance. Cause: " + e, e);
        }
    }

    /**
     * has dao proxy factory of interface.
     *
     * @param type
     * @param <T>
     * @return
     */
    public <T> boolean hasDaoProxy(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    /**
     *
     * @param type
     * @param <T>
     */
    public <T> void addDaoProxy(Class<T> type) {
        if (type.isInterface()) {
            if (hasDaoProxy(type)) {
                throw new JdbcJpaException("Type " + type + " is already known to the DaoProxyRegistry.");
            }
           knownMappers.put(type, new DaoProxyFactory<>(type));
        }
    }

    /**
     *
     * @return
     */
    public Collection<Class<?>> getDaoProxys() {
        return Collections.unmodifiableCollection(knownMappers.keySet());
    }


    public void addDaoProxy(String packageName) {
        ClassScanner scanner = new DefaultClassScanner();
        Set<Class<?>> mapperSet = scanner.scanClass(new String[]{packageName});
        for (Class<?> mapperClass : mapperSet) {
            addDaoProxy(mapperClass);
        }
    }
}
