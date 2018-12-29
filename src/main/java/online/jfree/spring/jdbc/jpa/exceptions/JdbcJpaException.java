package online.jfree.spring.jdbc.jpa.exceptions;

/**
 * @author Guo Lixiao
 * @description
 * @date 2018/12/28 14:56
 * @sign 1.0
 */
public class JdbcJpaException extends RuntimeException{

    private static final long serialVersionUID = 8035158135901442564L;

    public JdbcJpaException() {
        super();
    }

    public JdbcJpaException(String message) {
        super(message);
    }

    public JdbcJpaException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcJpaException(Throwable cause) {
        super(cause);
    }

}
