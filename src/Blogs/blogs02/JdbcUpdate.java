package Blogs.blogs02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 使用PreparedStatement  对增删改操作进行一个工具的封装
 */
public class JdbcUpdate {

    /**
     * 操心数据表执行sql语句
     */
    public void jdbcUpdate(String sql,Object...args) {
        /*
        Object...args 可变参数，用于存放你填充占位符的所传的信息，
        Object...args 可变参数,必须在参数的最后一个(不然无法识别是否,是可变参数的值),只能有一个可变参数
        可变参数可以不传参数(但是不用传null,防止引用类型的空引用报错),起始下标是0 ,旧(数组)
        Object 类型的可变参数,因为不要写死了，你所填充的占位符的数据类型
         */

        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于关闭/释放资源

        try {
            // 1. 注册驱动，2.连接数据库
            connection = this.Login();

            // 3. 获取操作数据库的对象(Connection.prepareStatement()预编译对象)
            preparedStatement = connection.prepareStatement(sql);  // 只是预编译sql语句,并没有执行

            // 3.1 填充占位符
            for(int i = 0; i < args.length; i ++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符的起始下标位置是 1 ,而可变参数的起始下标位置是 0
            }

            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate();  // 注意是无参数的,因为上面我们已经对sql语句进行编译了

            System.out.println(count > 0 ? "成功": "失败");

            // 5. 处理select 查询的结果集,这里没有不用处理
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 6. 关闭/释放资源
        this.close(preparedStatement,connection);
    }


    /**
     * 2. 关闭/释放资源,从小到大,最晚使用的资源，最先释放资源
     */
    public void close(PreparedStatement preparedStatement, Connection connection) {

        /*
        !=null,防止空引用,!=null 表示连接/使用了资源才需要关闭,=null没有连接/使用的资源不需要关闭
         */
        if(preparedStatement != null) {
            try {
                preparedStatement.close(); // 关闭/释放获取到操作数据库的资源
            } catch (SQLException e) {
                throw new RuntimeException(e); // 将编译异常转为为运行异常抛出
            }

        }


        if(connection != null) {
            try {
                connection.close(); // 关闭/释放连接的数据库资源
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



    /**
     * 1.注册驱动，2. 连接数据库
     * @return Connection
     */
    public Connection Login() {
        Connection connection = null;

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行com.mysql.jdbc.Driver中静态代码块(注册驱动)

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?jdbc:mysql://localhost:3306" +
                    "/dbtest6?useUnicode=true&characterEncoding=utf8", "root","MySQL123");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
        } catch (SQLException e) {
            throw new RuntimeException(e); // 将编译异常转化为运行异常抛出;
        }

        return connection;
    }
}
