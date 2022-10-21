package Blogs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

public class blogs01 {
    /**
     * 方式一:
     * @param args
     */
    public static void main1(String[] args) throws SQLException {
        // 1. 注册驱动(说明你要连接的数据库是什么品牌的)
        // java.sql.Driver 是接口, com.mysql.jdbc.Drivers是该接口的实现类
        java.sql.Driver driver = new com.mysql.jdbc.Driver(); // 路径new 类

        // 或者我们导入 java.sql.Driver 可以省略其中的java.sql.Driver

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        /*?useUnicode=true&characterEncoding=utf8 设置字符编码,如果JDBC程序与服务器端的字符集不一致，
        会导致乱码，那么可以通过参数指定服务器端的字符集
         */
        // url 统一资源定位符

        // 将连接数据库的用户名和密码封装到 Properties 中,让其自动获取
        Properties properties = new Properties();
        properties.setProperty("user","root"); // 用户名
        properties.setProperty("password","MySQL123"); // 密码

        // 2.连接上(注册驱动时的)数据库
        // Connection 是一个接口
        Connection connection = driver.connect(url,properties);
        System.out.println("方式一:"+connection);  // 打印连接上的数据库的地址

        // 3. 获取操作数据库对象...这里省略,我们只是连接数据库
        // 4. 通过获取的对象操作数据库，执行SQL语句
        // 5. 如果第四步是 select 查询结果集,处理查询的结果集
        // 6. 关闭资源,一般放在 finally 一定会被执行(无论是否存在异常)

    }



    /**
     *方式二
     * @param args
     * @throws SQLException
     */
    public static void main2(String[] args) throws SQLException {

        // 1. 注册驱动(说明你要连接的是什么品牌的数据库)
        // registerDriver 的参数是 Driver类类型, 而Driver 在默认包下是接口,接口是不可以new的,所以我们需要传入其实现类
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());

        // 或者分两步
        // java.sal.Driver 是接口, com.mysql.jdbc.Driver() 是实现类
        java.sql.Driver driver = new com.mysql.jdbc.Driver();
        DriverManager.registerDriver(driver);

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";  // 所连数据库在网络上的位置
        String user = "root"; // 用户名
        String password="MySQL123";  // 密码

        // 2.连接根据信息连接上(注册驱动)上的数据库  DriverManager.getConnection
        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println("方式二:"+connection);  // 打印连接上数据库的地址

