package online.jfree.spring.jdbc.jpa.bind;

/**
 * @author Guo Lixiao
 * @description
 * @date 2018/12/28 14:49
 * @sign 1.0
 */
class DaoProxyFactory<T> {

    private final T dao;

    public DaoProxyFactory(T dao) {
        this.dao = dao;
    }

    T newInstance() {

        return null;
    }

}
