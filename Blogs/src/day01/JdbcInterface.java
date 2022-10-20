package day01;

/**
 * 模拟 SUN公司制定的 JDBC 接口
 */
public interface JdbcInterface {

    // 抽象方法，连接数据库
    public Object getConnection();

    // crud 增删改
    public void crud();

    // 关闭资源
    public void close();
}
