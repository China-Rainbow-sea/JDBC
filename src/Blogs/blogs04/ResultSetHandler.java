package Blogs.blogs04;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ResultSetHandler {
    public static void main(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件
        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();


            // 创建存储 select 查询的存储方式：这里是  ScalarHandler 处理特殊的查询结果 比如: max(),sum(),count()
            ScalarHandler scalarHandler = new ScalarHandler();
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select count(*) from customers";
            // connection 连接，sql :sql语句，columnListHandler:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            long count = (long)queryRunner.query(connection,sql,scalarHandler);    // 强制转换为你所需要的对应的数据类型
            System.out.println("count: "+count);

            // 可以将创建存储形成，和执行sql，一体化 如下：
            String sql2 = "select max(id) from customers";
            int max = (int)queryRunner.query(connection,sql2,new ScalarHandler());  // 强制转换为你所需要的对应的数据类型
            System.out.println("max: "+max);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }



    public static void main7(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件
        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();

            // 创建存储 select 查询的存储方式：这里是  ColumnListHandler 存储 "某一列中所有的" 记录，以List链表的形式存储起来单条记录
            ColumnListHandler columnListHandler = new ColumnListHandler(1);  // 注意的是 在jdbc 中起始下标是从 1开始的不是从 0 开始的
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();

            String sql = "select id,name,email,birth from customers where id < ?";
            // connection 连接，sql :sql语句，columnListHandler:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            List<Object> list = queryRunner.query(connection, sql, columnListHandler, 30);
            list.forEach(System.out::println);


            System.out.println("****************************************************************");
            // 可以将创建存储形成，和执行sql，一体化 如下：
            List<Object> list2 = queryRunner.query(connection, sql, new ColumnListHandler("name"), 30);  // 可以是列名(别名    )
            for(Object o : list2) {
                System.out.println(o);
            }


            System.out.println("****************************************************************");
            // 没有查询到的返回null
            List<Object> list3 = queryRunner.query(connection, sql, new ColumnListHandler(1), 0);
            list3.forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }


    public static void main6(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件
        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();


            // 创建存储 select 查询的存储方式：这里是  MapListHandler 存储 "多条" 记录，以键值对的形式存储起来单条记录，再将单条记录全部存储到List链表中
            MapListHandler mapListHandler = new MapListHandler();
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();
            // connection 连接，sql :sql语句，MapListHandler:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            String sql = "select id,name,email,birth from customers where id < ?";
            List<Map<String, Object>> mapList = queryRunner.query(connection, sql, mapListHandler, 30);
            mapList.forEach(System.out::println);


            System.out.println("****************************************************************");
            // 可以将创建存储形成，和执行sql，一体化 如下：
            List<Map<String, Object>> mapList2 = queryRunner.query(connection, sql, new MapListHandler(), 30);
            for(Map m : mapList2) {
                System.out.println(m);
            }


            System.out.println("****************************************************************");
            // 没有查询到的返回null
            List<Map<String, Object>> mapList3 = queryRunner.query(connection, sql, mapListHandler, 0);
            mapList3.forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

    }



    public static void main5(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件
        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();


            // 创建存储 select 查询的存储方式：这里是  MapHandler 存储 "一条" 记录，以键值对的方式存储一条记录
            MapHandler mapHandler = new MapHandler();
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();

            // connection 连接，sql :sql语句，MapHandler:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            String sql = "select id,name,email,birth from customers where id = ?";
            Map<String, Object> map = queryRunner.query(connection, sql, mapHandler, 10);
            System.out.println(map);

            // 可以将创建存储形成，和执行sql，一体化 如下：
            Map<String, Object> map2 = queryRunner.query(connection, sql, new MapHandler(), 10);
            System.out.println(map2);

            // 没有查询到的返回null
            Map<String, Object> map3 = queryRunner.query(connection, sql, new MapHandler(), 0);
            System.out.println(map3);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }



    public static void main4(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件
        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();


            // 创建存储 select 查询的存储方式：这里是  BeanListHandler<> 存储 "多条" 记录，javaBean实例存储一行记录，再将多行JavaBean存储到List链表当中
            BeanListHandler<Customer> beanListHandler = new BeanListHandler<>(Customer.class);
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth from customers where id < ?";
            // connection 连接，sql :sql语句，beanListHandler:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            List<Customer> customersList = queryRunner.query(connection, sql, beanListHandler, 30);
            customersList.forEach(System.out::println);
            System.out.println("****************************************************************");

            // 可以将创建存储形成，和执行sql，一体化 如下：
            List<Customer> list = queryRunner.query(connection, sql, new BeanListHandler<Customer>(Customer.class), 30);
            for(Customer c : list) {
                System.out.println(c);
            }


            System.out.println("****************************************************************");
            // 没有查询到的返回null
            List<Customer> query = queryRunner.query(connection, sql, new BeanListHandler<Customer>(Customer.class),
                    -1);
            query.forEach(System.out::println);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }



    public static void main3(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件
        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();


            // 创建存储 select 查询的存储方式：这里是  BeanHandler<> 存储 "一条" 记录，javaBean实例对象存储起来单条记录
            BeanHandler<Customer> beanHandler = new BeanHandler<>(Customer.class);
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth from customers where id = ?";
            // connection 连接，sql :sql语句，BeanHandler<>:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            Customer customer = queryRunner.query(connection, sql, beanHandler, 10);
            System.out.println(customer);


            // 可以将创建存储形成，和执行sql，一体化 如下：
            Customer customer1 = queryRunner.query(connection, sql, new BeanHandler<>(Customer.class), 10);
            System.out.println(customer1);


            // 没有查询到的返回null
            Customer customer2 = queryRunner.query(connection, sql, new BeanHandler<>(Customer.class), 100);
            System.out.println(customer2);
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }


    public static void main2(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件


        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();


            // 创建存储 select 查询的存储方式：这里是  ArrayListHandler 存储 "多条" 记录，以数组的形成存储起来单条记录，再存入到List链表当中
            ArrayListHandler arrayListHandler = new ArrayListHandler();
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth from customers where id < ?";
            // connection 连接，sql :sql语句，ArrayListHandler:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            List<Object[]> list = queryRunner.query(connection, sql, arrayListHandler, 30); // 执行sql语句
            list.forEach(System.out::println);


            // 可以将创建存储形成，和执行sql，一体化 如下：
            List<Object[]> query = queryRunner.query(connection, sql, new ArrayListHandler(), 30);
            query.forEach(System.out::println);

            // 没有查询到的返回null
            List<Object[]> query2 = queryRunner.query(connection, sql, new ArrayListHandler(), -1);
            query.forEach(System.out::println);

        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

    }





    public static void main1(String[] args) {
        // 读取配置文件方式一:
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 默认是读取src
        // 目录下的配置文件

        try {
            // 读取配置文件方式二:
            FileInputStream io = new FileInputStream(new File("src/druid.properties"));

            // 创建 Properties对象用于获取到以读取的配置信息参数
            Properties properties = new Properties();
            properties.load(io);   // 以简单的线性格式读取属性列表(关键字/元素对)
            // 通过传入读取配置文件的对象，创建 druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            // 获取到 druid 数据库连接池中的连接
            Connection connection = dataSource.getConnection();


            // 创建存储 select 查询的存储方式：这里是 ArrayHandler 存储一条记录，以数组的形成存储起来
            ArrayHandler arrayHandler = new ArrayHandler();
            // 创建 QueryRunner 对象使用其中的 ResultSetHandler接口中的方法
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select id,name,email,birth from customers where id = ?";   // 占位符不要加单引号,不然就成了字符串了
            // connection 连接，sql :sql语句，arrayHandler:存储形式，params 可变参数：填充占位符，可以不传参数，但不要传null
            Object[] arrs = queryRunner.query(connection, sql, arrayHandler, 10); // 执行sql语句,并处理select结果集
            System.out.println(Arrays.toString(arrs));  // 打印查询到的信息


            // 可以将创建存储形成，和执行sql，一体化 如下：
            Object[] query = queryRunner.query(connection, sql, new ArrayHandler(), 13);
            System.out.println(Arrays.toString(query));  // 打印查询到的信息


            // 没有查询到的返回null
            Object[] not = queryRunner.query(connection, sql, new ArrayHandler(), 100);
            System.out.println(Arrays.toString(not));  // 打印查询到的信息

        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }
}
