package Blogs.blogs04;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DruidTest {
    public static void main(String[] args) {
        // 读取配置文件的方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

        try {
            // 读取配置文件的方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建对象获取到读取的配置文件中的关键字/属性值
            Properties properties = new Properties();
            properties.load(io);

            // 通过传入读取配置文件信息对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            long start = System.currentTimeMillis();   // 获取到连接前的时间点：单位毫秒

            for(int i = 0; i < 5000000; i++) {
                // 从创建的 druid数据库连接池中获取到连接
                Connection connection = dataSource.getConnection();

                if(connection != null) {
                    try {
                        connection.close();   // “归还连接”，不是关闭连接
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            long end = System.currentTimeMillis();   // 获取到连接完毕后的时间点：单位毫秒

            System.out.println("500w次连接所消耗的时间：" +(end - start));

        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出。
        }

    }
}
