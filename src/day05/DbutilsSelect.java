package day05;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
ResultSetHandler 接口及实现类
该接口用于处理 java.sql.ResultSet 将数据按要求转换为另一种形式
ResultSetHandler 接口提供了一个单独的方法：Object handle (java.sql.ResultSet,rs)
接口的主要实现类
    1. ArrayHandler: select 查询的结果集中的第一行数据转换成对象数组存放起来
    2. ArrayListHandler: 把select 查询到的所有结果集中的每一行数据都转成一个数组，再存放大宋List中。
    3. BeanHandler: 将select 查询的结果集中的第一行数据封装到一个对应的javaBean(ORM映射类)实例中
    4. BeanListHandler: 将select查询的结果集中所有每一行数据都封装到一个对应的 javaBean(ORM映射类)的实例当中，并将实例存放到List里
    5. ColumnListHandler: 将select 查询的结果集中某一列的数据存放到List中
    6. KeyedHandler(name): 将 select 查询的结果集中所有的每一行数据都封装到一个Map里，再把这些map再存到一个map里，
    其 key 为指定的key
    7.MapHandler: 将select 查询的结果集中的第一行数据封装到一个Map里，key 是列名,value就是对应的值
    8.MapListHandler: 将select查询到的结果集中的每一行数据都封装到一个Map里，然后再存放到List中
    9.ScalarHandler: 将select 查询到的单个值对象.



 */
public class DbutilsSelect {
    /**
     * 测试查询
     * BeanHander: 是ResultSetHandler 接口的实现类，用于封装表中的 "一条" 记录
     * 先创建javabean(ORM)映射类
     */
    public static void main1(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();   // 创建读取.properties后缀的配置文件的信息的对象


        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties"); // 获取到指定文件的信息
        try {
            properties.load(is); // 以简单的线性格式输入字符读取属性列表(关键字/元素对)7

            // 1. 通过传入读取到的配置文件对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);  // 注意返回类型是DataSource jdbc接口

            // 2. 获取到druid数据库连接池中的连接对象
            connection = dataSource.getConnection();

            // 3. 通过dbutil工具类获取操作数据库的对象
            QueryRunner queryRunner = new QueryRunner();

            String sql = "select id,name,email,birth from customers where id = ?"; // 占位符不要加单引号

            // 4. 创建相关存储的接口实现对象,用于存放查询的结果集，
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            // 执行sql语句并处理select查询的结果集
            Customer customer = queryRunner.query(connection,sql,handler,10); // 找到返回对象实例,没有找到返回null
            System.out.println(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        } finally {
            // 6. 关闭资源,使用数据库连接池是 “归还连接”
            DbUtils.closeQuietly(connection);
        }

    }


    /**
     * 测试查询：
     * BeanListHandler: 是ResultSetHandler 接口的实现类，用于封装表中的多条记录;构成的集合
     * @param args
     */
    public static void main2(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();  // 创建读取.properties配置文件的对象

        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties"));  // 读取指定文件的信息
            properties.load(is);  // 以简单的线性格式输入字符读取属性列表中的(关键字/元素对);

            //1. 通过传入读取到的配置文件信息的对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 2. 获取到druid数据库连接池中的连接对象
            connection = dataSource.getConnection();

            // 3.通过druild 工具类获取到操作数据库对象
            QueryRunner queryRunner = new QueryRunner();

            String sql = "select id,name,email,birth from customers where id < ?"; // 占位符不要加单引号

            // 创建用于存放select查询到的结果集对象
            BeanListHandler<Customer> handlerList = new BeanListHandler<>(Customer.class);

            // 4.5. 执行sql语句，并处理select 查到的结果集
            List<Customer> list = queryRunner.query(connection,sql,handlerList,30);  // 可变参数可以不传参数,但不要传null
            for(Customer c : list) {
                System.out.println(c);
            }
            // 或者使用
            System.out.println("*************************************************");
            list.forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出;
        } finally {
            // 关闭资源,最晚的最先关闭,使用了数据库连接池 是“归还连接”
            DbUtils.closeQuietly(connection);
        }
    }


    /**
     * 测试dbutils工具类的查询
     * MapHandler: 是ResultSetHandler 接口的实现类，用于封装表中的 “一条” 记录，将字段及相应字段的值作为map中的key 和value值
     * @param args
     */
    public static void main3(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();  // 创建读取.properties配置文件的对象

        InputStream is = ClassLoader.getSystemResourceAsStream("druid.properties"); // 读取配置文件名为druid.properties的信息
        try {
            properties.load(is);  // 以简单的线性格式输入字符读取属性列表中的(关键字/元素对)

            // 1. 通过传入读取到的文件的对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 2. 获取到druid数据库连接池中的连接对象
            connection = dataSource.getConnection();

            // 3. 通过 Dbutils工具类获取操作数据库的对象
            QueryRunner queryRunner = new QueryRunner();

            String sql = "select id,name,email,birth from customers where id = ?";  // 占位符不要加单引号

            // 4. 执行sql语句，并处理select查询的结果集
            // 创建存放select查询到的结果集的数据的集合
            MapHandler mapHandler = new MapHandler();   // 集合存储对象

            Map<String,Object> map = queryRunner.query(connection,sql,mapHandler,10);
            System.out.println(map);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出
        } finally {
            // 6. 关闭资源(最晚使用的最先关闭)，使用了数据库连接池就是 “归还连接”不是关闭连接了
            DbUtils.closeQuietly(connection);
        }

    }


