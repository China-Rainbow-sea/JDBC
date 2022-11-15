package Blogs.blogs03;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * 测试对应UserDaoImp的封装的工具类DAO
 */
public class UserDaoImplTest {
     private static UserDaoImpl userDao = new UserDaoImpl();
    // 注意静态方法访问非静态方法


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {

    }


    /**
     * 测试: 查询user_table 数据表中指定的user的值
     * @param args
     */
    public static void main4(String[] args) {
        // 1.注册驱动，2.连接驱动
        Connection connection = userConnection();
        User user = userDao.getUser(connection, "KK");
        System.out.println(user);

        // 关闭连接
        closeConnection(connection);

    }


    /**
     * 测试查询user_table 数据表中所有的记录构成集合
     * @param args
     */
    public static void main3(String[] args) {
        // 1.注册驱动，2.连接驱动
        Connection connection = userConnection();
        List<User> userList = userDao.getAll(connection);

        userList.forEach(System.out::println);

        // 关闭资源连接
        closeConnection(connection);

    }

    /**
     * 测试查询user_table数据表中记录总个数
     * @param args
     */
    public static void main2(String[] args) {

        // 1.注册驱动，2.连接驱动
        Connection connection = userConnection();

        System.out.println(userDao.getCount(connection));

        // 关闭连接
        closeConnection(connection);

    }

    /**
     * 查询user_table 表中的 balance 的最大值
     * @param args
     */
    public static void main1(String[] args) {

        Connection connection = null;

        // 1.注册驱动，2.连接驱动
        connection = userConnection();
//        this.userDao.getMaxBalance(); this无法在静态方法引用,因为this本身就是代表非静态的引用
        int maxBalance = userDao.getMaxBalance(connection);
        System.out.println(maxBalance);

        // 关闭连接
        closeConnection(connection);

    }


    /**
     * 封装对 dbtest6 数据库的连接
     * @return Connection
     */
    public static Connection userConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root",
                    "MySQL123");
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 关闭Connection连接的数据库
     * @param connection
     */
    private static void closeConnection(Connection connection) {
        if(connection != null) {
            try{
                connection.close();
            } catch(SQLException e) {
                throw new RuntimeException(e); // 将编译异常转换为运行异常抛出
            }
        }

    }

}
