package Blogs.blogs04;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Test {
    // 方式二: 通过读取配置文件中的信息，创建c3p0数据库连接池
    public static void main(String[] args) {
        // 创建c3p0数据库连接池
        DataSource dataSource = new ComboPooledDataSource("helloc3p0");

        long start = System.currentTimeMillis();  // 连接前的时间点：单位：毫秒
        try {

            for(int i = 0 ; i < 500000; i++) {
                // 获取到数据库连接池中的连接
                Connection connection = dataSource.getConnection();

                if(connection != null) {
                    try{
                        connection.close();  // 使用使用数据库连接池，归还连接，不是关闭连接
                    } catch(SQLException e) {
                        throw new RuntimeException(e); // 将编译异常转换为运行异常抛出
                    }
                }
            }

            long end = System.currentTimeMillis();   // 连接结束后的时间点： 单位：毫秒

            System.out.println("c3p0 数据库连接池 50w 次连接所消耗的时间："+ (end - start));

        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }
    }


    // 方式一: 传配置参数创建 c3p0数据库连接池
    public static void main2(String[] args) {
        try {
            // 通过传入配置参数,创建 c3p0 数据库连接池
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" );   // 注册数据库驱动信息
            cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test");  // 实际连接的数据库的url在网络中所在的位置
            cpds.setUser("root");  // 用户名
            cpds.setPassword("MySQL123");  // 密码

            // 从c3p0 数据库连接池中获取连接数据库的对象
            Connection connection = cpds.getConnection();

            System.out.println(connection); // 打印连接的地址
            System.out.println(connection.getClass()); // 打印显示实现该类的所在包路径

        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
