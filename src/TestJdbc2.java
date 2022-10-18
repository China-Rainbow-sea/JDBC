
import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class TestJdbc2 {
    public static void main(String[] args) throws SQLException {

        // 1. 注册对应 mysql 厂商实现的驱动,创建 Driver 对象
       // Driver driver = (Driver) new Driver();

        // 或者使用如下绝对路径的方式导入对应的类中的,从而实例化对象
        Driver driver = new com.mysql.jdbc.Driver();
        //  Driver driver = new com.mysql.cj.jdbc.Driver();
        // 通过 对应路径下的类,实例化对应的路径下的对象


        // 2.设置数据库在网络上的地址,同时将用户名和密码封装到Properties()的类当中去
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
        // ?useUnicode=true&characterEncoding=utf8
        /*
        注意:url 表示的是 你所要连接的Mysql中的数据库的网络上的地址
        jdbc:mysql 表示的是 mysql的协议,
        和 http 中的 action(url)name=value&name=value&...是一样的,是规定好的,不可以修改
        其对应的变量名,其中的值是你需要根据实际情况赋值的
        //localhost: 表示的是 ip 地址,这里连接的是本地计算机
        3306 表示的是 mysql 默认的端口号,
        test 表示你所需要连接的Mysql 的数据库名

        Exception in thread "main" java.sql.SQLException: Unknown initial character set index '255' received from server.
        Initial client character set can be forced via the 'characterEncoding' property.
        这个报错的原因是主要是因为我们的 Mysql无法识别,对应的字符集设置，我们只需要在其后面添加上
        这样一句话: ?useUnicode=true&characterEncoding=utf8 就可以解决这个问题了,
        具体如下: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8
         */



        // 将用户名和密码封装到Properties 中的类对象中的方法中
        // 让Connect 自动调用其方法获取对应的用户名和密码的值
        Properties info = new Properties();
        info.setProperty("user","root");          // user 表示用户名
        info.setProperty("password","MySQL123");  // password 表示的是密码

        /*
        user(用户名) 和 password(密码) 这两个变量名是固定的,规定好的,是不可以
        修改的,修改了,就可能无法识别上了,其值根据实际情况赋值,(其所赋予的值,不要加上空格,防止无法识别到)
         */

        // 获取连接,调用Connection 的类中的方法,其中使用我们刚刚对用户名和密码进行封装的对象类,
        // 让 connect 方法自动调用其中的 getProperty方法,获取对应的用户名和密码进行登入连接上
        // 网络地址上指定的数据库,中的这个test数据库
        Connection conn = driver.connect(url,info);
        System.out.println(conn);


    }
}
