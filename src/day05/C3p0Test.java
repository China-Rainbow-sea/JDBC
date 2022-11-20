package day05;

/*
在使用开发基于数据库的web程序是，传统的模式基本是按以下步骤：
1. 在主程序如 servlet beans 中建立数据库连接
2. 进行sql操作
3. 断开数据库连接
这中模式开发，存在问题：
1 普通的Jdbc数据库连接使用 DriverManager来获取,每次向数据库建立连接的时候，都要将 Connection 加载到内存中，
再验证用户名和密码(时间开销为 0.05~1s 的时间)需要数据库连接的时候，就向数据库要求一个，执行完成后，再断开连接，这样的方式
会消耗大量的资源和时间，数据库的连接资源并没有得到很好的重复利用，若同时有几百人甚至几千人在线，频繁的进行数据库连接操作将占用
很多的系统资源，严重的甚至会造成服务器的崩溃。
对于每一次数据库连接，使用完后都要断开，否则，如果程序出现异常而未能关闭，将导致数据库系统中内存的泄漏，服务器崩溃。


为此我们采用数据库连接池技术:
1. 为了解决传统开发中数据库连接问题，可以采用数据库连接池技术。
数据库连接池的基本思想：就是为了数据库连接建立一个”缓冲池“。预先在缓冲池中放入一定数量的连接，当需要建立
数据库连接时，只需从“缓冲池”中取出一个，使用完毕之后，再放回连接池中去，注意这时候仅仅只是放回连接池中去，并没有关闭数据库连接
2. 数据库连接池负责分配，管理和释放数据库连接，它允许应用程序重复使用一个现有的数据库连接，而不是重新建立一个新的连接
3. 数据库连接池再初始化时将创建一定数量的数据库连接存放到数据库连接池中去，这些数据库连接的数量时由自定义的最小数据库连接
数来确定的，无论这些数据库连接是否被使用，连接池都将一直保证至少拥有这么多个连接数据量。连接池的最大数据库连接数量限定了这个连接池
能占有的最大连接数，当应用程序向连接池请求的连接超过最大连接数量时，这些多余的请求将会被加入到等待队列中去。

数据库连接池技术的优点：
1. 资源重用：
由于数据库连接得以重用，避免了频繁创建，以及释放连接而引起的大量性能上的开销，在减少系统消耗的基础上。另一方面
也增加了系统运行环境的平稳性。
2. 更快的系统反应速度
数据库连接池在初始化过程中，往往已经创建了若干数据库连接置于连接池中备用，此时连接池的初始化工作均已完成。
对于业务请求处理而言，直接利用现有可用的连接，避免了数据库连接初始化和释放过程的时间开销，从而减少系统响应时间。
3.新的资源分配手段
对于多应用共享同一数据库系统而言，可在应用层通过数据库连接池的配置，实现某一应用最大可用数据库连接数的限制，避免某一
应用独占所有的数据库资源
4. 统一的连接管理，避免数据库连接泄漏。
在较为完善的数据库连接池实现中，可根据预先的占用超时设定，强制回收被占用的连接，从而避免了常规数据库连接操作中可能出现的资源泄漏.
*/

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DBC 的数据库连接池使用javax.sql.DataSource 来表示: DataSource 是一个接口.
 * 该接口通常由服务器(WebLogic,WebSphere，Tomcat) 提供实现，也有一些开源组织提供实现
 *
 * C3P0 是一个开源组织提供的一个数据库连接池，速度相对较慢，稳定性还可以。hibernate官方推荐使用
 * 下面测试C3p0的连接
 */
