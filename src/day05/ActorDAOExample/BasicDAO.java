package day05.ActorDAOExample;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class BasicDAO<T> {  // 定义抽象类,抽象类不可以new

    /**
     * 处理查询单个特殊的查询操作 max() count()
     *  使用 dbutils封装的工具类，操作数据库，以及执行sql语句，处理select查询的结果集
     *  ScalarHandler：查询单个值对象
     * @param sql
     * @param args
     * @return Object
     */
    public Object getScalarHandler(String sql,Object...args) {
        // 1. 注册驱动，2.连接数据库，这里通过使用 dbutils 数据库连接池获取到其中的一个连接对象
        Connection connection = getDruidConnection();

        // 2.创建dbutils封装的工具类对象,调用其中封装的方法操作数据库，执行sql语句,处理select结果集
        QueryRunner queryRunner = new QueryRunner();

        // 创建存储select 查询结果集的对象这里是 : ScalarHandler 单个值对象
        ScalarHandler scalarHandler = new ScalarHandler();

        // 执行sql语句,以及处理select 查询的结果集对象
        Object object = null;
        try {
            object = queryRunner.query(connection, sql, scalarHandler, args);
            return object;
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出.
        } finally {
            // 关闭资源,最晚使用的最先关闭,如果使用数据库连接池，则是 “归还连接”,不是关闭连接
            close(null,null,connection);
        }

        // 或者将创建select 查询的结果集对象和执行sql语句以及处理select查询的结果封装为一体
//        Object o = queryRunner.query(connection, sql, new ScalarHandler(), args);

    }






    /**
     * 查询所有记录
     * 使用dbutils 封装的工具类，执行sql语句操作数据库，
     * MapListHandler：将select查询的结果集中的"所有"每一行数据都封装到一个Map里，然后再存放到List
     * @param sql
     * @param args
     * @return List<Map<String,Object>>
     */
    public List<Map<String,Object>> getMapListHandler(String sql,Object...args) {
        // 1. 注册驱动，连接数据库,使用从 druid数据库连接池中获取到的其中的一个连接对象
        Connection connection = getDruidConnection();

        // 2. 创建dbutils 封装的工具类对象,用于调用其中的方法操作数据库以及处理select 查询的结果集
        QueryRunner queryRunner = new QueryRunner();

        // 创建存储select 查询的结果集对象,这里我们使用的是: MapListHandler
        MapListHandler mapListHandler = new MapListHandler();
        // 执行sql语句,并处理select 查询的结果集数据
        List<Map<String,Object>> list = null;
        try {
            list = queryRunner.query(connection, sql, mapListHandler, args);
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
        }

        // 或者将创建select查询结果集的对象，以及执行sql语句，处理结果集一体化
        // List<Map<String, Object>> mapList = queryRunner.query(connection, sql, new MapListHandler(),args);
    }



    /**
     * 查询一条记录
     * 使用 dbutils 封装的工具类进行操作数据库以及处理select 查询的结果集
     * MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值。
     * @param sql
     * @param args
     * @return  Map<String,Object>
     */
    public Map<String,Object> getMapHandler(String sql, Object...args) {
        // 1. 注册驱动，连接数据库，这里使用从 druid数据库连接池中获取其中的一个连接对象
        Connection connection = getDruidConnection();

        // 创建Dbutils 封装的工具类的对象，用于调用其中的方法，操作数据库和处理select 查询的结果集
        QueryRunner queryRunner = new QueryRunner();

        // 创建存储select 查询结果集的集合对象，这里我们使用 MapHandler 对象
        MapHandler mapHandler = new MapHandler();
        try {
            // 执行sql语句,处理select的结果集
            Map<String,Object> map = queryRunner.query(connection,sql,mapHandler,args);// 可变参数可以不传参数,不要传null

            // 或者将创建存储select 查询的结果集,和执行sql语句,处理select查询的结果集一体化
            // Map<String, Object> map = queryRunner.query(connection, sql, new MapHandler(), args);
            return map;
        } catch (SQLException e) {
            throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
        } finally {
            // 6.关闭资源(最晚使用的最先关闭)，如果使用的数据库连接池，则是“归还连接” 不是关闭连接
            close(null,null,connection);
        }

    }



    /**
     *  使用 Dbutils封装的工具类，处理 "多条" 记录
     *  ArrayListHandler：把 select查询的 结果集中的每一行数据都转成一个数组，再存放到List集合链表当中
     * @param sql
     * @param clazz
     * @param args
     * @return List<Object[]>
     */
    public List<Object[]> getArrayListHandler(String sql, Class<T> clazz, Object...args) {
        // 1. 注册驱动，2. 连接数据库，这里通过从 druid  数据库连接池中获取到其中的一个连接对象
        Connection connection = getDruidConnection();

        // 2. 创建Dbutils封装的工具类的对象，用于调用其中的方法操作数据库以及处理select 查询的结果集
        QueryRunner queryRunner = new QueryRunner();

        // 3. 创建存储select 查询结果集的集合对象，这里我们使用: ArrayListHandler
        ArrayListHandler arrayListHandler = new ArrayListHandler();
        // 执行sql语句,并处理select 查询的结果集

        try {
            List<Object[]> list= queryRunner.query(connection, sql, arrayListHandler, args);

            // 或者将创建存储select 查询结果集的对象,和 执行sql语句以及处理select 查询的结果集一体化
            // List<Object[]> query = queryRunner.query(connection, sql, new ArrayListHandler(), args); //
            // 可变参数可以不传参数，但是不要传null(防止null引用,报错)

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
        } finally {
            // 关闭了资源,最晚使用的最先关闭,如果使用数据库连接池，则是“归还连接” 不是关闭连接
            close(null,null,connection);
        }

    }


    /**
     * 使用 Dbutils 封装的工具类，处理 “一条” 记录
     * ArrayHandler：把select 查询到的结果集中的第一行数据转成对象数组。存储起来
     * @param sql
     * @param clazz
     * @param args
     * @return Object[]
     */
    public Object[] getArrayHandler(String sql,Class<T> clazz,Object...args) {
        // 1. 注册驱动，2.获取连接，通过dbutils 数据库连接池中获取到其中的一个连接对象
        Connection connection = getDruidConnection();


        // 3. 执行sql语句,并处理select 查询的结果集
        QueryRunner queryRunner = new QueryRunner();   // 创建dbutils工具类的对象，用于调用其中的封装的方法

        // 2. 创建存储select 查询的结果集的对象,这里我们使用的是: ArrayHandler “以数组的形式存储一条记录”
/*        ArrayHandler arrayHandler = new ArrayHandler();
        Object[] arr = queryRunner.query(connection, sql, arrayHandler, args);*/

        Object[] arr = {};
        try {
            // 也可以将创建select 查询结果集的存储对象，和执行 QueryRunner.query()结合在一起
             arr = queryRunner.query(connection, sql, new ArrayHandler(), args);
        } catch (SQLException e) {
            throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
        } finally {
            // 关闭资源,(最晚使用的最先关闭),如果是数据库连接池，则是 “归还连接” ,不是关闭连接
            close(null,null,connection);
        }
        return arr;

    }

    /**
     * 使用 dbutils 工具类封装处理 “多条” 记录
     * BeanListHandler：将select查询的结果集所有的每一行数据都封装到一个对应的JavaBean(ORM映射)实例中，存放到List里。
     * @param sql
     * @param clazz
     * @param args
     * @return List<T>
     */
    public List<T> getBeanListHandler(String sql, Class<T> clazz, Object...args) {
        // 1. 注册驱动，2.获取连接，通过 dbutils 数据库连接池获取其中的一个连接对象
        Connection connection = getDruidConnection();



        QueryRunner queryRunner = new QueryRunner();   // 创建dbutils工具类对象，用于使用其中的封装的的方法

        List<T> list = null;   // ResultSetHandler接口中的BeanListHandler返回类型.
        try {
            // 3. 执行sql语句,并处理select 查询的结果集,
            // 2. 创建存储select 查询的结果集的对象,这里我们使用: BeanListHandler
            BeanListHandler<T> beanListHandler = new BeanListHandler<T>(clazz);  // 泛型
            // ResultSetHandler接口中的BeanListHandler返回查询select查询到的结果集的 javaBean实例对象,存放到list集合中
            list = queryRunner.query(connection, sql, beanListHandler, args);

            // 或者将创建select 存储对象和执行sql语句一体化
//            list = queryRunner.query(connection, sql, new BeanListHandler<T>(clazz), args);

        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        } finally {
            // 关闭资源(最晚使用的最先关闭),如果是从数据库连接池中获取到连接对象则是 “归还连接”,不是关闭连接
            close(null,null,connection);
        }
        return list;

    }


    /**
     * 使用 dubitls 封装的工具类进行查询处理操作 ，“处理一条” 记录
     * BeanHandler：将select查询到的结果集中的第一行数据封装到一个对应的JavaBean(orm映射类中)实例中。存储起来
     * @param sql
     * @param clazz
     * @param args
     * @return T 一条记录
     */
    public T getBeanHandler(String sql ,Class<T> clazz,Object...args) {
        // 获取到 driod 数据库连接池的其中一个连接对象
        Connection connection = getDruidConnection();

        // 创建Dbutils 封装工具类的对象,使用其中的封装的方法
        QueryRunner queryRunner = new QueryRunner();


        // 执行sql语句，并处理select 查询的结果集
        T t = null;  // 创建queryRunner 处理结果集的返回类型

        // 执行sql语句,返回类型
        try {
            // 创建存放 select 结果集中实例对象,使用泛型,这里我们使用  BeanHandler<T> 类型 "将查询的一行记录存储到javaBean orm 实例对象中"
            BeanHandler<T> beanHandler = new BeanHandler<>(clazz);
            t = queryRunner.query(connection, sql, beanHandler, args);


            // 或者创建存储对象和执行sql语句一体化
//            t = queryRunner.query(connection, sql, new BeanHandler<>(clazz), args);

        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        } finally {
            // 6.关闭资源连接,最晚使用的最先释放,使用的数据库连接池,则就是“归还连接”
            close(null,null,connection);
        }
        return t;

    }


    /**
     * 使用 dbutils工具类，封装通用数据表的增删改
     * @param sql sql语句
     * @param args args 填充的占位符
     * @return int 返回影响数据库的记录
     */
    public int update(String sql, Object...args) {
        // 通过自行封装的 Druid数据库连接池中获取连接对象
        Connection connection = getDruidConnection();
        // 通过 dbutils 工具类创建数据库操作对象
        QueryRunner queryRunner =  new QueryRunner();
        int updateCount = 0;   // 影响数据库的行数
        try {
            updateCount = queryRunner.update(connection,sql,args);  // 返回影响数据库的行数记录
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        } finally {
            //  关闭资源，使用了数据库连接池就是 “归还连接”不是关闭连接
            close(null,null,connection);
        }
        return updateCount;
    }





    private static DataSource dataSource = null;     // druid 数据库连接池的对象，同样以类属性的形式存在,
    // 注意静态的static 不用使用 this引用,类中不可以调用方法(方法的调用是在“代码块中，静态代码块，方法代码块”)
    static {  // static 静态代码块，和类一起加载到堆区当中,仅仅只是执行一次,所有对象共用,符合一个数据库连接池的构想

        Properties properties = new Properties();   // 创建获取.properties后缀的配置文件的信息的对象
        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties")); // 获取到指定路径下的配置文件的信息
            properties.load(is);  // 以简单的线性格式输入字符获取到属性列表(关键字/元素对)

            // 传入获取配置文件的信息的对象，创建druid 数据库连接池
            dataSource = DruidDataSourceFactory.createDataSource(properties);  // 注意返回类型是 DataSource jdbc 接口的实现类
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出。
        }

    }

    /**
     * 通过 druid 数据库连接池获取到连接对象
     * 注意我们只需要一个数据库连接池，当连接不够时，通过增加数据库连接池中的连接数量.不要创建多个数据库连接池
     * @return Connection
     */
    private static Connection getDruidConnection() {
        Connection connection = null;
        // 从druid 数据库连接池获取到其中的一个连接对象
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出。
        }
        return connection;   // 返回从数据库连接池中获取到的对象
    }




    /**
     * 使用 DButils 工具类关闭资源(最晚使用的最先关闭),
     * 如果是从数据库连接池中获取到的连接，则是“归还连接” 并不是关闭连接
     * @param resultSet
     * @param statement
     * @param connection
     */
    private static void close(ResultSet resultSet, Statement statement, Connection connection) {
        // 方式一；需要处理异常,
        /*try {
            DbUtils.close(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

        try {
            DbUtils.close(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

        try {
            DbUtils.close(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }*/

        // 方式二: 不用处理异常,它自动处理了异常

        DbUtils.closeQuietly(resultSet);
        DbUtils.closeQuietly(statement);
        DbUtils.closeQuietly(connection);

    }

}
