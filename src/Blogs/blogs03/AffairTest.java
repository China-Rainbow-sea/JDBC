package Blogs.blogs03;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 博客事务演示转账操作
 */
public class AffairTest {


    /**
     * 考虑上事务的问题
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接驱动中是数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6", "root", "MySQL123");


            // 1. 事务处理：取消自动commit 提交数据操作
            connection.setAutoCommit(false);   // 默认是true 开启自动提交数据信息的

            // AA用户的转账 1000 元,update 减少 1000 元
            // 3. 获取操作数据库对象(预编译sql语句的对象)
            String sqlAA = "update t_user set balance = balance - 1000 where `name` = ?";  // 占位符不要加单引号(不然就成了字符串了,
            // 失去了占位符的作用了
            preparedStatement = connection.prepareStatement(sqlAA);  // 仅仅只是预编译sql语句
            // 填充占位符,注意占位符的填充在预编译 sql语句之后，不然可以存在sql注入的问题
            preparedStatement.setString(1, "AA");  // 占位符的填充的起始下标是 1
            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate();
            // 创建一个算术异常模拟网络中断异常问题
            int num = 5 / 0;  // 分母是不可以为 0 的.

            // BB 用户的接受AA用户的转账金额, updata 增加 1000 元
            String sqlBB = "update t_user set balance = balance + 1000 where `name` = ?";
            preparedStatement = connection.prepareStatement(sqlBB);
            // 填充占位符
            preparedStatement.setString(1, "BB");
            // 执行sql语句
            count = preparedStatement.executeUpdate();

            System.out.println(count > 0 ? "成功" : "失败");
            // 执行到这,说明没有异常,手动commit 提交数据信息
            connection.commit();
            // 处理查询select 的结果集
        } catch (ClassNotFoundException e) {
            // 发生了异常,事务回滚,不执行上述中断的转账操作
            if (connection != null) {
                try {
                    connection.rollback();  // 事务回滚
                } catch (SQLException e2) {
                    throw new RuntimeException(e);
                }
            }
            e.printStackTrace();
        } catch (SQLException e) {
            // 发生了异常,事务回滚,不执行上述中断的转账操作
            if (connection != null) {
                try {
                    connection.rollback();  // 事务回滚
                } catch (SQLException e2) {
                    throw new RuntimeException(e);
                }
            }
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间

            if (preparedStatement != null) {  // 防止null引用报错
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

            // 这里我们不要关闭数据库连接,因为默认关闭连接是会提交commit 数据的
        }

    }

    /**
     * 没有进行事务处理的转账演示操作
     * @param args
     */
    public static void main1(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接驱动中是数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");


            // AA用户的转账 1000 元,update 减少 1000 元
            // 3. 获取操作数据库对象(预编译sql语句的对象)
            String sqlAA = "update t_user set balance = balance - 1000 where `name` = ?";  // 占位符不要加单引号(不然就成了字符串了,
            // 失去了占位符的作用了
            preparedStatement = connection.prepareStatement(sqlAA);  // 仅仅只是预编译sql语句
            // 填充占位符,注意占位符的填充在预编译 sql语句之后，不然可以存在sql注入的问题
            preparedStatement.setString(1,"AA");  // 占位符的填充的起始下标是 1
            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate();

            // 创建一个算术异常模拟网络中断异常问题
            int num = 5 / 0 ;  // 分母是不可以为 0 的.

            // BB 用户的接受AA用户的转账金额, updata 增加 1000 元
            String sqlBB = "update t_user set balance = balance + 1000 where `name` = ?";
            preparedStatement = connection.prepareStatement(sqlBB);
            // 填充占位符
            preparedStatement.setString(1,"BB");
            // 执行sql语句
            count = preparedStatement.executeUpdate();

            System.out.println( count > 0 ? "成功" : "失败" );
            // 处理查询select 的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间

            if(preparedStatement != null) {  // 防止null引用报错
                try{
                    preparedStatement.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }

            }


            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }

}
