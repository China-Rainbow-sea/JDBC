package day03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestUpdate {
    public static void main(String[] args){
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于后面的关闭资源
        try {
            // 1.注册驱动,2.连接数据库
            connection = JdbcUtils.getConnection();

            // 3.获取操作数据库的对象(预编译的操作对象)
            String sql = "update customers set name = ? where id = ?";
            // ? 占位符,不要加单引号,从 1 开始,在Java当中的sql语句不要加分号
            preparedStatement = connection.prepareStatement(sql); // 预编译sql语句,不会执行sql语句

            // 4.执行sql语句
            // 填充占位符,从第一列 1 开始,jdbc 基本上都是从下标 1 开始的,
            preparedStatement.setString(1,"韩立"); // 第一个占位符
            preparedStatement.setInt(2,13); // 第二个占位符
            int count = preparedStatement.executeUpdate(); // 执行sql语句,注意是没有参数的,因为我们上面已经编译过了
            // preparedStatement.executeUpdate()返回影响数据库的行数
            System.out.println( count == 1 ? "修改成功" : "修改失败"); // 因为我们只是修改了一行记录,所以只会影响一行数据

            // 5.处理结果集,不是 select 不需要处理查询结果集

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{

            // 6. 关闭资源
            JdbcUtils.closeResource(connection,preparedStatement);
        }

    }
}
