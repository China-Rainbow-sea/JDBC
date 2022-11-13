package day04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 转账事务处理
 * 1. jdbc 中的事务时自动提交的,什么是自动提交?
 * 只要执行任意一条DML语句,则自动提交一次,这是JDBL默认的事务行为.
 * 但是在实际的业务当中,通常都是N条 DML语句共同联合才能完成的,必须保证他们这些DML语句在同一个事务中同时成功
 * 或者同时失败.
 * 准备工作:
 *
 * DROP TABLE IF EXISTS t_act;
 CREATE TABLE t_act(
 acton BIGINT,
 balance DOUBLE(7,2)  # 注意7:表示有效数字的个数,2:表示小数位的个数.
 )

 SELECT *
 FROM t_act;

 INSERT INTO t_act(acton,balance)
 VALUES(111,20000),
 (222,10000);
 */
public class donglijiediAnaffair {


    /**
     * 进行了事务处理的
     * 1.Connection.setAutoCommit(false); 取消自动commit 数据给DB
     * 2.Connection.commit(); 手动commit 数据给DB
     * 3.Connection.rollback(); 网络异常，操作中断: 事务回滚
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 一事务处理: 设置取消自动commit提交数据的机制
            connection.setAutoCommit(false);   // 默认是true开启,自动commit 数据给DB的

            // 3. 获取操作数据库的(预编译sql)的对象: 111 用户的
            String sql2 = "update t_act set balance = balance - ? where acton = ?"; // ?占位符不要加单引号(不然就成了字符串了,失去了占位符的作用了)
            preparedStatement = connection.prepareStatement(sql2);  // 仅仅只是预编译sql语句,并没有执行sql语句

            // 填充占位符
            preparedStatement.setDouble(1,10000);
            preparedStatement.setInt(2,111);
            // 4.执行sql语句
            int count = preparedStatement.executeUpdate();

            // 模拟网络异常,操作中断
/*
            String str = null;
            str.toString();
*/

            // 222用户的
            String sql3 = "update t_act set balance = balance + ? where acton = ?";
            preparedStatement = connection.prepareStatement(sql3);

            // 填充占位符
            preparedStatement.setDouble(1,10000);
            preparedStatement.setInt(2,222); // 占位符起始下标位置是从 1 开始的

            int count2 = preparedStatement.executeUpdate();   // 执行sql语句

            System.out.println("成功");
            // 二: 手动commit 提交数据给DB
            connection.commit();

            // 5. 处理select 查询的结果集
        } catch (ClassNotFoundException e) {
            if(connection != null) {
                try{
                    // 三:发生异常,保证事务,回滚数据
                    connection.rollback();
                } catch(SQLException e2) {
                    throw new RuntimeException(e2);  // 将编译异常转换为运行异常抛出
                }
            }
            e.printStackTrace();
        } catch (SQLException e) {
            if(connection != null) {
                try{
                    // 三:发生异常,保证事务,回滚数据
                    connection.rollback();
                } catch(SQLException e2) {
                    throw new RuntimeException(e2);  // 将编译异常转换为运行异常抛出
                }
            }
            e.printStackTrace();
        } finally{
            // 6. 关闭资源,最晚使用的最先释放资源
            if(connection != null) {  // 防止null引用报错
                try {
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
                }
            }

            if(preparedStatement != null) { // 防止null引用
                try{
                    preparedStatement.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    /**
     * 没有进行事务回滚处理的。
     * @param args
     */
    public static void main2(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 3. 获取操作数据库的(预编译sql)的对象: 111 用户的
            String sql2 = "update t_act set balance = balance - ? where acton = ?"; // ?占位符不要加单引号(不然就成了字符串了,失去了占位符的作用了)
            preparedStatement = connection.prepareStatement(sql2);  // 仅仅只是预编译sql语句,并没有执行sql语句

            // 填充占位符
            preparedStatement.setDouble(1,10000);
            preparedStatement.setInt(2,111);
            // 4.执行sql语句
            int count = preparedStatement.executeUpdate();

            String str = null;
            str.toString();   //  创建异常,模拟网络异常,操作中断

            // 222用户的
            String sql3 = "update t_act set balance = balance + ? where acton = ?";
            preparedStatement = connection.prepareStatement(sql3);

            // 填充占位符
            preparedStatement.setDouble(1,10000);
            preparedStatement.setInt(2,222); // 占位符起始下标位置是从 1 开始的

            int count2 = preparedStatement.executeUpdate();   // 执行sql语句

            // 5. 处理select 查询的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            // 6. 关闭资源,最晚使用的最先释放资源
            if(connection != null) {  // 防止null引用报错
                try {
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);   // 将编译异常转换为运行异常抛出
                }
            }

            if(preparedStatement != null) { // 防止null引用
                try{
                    preparedStatement.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }




}