        // 3.获取操作数据库的对象
        // 4.通过对象执行SQL语句
        // 5.如果第4步是select 查询结果集的操作,处理结果集
        // 6.关闭资源,一般放在finally 中.

    }


    /**
     * 方式三
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main3(String[] args) throws ClassNotFoundException, SQLException {
        // 1.注册驱动(说明你要连接的是什么品牌的数据库),这里通过反射加载Driver类,执行Driver类中的 注册驱动的静态方法
        Class.forName("com.mysql.jdbc.Driver"); // 因为我们只是想要执行其中Driver的静态方法,所以我们并不需要接受返回值

        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "MySQL123";

        // 2. 连接驱动
       Connection connection = DriverManager.getConnection(url,user,password);
       System.out.println("方式三:"+connection);  // 打印其连接上数据库的地址

        // 3. 获取操作数据库的对象
        // 4. 通过对象操作数据库，执行SQL语句
        // 5. 如果第四步是 select 是查询结果集,需要处理结果集
        // 6.关闭资源,一般放在 finally 中(一定会被执行,无论是否异常)

    }


    /**
     *方式四: 通过设置配置文件，
     */

    public static void main4(String[] args) throws ClassNotFoundException, SQLException {
        // 使用资源绑定器,绑定属性的配置文件.properties 这个配置文件
        ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
        String driver = bundle.getString("driverClass"); // Driver类包
        String url = bundle.getString("url");    // url 数据库在网络上的地址
        String user = bundle.getString("user");  // user 用户名
        String password = bundle.getString("password");  // password 密码

        // 1. 注册驱动(说明你想要连接的是哪个数据库)
        Class.forName(driver);

        // 2. 连接数据库
        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println("方式四:"+connection);  // 打印连接上数据库的地址

        // 3. 获取操作数据库的对象
        // 4. 通过对象执行SQL语句
        // 5. 如果第四步是 select 查询结果集的话,需要处理结果集
        // 6. 关闭资源,一般放在 finally 中(一定会被执行的,无论是否异常)
    }

    public static void main5(String[] args) throws IOException, ClassNotFoundException, SQLException {
        // 通过Properties对象获取配置文件(jdbc.properties)的信息
        Properties properties = new Properties();
        properties.load(new FileInputStream("src\\jdbc.properties"));
        String driver = properties.getProperty("driverClass");
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");

        // 1. 注册驱动(说明你要连接的什么品牌的数据库)
        Class.forName(driver);

        // 2. 连接(注册驱动上的)数据库
        Connection connection = DriverManager.getConnection(url,user,password);
        System.out.println("方式四:"+connection);

        // 3. 获取操作数据库的对象
        // 4. 通过对象执行SQL语句
        // 5. 如果第四步是 select 查询结果集,则需要处理结果集,不是就不用
        // 6. 关闭资源,一般放在 finally中(一定会被执行)

    }


    public static void main6(String[] args) throws ClassNotFoundException, SQLException {
        // 1. 注册驱动(说明连接的什么品牌的数据库)通过反射加载Driver类,执行Driver类中的静态代码块(注册驱动)
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/dbtest7?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "MySQL123";

        // 2. 连接(驱动上的)数据库
        Connection connection = DriverManager.getConnection(url,user,password);

        // 3. 获取操作数据库的对象
        // Statement 是一个接口,
        Statement statement = connection.createStatement();

        // 设置执行的sqL语句插入语句,注意在java中执行sql语句不要加;分号不然报错
        String sql = "delete from admin where id = 4";
        // 4. 通过对象中的方法执行SQL语句，操作数据库
        int count = statement.executeUpdate(sql); // 该方法返回影响数据库的行数
        System.out.println(count == 1 ? "删除成功":"删除失败"); // 因为我们只是删除了一行数据,所以只会有一行数据受影响

        // 5.第四步不是 select 查询结果集,不需要处理结果集，省略

        // 6. 关闭资源，从小到大
        statement.close();   // 关闭操作数据库的连接
        connection.close();  // 关闭数据库连接

    }


        public static void main(String[] args){
            Connection connection = null;  // 扩大作用域,利用关闭资源
            Statement statement = null;  //

            try {
                // 1. 注册驱动(说明连接什么品牌的数据库),通过反射加载Driver类,执行Driver类中的静态代码块(注册驱动)
                Class.forName("com.mysql.jdbc.Driver");

                String url = "jdbc:mysql://localhost:3306/dbtest7?useUnicode=true&characterEncoding=utf8";
                String user = "root";
                String password = "MySQL123";
                // 2. 连接(驱动上)的数据库
                connection = DriverManager.getConnection(url,user,password);

                // 3.获取操作数据库的对象
                statement = connection.createStatement();

                // 4. 通过对象的方法执行SQL语句，操作数据库
                String sql = "delete from admin where id = 3";
                int count = statement.executeUpdate(sql);
                System.out.println( count == 1 ? "删除成功" : "删除失败");

                // 5. 如果第四步是select 查询结果集,则需要处理结果集,这里不是，所以不用

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

                // 6.关闭资源,从小到大,一定会被执行,(无论是否异常)
                if( statement != null) { // 不为空在需要关闭资源,为空不需要
                    try {
                        statement.close();      // 关闭 statement 操作数据库连接
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


                if( connection != null){
                    try {
                        connection.close(); // 关闭 connection 连接上的数据库的资源
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
}
