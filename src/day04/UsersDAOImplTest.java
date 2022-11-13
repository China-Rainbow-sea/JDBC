package day04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
/**
 * 测试UsersDAOImpl的使用
 */
public class UsersDAOImplTest {
    private static UsersDAOImpl usersDAO = new UsersDAOImpl();


    /**
     * 查询User_table表中 balance最大的值
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;

        // 1. 注册驱动,2.连接注册驱动中的数据库
        connection = connectDB();
        int maxBalance = usersDAO.getMaxBalance(connection);
        System.out.println(maxBalance);

        // 关闭资源,最晚使用的最先释放空间
        if(connection != null) {  // 防止null引用
            try {
                connection.close();
            } catch(SQLException e) {
                throw new RuntimeException(e); // 将编译异常转换为运行异常抛出.
            }
        }

    }


    /**
     * 测试: 查询user_table数据表中数据的条目数
     * @param args
     */
    public static void main6(String[] args) {
        Connection connection = null;

        // 1. 注册驱动,2.连接数据库
        connection = connectDB();
        long count = usersDAO.getCount(connection);
        System.out.println(count);

        if(connection != null) {
            try{
                connection.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }


    /**
     * 测试: 查询User_table表中所有记录构成集合信息
     * @param args
     */
    public static void main5(String[] args) {
        Connection connection = null;

        // 查询数据不需要,事务回滚操作
        // 1.注册驱动,2连接驱动中的数据库
        connection = connectDB();
        List<Users> list = usersDAO.getAll(connection);
        list.forEach(System.out::println);

        // 关闭资源,最晚使用的最先释放空间
        if(connection != null) {
            try{
                connection.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }



    /**
     *  针对指定的user查询得到对应的 Users对象
     * @param args
     */
    public static void main4(String[] args) {
        Connection connection = null;
        // 查询不需要作回滚事务处理
        // 1.注册驱动,2.连接驱动中的数据库
        connection = connectDB();
        Users kk = usersDAO.getUsers(connection, "kk");
        System.out.println(kk);

        // 关闭资源,最晚使用的最先关闭释放资源
        if(connection != null) {
            try{
                connection.close();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * 测试:针对内存中的 users中的uses对象,去修改user_table数据表中指定的记录，字段值
     * @param args
     */
    public static void main3(String[] args) {
        Connection connection = null;

        // 1. 注册驱动,2.获取连接数据库
        try {
            connection = connectDB();
            connection.setAutoCommit(false);  // 设置取消自动commit 数据
            Users users = new Users("KK","987",200000);
            usersDAO.update(connection,users);
           // int n = 10 / 0;   // 模拟网络异常
            System.out.println("成功");
            connection.commit();   // 手动commit 提交数据给数据库
        } catch (SQLException throwables) {
            if(connection != null) {
                try {
                    connection.rollback();   // 发生异常,回滚数据信息
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出
                }
            }
            throwables.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先关闭
            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }



    /**
     * 测试删除user_table数据表中 指定user 的字段内容
     * @param args
     */
    public static void main2(String[] args) {
        // 1.注册驱动,2 连接数据库
        Connection connection = connectDB();       // static 不要使用this的引用

        try {
            connection.setAutoCommit(false);    // 设置取消自动commit 机制
            usersDAO.deleteByUser(connection,"KK");
//            int n = 10 / 0 ; // 异常模拟
            System.out.println("成功");
            connection.commit();  // 手动提交 commit 数据
        } catch (SQLException e) {
            if(connection != null) {
                try{
                    connection.rollback();  // 发生异常回滚数据
                } catch(SQLException e2) {
                    throw new RuntimeException(e2);   // 将编译异常转换为运行时异常抛出
                }
            }
            e.printStackTrace();
        } finally{
            // 6. 关闭资源,最晚使用的最先关闭
            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }




    /**
     * 测试 UsersDAOImpl.Insert()方法
     * @param args
     */
    public static void main1(String[] args) {
        Connection connection = null;

        // 1. 注册驱动,2.获取连接
        try {
            connection = connectDB();
            connection.setAutoCommit(false);   // 设置取消自动提交数据

            Users users = new Users("K","123",200000);
            usersDAO.insert(connection,users);
            // int n = 10/0; 异常测试:
            System.out.println("添加成功");
            connection.commit();   // 手动提交数据.
        } catch (SQLException e) {
            if(connection != null) {
                try {
                    connection.rollback();  // 发生异常数据回滚;
                } catch (SQLException throwables) {
                    throw new RuntimeException(throwables);  // 将编译异常转换为运行异常抛出；
                }
            }
            e.printStackTrace();
        } finally {
            // 6.关闭资源:最晚使用的最先关闭
        }

    }







    /**
     * 对数据库连接的封装
     * @return Connection
     */
    private static Connection connectDB() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