    /**
     * 测试 Dbutils 工具类的查询
     * BeanHandler 是ResultSetHandler 接口实现类，用于封装表中的 “一条” 记录
     * @param args
     */
    public static void main4(String[] args) {
        Connection connection = null;
        Properties properties = new Properties(); // 创建获取.properties 后缀的配置文件的对象


        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties")); // 读取到指定路径的文件信息
            properties.load(is);  // 以简单的线性格式输入字符读取属性列表中的(关键字/元素对)
            // 1. 通过传入读取到配置文件信息的对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 2. 获取到 druid创建的数据库连接池的连接对象
            connection = dataSource.getConnection();

            // 3. 通过Dbutils工具类获取到操作数据库的对象
            QueryRunner queryRunner = new QueryRunner();

            // 4. 执行sql语句，并处理select 查询到结果集
            String sql = "select id,name,email,birth from customers where id = ?"; // 占位符不要加单引号

            // 创建存放select 查询的结果集的集合对象
            BeanHandler<Customer> beanHandler = new BeanHandler<>(Customer.class);

            // 执行sql语句,并存储查询到的结果集
            Customer customer = queryRunner.query(connection,sql,beanHandler,10);
            System.out.println(customer);
        } catch (Exception e) {
            throw new RuntimeException(e); // 将编译异常转换为运行时异常抛出
        } finally {
            // 6. 关闭资源(最晚使用的最先关闭)，使用数据库连接池是 “归还连接” 不是关闭连接
            DbUtils.closeQuietly(connection);
        }

    }


    /**
     * 测试 Dbutils 工具类的查询
     * MapListHandler: 是ResultSetHandler 接口的实现类，用于封装表中的 “多条” 记录
     * 将字段及相应字段的值作为 map 中的 key 和value 将这些map 添加到 List 集合中去
     * @param args
     */
    public static void main5(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();  // 创建获取.properties()后缀的配置文件信息的对象

        InputStream is = ClassLoader.getSystemResourceAsStream("druid.properties"); // 读取指定文件名的信息
        try {
            properties.load(is);  // 以简单的线性格式输入字符读取属性列表中的(关键字/元素对)

            // 1. 通过传入读取druid.properties配置文件的对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties); // 注意返回的是DataSource中jdbc 接口

            // 2. 获取到 druid数据库连接池其中的连接对象
            connection = dataSource.getConnection();

            // 3. 通过dbutils 工具类获取到操作数据库的对象
            QueryRunner queryRunner = new QueryRunner();

            // 4. 执行sql语句,并处理select 查询的结果集
            String sql = "select id,name,email,birth from customers where id < ?";  // 占位符不要加单引号

            MapListHandler mapListHandler = new MapListHandler();  // 创建集合存储结果集

            List<Map<String,Object>> list = queryRunner.query(connection,sql,mapListHandler,23);  // 执行sql语句
            list.forEach(System.out::println);

            System.out.println("***************************或者使用入下方式********************");

            for(Object c : list) {
                System.out.println(c);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        } finally {
            // 6. 关闭连接,最晚使用的最先关闭连接)，使用了数据库连接池则是 “归还连接”不是关闭连接了
            DbUtils.closeQuietly(connection);
        }


    }


    /**
     * 测试 Dbutils 查询
     * 特殊的查询 如 max() ,count()
     * ScalarHandler 特殊查询 max(),count()
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        Properties properties = new Properties();  // 获取到读取.properties后缀的配置文件的对象

        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties"));  // 读取指定路径下的文件信息
            properties.load(is);  // 以简单的线性格式输入字符读取属性列表中的(关键字/元素对)

            // 1. 通过传入读取到druid.properties配置文件信息的对象，创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties); // 注意返回类型是 DataSource jdbc的接口

            // 2. 获取到druid数据库连接池其中的一个连接对象
            connection = dataSource.getConnection();

            // 3. 通过Dbutils 工具类获取到操作数据库的对象
            QueryRunner queryRunner = new QueryRunner();

            // 4. 执行sql语句，并处理select的结果集
            String sql = "select count(*) from customers"; // 在Java当中的sql语句不用加分号结尾，加了报错
            // 同理的可以查询 max
            String sql2 = "select max(birth) from customers";

            // 创建对象，QueryRunner 中的存储对象，存放查到的结果集
            ScalarHandler scalarHandler = new ScalarHandler();

            long count = (long)queryRunner.query(connection,sql,scalarHandler);  // 可变参数可以不传,但不要传null，null引用报错
            Date maxBirth = (Date)queryRunner.query(connection,sql2,scalarHandler);  // 强制转换为Date 日期时间类型
            System.out.println(count);
            System.out.println(maxBirth);
        } catch (Exception e) {
            throw new RuntimeException();  // 将编译异常转换为运行异常抛出
        } finally {
            // 6. 关闭资源,(最晚使用的最先关闭)，使用了数据库连接池则是“归还连接” 不是关闭连接
            DbUtils.closeQuietly(connection);
        }

    }


}
