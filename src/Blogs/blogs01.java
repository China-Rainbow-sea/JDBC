package Blogs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class blogs01 {
    /**
     * 方式一:
     * @param args
     */
    public static void main(String[] args) throws SQLException {
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
}
