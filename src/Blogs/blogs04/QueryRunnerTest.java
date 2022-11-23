package Blogs.blogs04;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class QueryRunnerTest {
    public static void main(String[] args) {
        // 获取到配置文件的信息方式一: 这里获取到的是 druid数据库连接池中的配置文件的信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是src目录下的文件
        Connection connection = null;

        try {
            // 获取到配置文件的信息方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建获取配置文件中的信息的对象
            Properties properties = new Properties();
            properties.load(io); // 以简单的线性格式输入字符读取属性列表(关键字/元素对)

            // 通过传入读取到配置文件对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 获取到 druid数据库连接池中的其中的一个连接
            connection = dataSource.getConnection();

            // 使用 QueryRunner 插入记录
            QueryRunner queryRunner = new QueryRunner();

            String sql = "delete from customers where id = ?"; // 占位符不要加单引号,不然就成了字符串了

            // 执行sql语句                       connection 表示连接,sql 表示要执行的sql语句,params:表示填充占位符
            int insertCount = queryRunner.update(connection, sql, 24); // 返回影响数据库的行数

            System.out.println("删除了 "+insertCount+"条记录");
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        } finally {
            if(connection != null) {
                try {
                    // 使用了数据库连接池中的连接，所以是 “归还连接”不是关闭连接
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出

                }
            }
        }


    }

}
