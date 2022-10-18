import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/*
1. JDBC 是什么 ?
java DataBase Connectivity(java语言连接数据库)
2. JDBC 的本质是什么
jdbc 是SUN 公司制定的一套接口(interface)
接口都有调用者和实现者
面向接口调用,面向接口写实现类,这都属于面向接口编程
3.为什么要面向接口编程
 解耦合:降低程序的耦合度,提高程序的扩展力
 多态机制就是非常典型的:面向抽象编程(不要面向具体编程)
 */

/**
 * Java程序连接数据库的五种方式:
 * JDBC 编程六步
 * 1.注册驱动: (说明一下你连接的是哪个公司的数据库),
 * 2.连接对应(注册驱动中的数据库):表示Jvm的进程和数据库进程之间的通道打开,这属于进程之间的通信,
 * 使用完后,一定要记住需要关闭它
 * 3.获取数据库操作对象(创建专门执行 sql 语句的对象)
 * 4. 执行sql语句(DOL DML...)
 * 5. 处理select 查询的结果集(只有当第四步执行的是 select 语句的时候,才有
 * 这第五步处理查询结果集)
 * 6. 释放资源(使用完资源之后,就一定要关闭资源),连接的资源有限,防止过多而无法再连接资源了
 * 一般写在finally 中一定会被执行
 */

public class TestJdbc {
    // 方式一:
    @Test // 测试
    public void ConnectionTest() throws SQLException { // throws SQLException 抛出异常

        // 1. 注册驱动(表明你要连接的数据库是什么)
        // 接口(java.sql.Driver)                 接口的实现类(com.mysql.jdbc.Driver)
        java.sql.Driver driver = new com.mysql.jdbc.Driver(); // 多态,接口引用实现类,动态绑定
        // Driver 默认是注册驱动的一个接口(java.sql.Driver),接口是不可以new的我们需要找到实现这个接口的类
        // 刚好实现这个Driver的接口的类名也叫Driver 所以为了防止冲突歧义,我们需要通过
        // 路径new 该Driver 类的对象(com.mysql.jdbc.Driver),从而注册驱动
        // 其中接口 java.sql.Driver 接口可以通过导入包,省略掉,需要注意的是,我们多个包中存在同一个类名存在歧义

        // 2. 连接上对应(注册驱动)时的数据库
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";  // 注意不要存在空格防止无法识别
        /*
        url 表示:统一资源定位符(网络中某个资源的绝对路径),其变量名是规定好的无法修改,赋值根据实际情况
        url 包括:协议,ip地址(域名),端口号,
        jdbc:mysql 协议
        localhost ip 地址 127.0.0.1
        3306 Mysql 默认的端口号
        test: 表示数据库中的具体的实例数据库名
        jdbc:mysql://localhost:3306/test 总的来说就是你所连接的数据库在网络上的地址
        ?useUnicode=true&characterEncoding=utf8 对应Mysql5.7存在编码字符集不匹配的问题(255),
        我们需要手动修改其字符集编码
         */

        // 将用户名和密码封装到 Properties 中,让其自动获取
        Properties properties = new Properties();
        properties.setProperty("user","root");   // 用户名
        properties.setProperty("password","MySQL123");  // 密码

        /*
        user(用户名)和password(密码) 的变量名是规定好的,不可以修改,
        其赋值,根据具体情况赋值
         */

        // Connection 是一个接口   driver.connect()尝试连接数据库,(尝试可能失败,需要处理该失败的异常)
        Connection connection = driver.connect(url,properties);
        System.out.println("方式一:"+connection);  // 打印连接数据库的地址
        // 方式一:com.mysql.jdbc.JDBC4Connection@68f7aae2
        // 发现没有和 com.mysql.jdbc.Driver很像,

        // 3. 获取操作数据库的对象
        // 4. 通过对象执行SQL语句
        // 5. 当SQL是select 查询结果集时,处理
        // 6. 关闭资源 一般是放在 finally (无论是否异常都会执行)



    }



