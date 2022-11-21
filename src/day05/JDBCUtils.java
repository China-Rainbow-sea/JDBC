package day05;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {

    /**
     * c3p0 数据库连接池中获取连接对象
     * @return Connection
     */
    // 一个类中只生成一个数据库连接池就够了.不要创建多个浪费空间和时间。
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");  // new对象定义为类属性存在
    public static Connection getC3p0Connection() {
        Connection connection = null;
        try {
            connection = cpds.getConnection(); // 获取c3p0数据库连接池中的其中的一个连接对象.
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出
        }

        return connection;
    }

    // 错误方式:
    /*
    错误的封装数据库连接池,把创建的数据库连接池定义在了类方法中，每调用一次该方法都会创建一个c3p0数据库连接池,
    太浪费空间和时间了。我们只需要一个数据库连接池就足够了,当我们的连接不够了,可以通过增加数据库连接池的“连接数量”,或者时等待机制

    public static Connection getc3p0Connection() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0"); // 根据c3p0-xml配置文件创建c3p0连接池
        return cpds.getConnection();  // 从c3p0数据库连接池中获取到连接，返回连接对象
    }
    */






    // dbcp 数据库连接池,
    // 数据库连接池一个就足够了,当连接不够时,可以增加数据库连接池中的连接数量,而不是创建多个数据库连接池(从而减少数据库的浪费,以及时间)
    private static DataSource source = null;    // 定义dbcp数据库连接池对象,用于在static静态代码块中使用,以及
    static {

        try {
            Properties properties = new Properties(); // 创建读取properties 配置文件的对象,以类属性的方式存在,
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));  // 读取dbcp.properties配置文件信息
            properties.load(is);   // 方法静态方法，属性不要用this.(引用)
            // 通过传入的读取到配置文件的对象,创建dbcp数据库连接池。
            source = BasicDataSourceFactory.createDataSource(properties);
            /*创一个建数据库连接池就足够了.*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * dbcp 数据库连接池，获取连接对象
     * @return Connection
     */
    public static Connection getDbcpConnection() {

        Connection connection = null;  // 获取到dbcp数据库连接池中的连接对象
        try {
            connection = source.getConnection();   // 从dbcp数据库连接池中获取到连接
        } catch (Exception e) {
            throw new RuntimeException(e);     // 将编译异常转化为运行异常抛出
        }

        return connection;

    }

    // 错误dbcp 错误封装演示:
/*

    Properties pros = new Properties();   // new 的对象 可以作为类属性存在,
    FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
    pros.load(is);  // 在类当中是无法调用方法的，方法的调用需要在(方法代码块中，以及静态代码块中)，报错
    DataSource source = BasicDataSourceFactory.createDataSource(pros);  // 创建dbcp数据库连接池

    public static void Connection getConnection() {
        return source.getConnection(); // 获取到dbcp数据库连接池中的连接对象;
    }
*/







    // druid 数据连接池的封装
    // 一个数据库连接池就足够了,当连接数量不足够时,可以通过扩大数据库连接池中的连接数量,或者等待队列
    private static DataSource dataSource = null;   // druid数据库连接池创建的对象,以类属性的方式存在
    // 类中不可以调用方法,(方法需要在代码块中调用（方法代码块，static静态代码块）)
    static {   // 静态代码块,和类一起加载到内存当中,仅执行一次,所有对象共用
        Properties properties = new Properties();   // 创建读取配置文件的对象
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 读取配置文件中的信息
        try {
            properties.load(is);  // 以简单的线性格式从输入字符流读取属性列表(关键字和元素对)

            // 通过传入读取到配置文件(druid.properties)信息的对象,创建数据库连接池
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);   // 将编译异常转化为运行异常抛出.
        }
    }

    /**
     * druid 数据库连接池，获取连接
     * @return Connection
     */
    public static Connection getDruidConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转化为运行时异常抛出
        }

        return connection;
    }


    /**
     * 使用Dbutils 中提交的DbUtils工具类，实现资源的关闭：
     * 注意了当是数据库连接池中获取到的连接：不是关闭连接而是 “归还连接”，
     * 和 jdbc.mysql包中的Connection是不一样的，在jdbc.mysql包中的Connection是真正的关闭连接，
     * 虽然它们的对象名是一样的但是，DbUtils重写了Connection,实现方式是不同的。关闭的方式也是不同的
     * 数据库连接池中获取到的连接是 “归还给数据库连接池”不是关闭连接
     * 最晚使用的资源，最先关闭
     */
    public static void closeResource(Connection connection, Statement statement, ResultSet resultSet) {
        /*// 方式一: 需要处理异常null
        try {
            DbUtils.close(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DbUtils.close(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            DbUtils.close(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/


        // 方式二: 不用判断异常，直接使用，它帮你处理了异常包括null引用异常
        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(statement);
        DbUtils.closeQuietly(connection);

    }




}
