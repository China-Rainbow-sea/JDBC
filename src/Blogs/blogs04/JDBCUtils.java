package Blogs.blogs04;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {

    private static DataSource dataSource = null;
    static {
        try {
            // 读取配置文件中的信息,方式一:
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            // 读取配置文件中信息，方式二：
            InputStream io = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties"); // 默认是读取src目录下的文件
            // 创建对象获取读取到的配置文件的参数信息
            Properties properties = new Properties();
            properties.load(is);  // 以简单的线性格式输入字符读取到属性列表(关键字/元素对)
            // 通过传入获取配置文件的对象，创建dbcp 数据库连接池
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e); // 将编译异常转换为运行异常抛出
        }
    }


    public static Connection getDbcpConnection() {

        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e); // 将编译异常转换为运行异常抛出
        }
    }


    // 通过 c3p0-config.xml 配置文件，创建c3p0数据库连接池
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0"); // 作为类属性存在
    public static Connection getC3p0Connection() {
        Connection connection = null;
        try {
            connection = cpds.getConnection();  // static 静态不要使用 this的引用
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

        return connection;
    }
}