public class C3p0Test {
    // 1.加载指定的jdr包
    // 方式一:导入配置参数,创建c3p0数据库连接池
    public static void main1(String[] args) {
        ComboPooledDataSource cpds = new ComboPooledDataSource();  // 创建有关c3p0的对象,并创建c3p0数据库连接池
        try {
            cpds.setDriverClass("com.mysql.cj.jdbc.Driver");   // 注册驱动
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");  // 所要连接的实际的数据库url的位置
        cpds.setUser("root");  // 用户名
        cpds.setPassword("MySQL123");  // 密码

        cpds.setInitialPoolSize(10);  // 设置初始化数据库连接池中的连接数量

        // 从c3p0数据库连接池中获取到其中的一个连接对象
        Connection connection = null;
        try {
            connection = cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(connection);  // 打印连接地址
        System.out.println(connection.getClass());  // 打印显示实现类的的(以及所在包路径)

        // 销毁 c3p0 数据库连接池，一般我们是不销毁的
        try {
            DataSources.destroy(cpds);  // 销毁c3p0数据库连接池
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    // 方式二: 使用C3P0数据库连接池的配置文件方式，获取数据库的连接：推荐
    // 在src目录下：创建xml配置文件,注意文件规定必须是名为: c3p0-config.xml 才可以
    public static void main2(String[] args) {
        Connection connection = null;
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");  //根据自动获取helloc3p0中的 c3p0-config.xml 的
        // 配置信息,创建c3p0数据库连接池


        try {
            connection = cpds.getConnection(); // 获取到c3p0数据库连接池中的其中的一个连接对象
        } catch (SQLException e) {
            throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
        }

        System.out.println(connection);   // 打印连接地址
        System.out.println(connection.getClass());  // 打印显示对应的类中的包路径

    }



    // 测试传统连接数据库的方式
    public static void main3(String[] args) {
        Connection connection = null;

        try {
            // 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 获取驱动上的连接
            connection = DriverManager.getConnection("jdbc:mysql:///test?rewriteBatchedStatements=true","root",
                    "MySQL123");  // localhost:3306(本主机) 可以省略改写为jdbc:mysql///test,?rewriteBatchedStatements=true批量处理

            System.out.println(connection);
            System.out.println(connection.getClass());  // 打印所在类的包所在位置(路径)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出
                }
            }
        }

    }


    // 测试传统连接数据库 5000次所消耗的时间:
    public static void main4(String[] args) {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            long start = System.currentTimeMillis();  // 连接之前的时间点:单位: 毫秒
            for(int i = 0; i < 5000; i++) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?rewriteBatchedStatements=true",
                    "root","MySQL123");
            // 传统的连接数据库方式过多连接数据库报错: Data source rejected establishment of connection,  message from server: "Too many connections"
                if(connection != null) { // 防止null引用
                    try{
                        connection.close();  // 关闭连接
                    } catch(SQLException e) {
                        throw new RuntimeException(e);  // 编译异常 ——> 运行异常抛出
                    }
                }
            }
            long end = System.currentTimeMillis();  // 5000次完成的时间点， 单位: 毫秒

            System.out.println("传统的连接数据库5000次所消耗的时间: "+(end - start)); // 42826

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    // 测试使用c3p0连接数据库连接池5000/5W/50w/500wl连接所消耗的时间是多少
    public static void main(String[] args) {
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0"); // 根据c3p0-config.xml配置信息创建c3p0数据库连接池

        try {
            long start = System.currentTimeMillis();  // 连接前的时间点：单位毫秒
            for(int i = 0 ; i < 500000; i++) {
                Connection connection = cpds.getConnection();  // 获取到c3p0数据库连接池中其中的一个连接对象
                if(connection != null) {
                    try {
                        connection.close();  // 注意在c3p0数据库连接池中获取到的连接是 “归还连接,并不是jdbc.mysql中的关闭连接”;
                                             // 虽然connection 对象一样,但是各自实现的方式不同.
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            long end = System.currentTimeMillis();  // 5000 次连接结束的时间点: 单位毫秒

            System.out.println("c3p0数据库连接池5000/5W/50w/500w次连接消耗的时间: " +(end - start));  // 没有归还连接消耗的时间:
                                                                                        // 归还连接的消耗的时间: 643 毫秒
             // 5000次: 639，5w次: 775,50w次: 2098
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}