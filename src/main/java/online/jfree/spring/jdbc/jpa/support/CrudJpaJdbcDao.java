package online.jfree.spring.jdbc.jpa.support;

import java.util.List;

/**
 * @author Guo Lixiao
 * @description
 * @date 2018/12/28 10:52
 * @sign 1.0
 */
public interface CrudJpaJdbcDao<T, ID> {

    /**
     * save
     * @param entity
     * @return
     */
    long save(T entity);

    /**
     * update
     * @param entity
     * @return
     */
    long update(T entity);

    /**
     * delete
     * @param entity
     * @return
     */
    long delete(T entity);

    /**
     * getById
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * getAll
     * @return
     */
    List<T> findAll();

    /**
     * getAll
     * @param entity
     * @return
     */
    List<T> findAll(T entity);

    /**
     * count
     * @return
     */
    long count();
}
