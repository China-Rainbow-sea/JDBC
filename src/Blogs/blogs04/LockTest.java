package Blogs.blogs04;

import java.sql.*;

public class LockTest {

    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;  // 扩大作用域,用于关闭资源


        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2.连接驱动中的数据库，url中的 localhost 可以省略为 ///dbtest
            connection = DriverManager.getConnection("jdbc:mysql:///dbtest6","root","MySQL123");

            connection.setAutoCommit(false);  // 取消自动提交数据,开启事务

            // 3. 获取操作数据库的(预编译)对象
            String sql = "select id,name from tests where id = ? for update";  // for update 悲观锁
            preparedStatement = connection.prepareStatement(sql);  // 预编译对象

            // 填充占位符
            preparedStatement.setInt(1,1);  // 占位符的填充从起始下标 1 开始

            // 4.执行sql语句
            resultSet = preparedStatement.executeQuery();

            // 处理select 查询的结果集
            while(resultSet.next()) {  // 判断该行记录是否有数据有,返回true并向下移动,没有返回false
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);

                System.out.println(id+"=>"+name);
            }

            connection.commit();  // 手动提交数据信息
        } catch (Exception e) {
            if(connection != null) {
                // 出现异常,回滚事务
                try{
                    connection.rollback();  // 事务的回滚
                } catch(SQLException E) {
                    throw new RuntimeException (E);  // 将编译异常转换为运行异常抛出
                }
            }
            throw new RuntimeException (e);  // 将编译异常转换为运行异常抛出
        } finally {
            // 6.关闭资源,最晚使用的最先关闭
            if(resultSet != null) {  // 防止空引用
                try{
                    resultSet.close();   // 释放处理select 查询结果集的资源
                }catch(SQLException e) {
                    throw new RuntimeException (e);  // 将编译异常转换为运行异常抛出
                }
            }


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
