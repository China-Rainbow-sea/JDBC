package Blogs.blogs04;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class JDBCTest {
    public static void main(String[] args) {
        // 读取配置文件信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");  // 默认读取的是src目录下的文件

        try {
            Properties properties = new Properties();   // 创建读取.properties后缀的配置文件的对象
            properties.load(is);  // 以简单的线性格式输入字符读取属性列表(关键字/元素对)

            // 通过对象传入关键字获取到其中的信息
            String driverClass = properties.getProperty("driverClass");
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            // 3. 加载/注册驱动
            Class.forName(driverClass);

            long start = System.currentTimeMillis();  // 获取到 5000 次连接数据库的前的时间点：单位：毫秒

            // 测试连接 5000 次
            for(int i = 0; i < 5000; i ++) {
                // 连接数据库
                Connection connection = DriverManager.getConnection(url,user,password);

                if(connection != null) {
                    try {
                        connection.close(); // 关闭数据库连接
                    } catch(SQLException e) {
                        throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                    }
                }

            }

            long end = System.currentTimeMillis();   // 5000次连接数据库结束的时间点：单位：毫秒
            System.out.println("jdbc 5000 次连接数据库所消耗的时间：" + (end - start));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
