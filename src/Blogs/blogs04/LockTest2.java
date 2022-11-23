package Blogs.blogs04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LockTest2 {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于关闭资源

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2.连接驱动上的数据库
            connection = DriverManager.getConnection("jdbc:mysql:///dbtest6","root","MySQL123");

            connection.setAutoCommit(false); // 取消自动提交数据，开启事务

            // 3. 获取到操作数据库的对象(预编译sql语句对象)
            String sql = "update tests set name = ? where id = ?";  // 占位符不要加单引号,不然就成字符串了
            preparedStatement = connection.prepareStatement(sql);  // 仅仅只是预编译sql语句

            // 填充占位符(在预编译之后,防止sql注入),起始下标是从 1 开始的
            preparedStatement.setString(1,"Tom");
            preparedStatement.setInt(2,1);

            // 4.执行sql语句
            int count = preparedStatement.executeUpdate(); // 返回影响数据库的行数(注意是无参的,因为上面我们已经编译过了)
            System.out.println("影响数据库的行数: "+count);

            connection.commit();  // 手动提交数据

            // 5. 处理select 查询的结果集,这里不是
        } catch (Exception e) {
            // 发生异常，回滚事务
            if(connection != null) {  // 防止null引用
                try {
                    connection.rollback();
                } catch (SQLException E) {
                    throw new RuntimeException (E);  // 将编译异常转换为运行异常抛出
                }
            }
            throw new RuntimeException (e);  // 将编译异常转换为运行异常抛出
        } finally {
            // 6.关闭资源，最晚使用的最先关闭资源
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();  // 释放操作数据库的资源
                } catch (SQLException e) {
                    throw new RuntimeException (e);  // 将编译异常转换为运行异常抛出
                }
            }

            if(connection != null) {
                try {
                    connection.close();  // 关闭数据库连接
                } catch (SQLException e) {
                    throw new RuntimeException (e);  // 将编译异常转换为运行异常抛出
                }
            }
        }


    }
}
