import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TestJdbc3 {

    /*
    Java程序连接Mysql数据库的方式一:创建Driver 类调用其中的对应的方法
     */                          // throws 异常处理
    public void testConnection() throws ClassNotFoundException, SQLException {
        //1. 注册驱动创建 Driver 对象
        Driver driver = new com.mysql.jdbc.Driver(); // 通过绝对路径进行导入 实例化 Driver()的类
        // 或者
        /*
        Driver driver = new Driver();  //直接写也是可以的,让它自己去找到对应的 类中的文件
        */

        // 2. 配置相关的连接属性,连接的网络上的数据库的url ，以及相关的用户名和密码
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8"; // 所连接的数据库上网络的url
        /*
        jdbc:mysql: 是相关规定的协议,和 http action(url)name=value&name=value&...是一样的,是规定的协议不可修改
        localhost 表示的是 ip 地址,这里是本机的地址 , 和 127.0.0.1 是一样的
        3306 : 表示的是mysql 默认的端口号,如有修改,这里同样要修改
        test 表示mysql中所连接的数据库名

        java.sql.SQLException: Unknown initial character set index '255' received from server.
        Initial client character set can be forced via the 'characterEncoding' property.
        可能会出现如上的 字符集编码的错误，我们只需在数据库名的后面添加上 如下的字符集配置:
        ?useUnicode=true&characterEncoding=utf8  字符集编码改为 utf8,具体实现如下:
        jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8

         */

        // 将用户名和密码封装到的Properties() 对象中,让其自动调用其需要的用户名和密码
        Properties properties = new Properties(); // 实例化 Properties()对象
        properties.setProperty("user","root");   // 用户名
        properties.setProperty("password","MySQL123");  // 密码

        /*
        user(用户名) 和password(密码) 其中的变量名是不可以修改的,这是规定好的,其中的赋值根据实际情赋值
         */

        // 3. 获取连接
        Connection conn = driver.connect(url,properties);
        System.out.println("方式一:"+conn);   // 打印所连接的引用类型的地址
        // 方式一:com.mysql.jdbc.JDBC4Connection@68f7aae2


    }


    /**
     * Java程序连接mysql数据库中的方式二
     * 通过反射加载 forName中的类掉应用曲中
     */
    public void testConnection2() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        /*
        通过反射加载Driver类,动态加载,更加的灵活,减少依赖性
         */
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();
        // 或者我们使用创建的如下格式
      /*
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = clazz.newInstance();
        */


        //2. 创建 url 要连接的数据库网络地址,
        String url = "jdbc:mysql://localhost:3306/test??useUnicode=true&characterEncoding=utf8";
        // ?useUnicode=true&characterEncoding=utf8 设置字符集有关的编码集
        //jdbc:mysql 协议,localhost ip地址,3306 mysql默认端口号, test表示数据库名


        //3.提供连接数据库需要的用户名和密码,封装在 Properties 的对象中,让其自动调用获取其中的数值
        //
        Properties info = new Properties();
        info.setProperty("user","root");     // 数据库的用户名
        info.setProperty("password","MySQL123");  // 数据库的密码
        /*
        user password 是规定好的变量,固定的不可以修改,其中的变量名,其值根据实际情况,赋值,
         */

        // 4. 获取连接,通过 Connect 连接数据库
        Connection conn = driver.connect(url,info);
        System.out.println("方式二:"+conn); // 打印其对象的地址

    }

    // com.mysql.jdbc.JDBC4Connection@68f7aae2


    /**
     * 方式三: 使用DriverManager 替代 Diver 进行统一的管理(注册驱动)
     */
    public void testConnection3() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        //1. 通过反射加载对应的类Driver,获取 Driver 实现类的对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,加载的类通过绝对路径引入
        Driver driver = (Driver) clazz.newInstance(); // ？？？？

        // 或者
        /*
        Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)aClass.newInstance();
        */

        // 2. 创建user url .password 的值
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";  // 数据库中的网络上的地址
        String user = "root";  // 用户名
        String password = "MySQL123"; // 密码


        // 3.注册驱动名
        DriverManager.registerDriver(driver);  // DriverManager代替使用 Driver 的使用


        // 4. 获取连接, 通过调用 DRiverManager.getConnection的方法,
        // 替换掉方式二中的 driver.connect 中的连接操作,去掉了用来的 Properties 对用户名和密码的封装直接
        // 一次性全部(将用户名,密码,url)传入到 gerConnection的参数当中,实现连接,
        Connection conn = DriverManager.getConnection(url,user,password);
        System.out.println("第三种方式:"+conn);

    }


    /**
     * java程序调用 JDBC 接口中连接 mysql 数据库的方式四:
     * 使用class.forName 自动完成注册驱动,简化代码,是因为该类中存在一个static 静态的代码块,反射加载类就会被执行该static 静态代码块
     * 1.mysql 驱动5.16可以只需 class.forName("com.mysql.jdbc.Driver");
     * 2.Mysql 1.5 以后使用了 jdbc4 不再需要显示调用 class.forName(), 注册驱动而是自动调用驱动，
     * jar驱动包下META-INF\Servies\java.sql.Driver 文本中类的名称注册,但是不建议省略,建议写上,提高代码的可读性
     *
     */
    public void testConnection4() throws ClassNotFoundException {
        // 1. 提供三个连接数据库的基本信息,url,user,password
        String url = "jdbc:mysql://localhost:3306/test"; // url 数据库在网络中的地址
        String user = "root";   // 用户名
        String password = "MySQL123";  // 密码

        // 2. 加载Driver
        Class.forName("com.mysql.jdbc.Driver");


    }



}
