package day05;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import day05.JDBCUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;

/*
commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库，
它是对JDBC的简单封装，学习成本极低，并且使用dbutils能极大简化jdbc编码的工作量，同时也不会影响程序的性能。
导入相关的jar包 commons-dbutils-1.3.jar
DbUtils:提供如 ：关闭连接，装载jdbc 驱动程序等常规工作的工具类，里面的所有方法都是静态的
主要方法如下:
1. public static void close() throws java.sql.SQLException: Dbutils类提供了三个重载的关闭方法.
这些方法检查所提供的参数是不是null,如果不是的话,它们就关闭Connection,Statement和ResulteSet,需要注意的是
在“数据库连接池”中的 Connection 关闭是"归还连接"不是关闭连接
2. public static void closeQuietly() 这一类方法不仅能在Connection,Statement,和ResultSet 为null的情况下
避免关闭，还能隐藏一些程序中抛出的SQLException异常
3. public static void commitAndClose(Connection conn) throws SQLException 用来提交连接的事务，然后关闭连接(数据库连接池是归还连接)
4. public static void commitAndCloseQuietly(connection conn) ：用来提交连接，然后关闭连接，并且在关闭连接时不抛出SQL异常
5. public static void rollbackAndClose(Connection conn) throws SQLException 允许conn为null,因为该方法内部做了null判断
6. public static boolean loadDriver(java.lang.String.driverClassName) 这一方装载并注册jdbc驱动程序，如果
成功就返回 true,使用该方法，你不需要捕捉这个异常ClassNotFoundException

QueryRunner类
该类简单化了SQL查询,它与ResultSetHandler 组合在一起使用可以完成大部分的数据库操作，能够大大减少编码量：
QueryRunner类提供了两个构造器:
   1. 默认的构造器
   2. 需要一个javax.sql.DataSource 来作参数的构造器\

QueryRunner类的主要方法：
 1. 更新
 public int update(Connection conn, String sql ,Object...params) throws SQLException用来执行一个更新(insert,delete,update)操作

 2. 插入
 public <T> insert(Connection conn,String sql,ResultSetHandler<T> rsh,Object...params) throws SQLException
 只支持insert 语句，其中

 3. 批处理
 public int[] batch(Connection conn,String sql,Object params) throws SQLException : insert,update,delete
 public <T> insertBatch(Connection conn, String sql,ResultSetHandler<T> rsh,Object params) throws
 SQLException：只支持insert语句

 4. 查询
 public Object query(Connection conn,String sql ,ResultSetHandler rsh,Object...params) throws SQLException ;执行
 一个查询操作，在这个查询中，对象数组中的每个元素值被用来作为查询语句的置换参数。该方法自行处理PreparedStatement和ResultSet的创建和关闭


 */
public class DbutilsUpdate {

    // 演示插入insert 记录
    public static void main1(String[] args) {
        Connection connection = null;
        // 插入记录测试:
        // 1.通过druid数据库连接池获取连接，
        Properties pros = new Properties();         // 获取配置文件以.properties后缀的文件信息的对象
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 获取配置文件的信息
        try {
            pros.load(is);  // 以简单的线性格式输入字符流读取属性列表(关键字/元素对)

            // 通过传入读取到配置文件druid.properties信息的对象，创建数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(pros);

            // 通过druid数据库连接池，获取其中一个连接对象
             connection = dataSource.getConnection();

            // 测试自行封装的druid数据库连接池
            // connection = JDBCUtils.getDruidConnection();

            //3.通过dbutils获取操作数据库对象
            QueryRunner queryRunner = new QueryRunner();   // 创建操作数据库对象

            String sql = "insert into customers(name,email,birth) values(?,?,?)"; // 占位符不要加单引号,不然就成字符串了失去占位符的作用了

            //4.执行sql语句 连接对象,执行的sql语句,填充占位符
            int insertCount = queryRunner.update(connection, sql, "蔡徐坤", "123@qq.com", "2022-11-20");
            // 执行sql语句返回影响数据库的行数:
            System.out.println("添加了 "+ insertCount+" 条记录");
            // 5. 处理select 查询的结果集
        } catch (Exception e) {
            throw new RuntimeException(e);   // 将编译异常转化为运行时异常抛出
        } finally {
            // 6.关闭资源,注意数据库连接池(是归还连接)不是jdbc.mysql实质的关闭连接
            // 数据库连接池中的Connection,和jdbc.mysql.中的Connection,虽然它们的对象名Connection是一样的,但是它们的实现方式是不同的。
            // jdbc.mysql中的Connection 关闭是真正的关闭连接，而数据库连接池中的Connection是归还连接
            DbUtils.closeQuietly(connection);
        }


    }



    // 测试 dbutils数据库中的 update 修改操作

    public static void main2(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();   // 创建获取.properties后缀的配置文件信息的对象
        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties"));  // 读取该路径下的配置文件信息
            properties.load(is);   // 以简单的线性格式从输入字符流中获取属性列表(关键字/元素对)

            // 1.通过传入读取到的配置文件信息对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);   // 注意返回类型是DataSource中jdbc接口

            // 2. 获取到druid数据库连接池中的一个连接对象
            connection = dataSource.getConnection();
/*            // 测试使用自行封装的 dbcp数据库连接池获取连接
            connection = JDBCUtils.getDbcpConnection();*/

            // 3. 创建操作数据库对象：通过 dbutils工具类
            QueryRunner queryRunner = new QueryRunner();


            String sql = "update customers set name = ? where id = ?";  // 占位符不要加单引号,Java当中sql不要加;分号结束

            // 4. 执行sql语句
            int updateCount = queryRunner.update(connection,sql,"偶像练习生蔡徐坤2",23); // 返回影响数据库的行数

            System.out.println("影响行数: "+updateCount);

            // 5. 处理select 查询的结果集:
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转化为运行时异常抛出
        } finally {
            // 6.关闭资源：使用数据库连接池机制是 "归还连接"
            DbUtils.closeQuietly(connection);
        }

    }


    // 测试 dbutils delete 删除记录
    public static void main(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();  // 创建读取以.properties后缀的配置文件信息的对象

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties"); // 读取指定文件的信息
        try {
            properties.load(is);  // 以简单的线性格式从输入字符读取属性列表(关键字/元素对)

            // 1. 创建数据库连接池,通过传入读取到文件的对象
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 2. 获取到数据库连接池中的连接对象
            connection = dataSource.getConnection();

            /*
            // 测试使用自行封装的c3p0数据库连接池获取连接对象
            connection = JDBCUtils.getC3p0Connection();
*/
            // 3. 通过dbutils工具类，获取操作数据库对象
            QueryRunner queryRunner = new QueryRunner();  // 创建dbutils工具类对象

            String sql = "delete from customers where id = ?";  // 占位符不要加单引号

            // 4. 执行sql语句
            int deleteCount = queryRunner.update(connection,sql,22);

            System.out.println("影响数据库的行数: "+deleteCount);

            // 5. 处理select 查询的结果集
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转化为运行异常抛出
        } finally {
            // 6. 关闭资源(最晚使用的最先释放),数据库连接池是“归还连接”
            DbUtils.closeQuietly(connection);
        }


    }

}
