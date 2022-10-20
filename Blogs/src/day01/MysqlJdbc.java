package day01;

/**
 * 模拟 Mysql 数据库厂家实现 JDBC 接口的实现类
 */
public class MysqlJdbc implements JdbcInterface{
    @Override
    public Object getConnection() {
        System.out.println("连接上 MySQL数据库");
        return null;
    }

    @Override
    public void crud() {
        System.out.println("对MySQL数据库进行增删改操作");
    }

    @Override
    public void close() {
        System.out.println("关闭MySQL资源");
    }
}
