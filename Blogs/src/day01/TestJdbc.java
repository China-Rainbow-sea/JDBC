package day01;

/**
 * 模拟Java程序面向JDBC接口连接数据库
 */
public class TestJdbc {
    public static void main(String[] args){
        JdbcInterface mysqlJdbc = new MysqlJdbc(); // 多态,接口引用实现类,(动态绑定)
        mysqlJdbc.getConnection(); // 连接Mysql数据库
        mysqlJdbc.crud();  // 对Mysql数据库进行增删改操作
        mysqlJdbc.close(); // 关闭Mysql 的资源

        System.out.println("==============下面是Oracle的连接操作============");

        JdbcInterface oracleJdbc = new OracleJdbc();
        oracleJdbc.getConnection();
        oracleJdbc.crud();
        oracleJdbc.close();
    }
}
