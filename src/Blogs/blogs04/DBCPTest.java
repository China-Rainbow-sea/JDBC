package Blogs.blogs04;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPTest {
    // 方式二:通过读取配置文件中的信息，创建dbcp数据库连接池
    public static void main(String[] args) {

        try {
            // 读取配置文件中的信息,方式一:
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));

            // 读取配置文件中信息，方式二：
            InputStream io = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties"); // 默认是读取src目录下的文件

            // 创建对象获取读取到的配置文件的参数信息
            Properties properties = new Properties();
            properties.load(is);  // 以简单的线性格式输入字符读取到属性列表(关键字/元素对)

            // 通过传入获取配置文件的对象，创建dbcp 数据库连接池
            DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);

            long start = System.currentTimeMillis();  // 获取到连接前的时间点: 单位毫秒

            for(int i = 0; i < 5000000; i++) {
                // 从dbcp数据库连接池中获取到连接
                Connection connection = dataSource.getConnection();

                if(connection != null) {
                    try{
                        // 归还连接，不是关闭连接
                        connection.close();
                    } catch(SQLException e) {
                        throw new RuntimeException(e); // 将编译异常转换为运行异常抛出
                    }
                }
            }

            long end = System.currentTimeMillis();  // 获取到所有连接完毕后的时间点:

            System.out.println("500w次连接所消耗的时间: " + (end - start));
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }


    // 方式一: 通过传参数,创建dbcp数据库连接池,再获取连接
    public static void main1(String[] args) {
        // 定义创建 dbcp 数据库连接池的对象
        BasicDataSource source = new BasicDataSource();

        source.setDriverClassName("com.mysql.cj.jdbc.Driver"); // 注册驱动这里是mysql8.0/ 5.0的没有 cj
        source.setUrl("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true"); // 实际数据库在网络url的位置
        // ?rewriteBatchedStatements=true批量处理
        // 或者可以省略本地主机 localhost:3306 改为 jdbc:mysql:///test

        source.setUsername("root"); // 用户名
        source.setPassword("MySQL123"); // 密码

        // 获取到数据库中的连接
        try {
            Connection connection = source.getConnection();
            System.out.println(connection);  // 所获得的连接的地址
            System.out.println(connection.getClass());  // 类所在的包路径
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

    }
}
