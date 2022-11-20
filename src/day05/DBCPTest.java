package day05;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 关于DBCP 数据库连接池的测试
 * 1. 导入相关的jar包。Commons-dbcp.jar连接池的实现 以及 Commons-pool.jar：连接池实现的依赖库
 */
public class DBCPTest {
    // 方式一: 通过传入参数的方式创建 DBCP数据库连接池,来获取到连接，不推荐
    public static void main1(String[] args) {
        BasicDataSource dbSource = new BasicDataSource();  // 创建dbcp的数据库连接池对象
        Connection connection = null;
        // 设置注册驱动/连接数据库url的基本信息
        dbSource.setDriverClassName("com.mysql.cj.jdbc.Driver");  // 注册驱动(说明你要连接的数据库的品牌)

        dbSource.setUrl("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true");  // 实际连接的数据库url位置
        dbSource.setUsername("root");  // 用户名
        dbSource.setPassword("MySQL123");  // 密码

        // 还可以设置其他涉及数据库连接池管理的相关属性
        dbSource.setInitialSize(10);   // dbcp数据库连接池启动时，创建的初始化连接数量.
        dbSource.setMaxActive(20);     // dbcp数据库连接池可同时连接的最大连接数量;

        try {
            connection = dbSource.getConnection();  // 获取到dbcp数据库连接池中其中的一个连接对象
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转化为运行时异常抛出
        }

        System.out.println(connection);
        System.out.println(connection.getClass());

    }


    // 方式二: 通过创建配置文件信息: xxx.properties后缀的文件信息，根据配置文件的信息创建数据库连接池
    /*
    在 src 目录中导入以xxx.properties 后缀的文件名(后缀名不可更改),并配置相关参数信息
     */
    public static void main2(String[] args) {
        Properties properties = new Properties();   // 读取I/O文件内容的对象

        // 读取指定文件内容，方式一:
        /*InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        properties.load(is);
        // 通过传入读取到配置文件(.properties)对象，创建dbcp数据库连接池.
        DataSource dbSource = BasicDataSourceFactory.createDataSource(properties);
        */

        try {
            // 读取指定文件内容，方式二:
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            properties.load(is);
            // 通过传入的读取到配置文件(properties)的对象，创建dbcp数据库连接池
            DataSource dbSource = BasicDataSourceFactory.createDataSource(properties); // 注意createDataSource
            // 返回类型是DataSource 接口
            Connection connection = dbSource.getConnection();  // 通过dbcp数据库连接池获取到连接池中的连接对象

            System.out.println(connection);
            System.out.println(connection.getClass());  // 打印显示该类所在的包(路径);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    // 测试dbcp 连接 5000/ 5W次所消耗的时间
    public static void main(String[] args) {
        Properties properties = new Properties(); // 读取配置文件properties的对象
        Connection connection = null;   // 数据库连接对象
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            properties.load(is);

            // 通过传入读取到配置文件(properties)的信息,创建dbcp数据库连接池
            DataSource source = BasicDataSourceFactory.createDataSource(properties);

            long start = System.currentTimeMillis();  // 5000/5w连接前的时间点 单位：毫秒

            for(int i = 0; i < 5000000; i++) {
                connection = source.getConnection();

                connection.close(); // 释放从dbcp数据库连接池获取到的连接(归还给dbcp数据库连接池),并不是关闭连接
            }

            long end = System.currentTimeMillis();  // 处理完5000/5w个连接的后的时间点 单位：毫秒

            System.out.println("5000/5w连接所消耗的时间: "+ (end - start)); // 5000 次: 672，5w次：691 50w次：839 500w次：2114
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转化为运行异常抛出
        }

    }
}