    // 方式二 使用 DriverManager 代替driver.connect(url,properties);统一管理数据库
    @Test  // 测试
    public void ConnectionTest2() throws SQLException {

        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        // 1. 注册驱动,使用public static void registerDriver(Driver driver) 注册驱动,说明你要连接的数据库是什么
        // 注意其中的参数是 Driver ,默认import java.sql 包下是一个接口,接口是不可以实例化的,不可以实例化,我们
        // 只能使用多态(接口引用其实例类) 和 interface Driver 同名的类 Driver 在 package com.mysql.jdbc.Driver
        // 存在歧义我们需要指定包下 new Driver 对象

        // 或者我们也可以使用如下方法：分开写
        // java.sql.Driver package下的接口            comm.mysql.jdbc.Driver() package 下的类
        java.sql.Driver driver = new com.mysql.jdbc.Driver(); // 多态,接口引用实现类(动态绑定)
        // 可以通过导入 import java.sql.Driver 可以省略
        DriverManager.registerDriver(driver);  // 这样我们的 DriverManager.registerDriver参数就是driver类了


        // 2. 连接(注册的驱动)的数据库使用 public static Connection getConnection(String url,
        //                                       String user,
        //                                       String password)
        // 尝试建立连接,尝试可能失败,所以我们需要进行失败异常处理一下
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8"; // 数据库在网络上的位置
        String user = "root"; // 用户名, user 变量名是规定好的了,不可修改,值根据实际情况赋值
        String password = "MySQL123"; // 密码, password 变量名  是规定好了的,不可修改,值根据实际情况赋值
        /*
        jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8"
        url 统一资源定位符,是规定好了的,不可修改,值
        jdbc:mysql 是协议,localhost(域名) ip 地址,3306 mysql 默认端口,test 具体实例数据库,
        // ?useUnicode=true&characterEncoding=utf8 设置其mysql 的字符集编码,防止 255报错
         */

        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println("方式二:"+connection); // 打印连接的数据库的地址
        // 方式二:com.mysql.jdbc.JDBC4Connection@4c3e4790

        // 3. 获取操作数据库的对象
        // 4. 根据对象执行SQL语句(DQL DML)
        // 5. 如果第4步执行的是 select 查询返回结果集,处理该结果集
        // 6. 关闭资源,从小到大,一般放入到 finally 中一定会被执行(无论是否存在异常);


    }

}




/*

 Driver 接口的实现类 包位置是 package com.mysql.jdbc;
public class Driver extends NonRegisteringDriver implements java.sql.Driver {

	static {
		try {
			java.sql.DriverManager.registerDriver(new Driver());
		} catch (SQLException E) {
			throw new RuntimeException("Can't register driver!");
		}
	}

    public Driver() throws SQLException {

    }


 */


/*
    http: 表示的是通信协议
    什么是通信协议,有什么用 ???
    通信协议是在通信之前就提前定好的数据传送格式,数据包具体怎么传出的数据的，以什么样的格式传送的
    action(url)name=value&name=value&...
    比如: 你说中文,别人说英文,语言不同,协议不同,就无法交流,无法通信,
 */


/*
    Interface Connection
    Connection connect(String url,
                       Properties info)
                throws SQLException
 尝试使数据库连接到给定的URL。
         如果驱动程序意识到连接到给定的URL是错误的驱动程序，
          驱动程序应该返回“null”。 这将是常见的，因为当JDBC驱动程序管理器被要求连接到给定的URL时，
             它会依次将URL传递给每个加载的驱动程序。
 */


/*
    public class DriverManager
    extends Object用于管理一组JDBC驱动程序的基本服务。
    注意： JDBC 2.0 API中新增的DataSource接口提供了另一种连接到数据源的方法。
    使用DataSource对象是连接到数据源的首选方法。


 */


/*


    public static void registerDriver(Driver driver)
                           throws SQLException
 注册与给定的驱动程序DriverManager 。
                  新加载的驱动程序类应该调用方法registerDriver使其自己已知的DriverManager
 */



/*
public static Connection getConnection(String url,
                                       String user,
                                       String password)
                                throws SQLException
     尝试建立与给定数据库URL的连接。
            DriverManager尝试从一组已注册的JDBC驱动程序中选择适当的驱动程序

public static Connection getConnection(String url)
                                throws SQLException
   尝试建立与给定数据库URL的连接。 DriverManager尝试从一组已注册的JDBC驱动程序中选择适当的驱动程序。
 */