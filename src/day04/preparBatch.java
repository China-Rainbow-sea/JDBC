package day04;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 使用preparedStatement实现批量数据的操作
 * update,delete 本身就具有批量操作的效果,此时的批量操作，主要指的是插入,
 * 使用PreparedStatement如何实现高效的批量插入
 * 准备工作,创建数据表
 * CREATE TABLE batch (
 * id INT PRIMARY KEY AUTO_INCREMENT,  -- 主键,自增
 * NAME VARCHAR(25)
 * );
 */
public class preparBatch {

    /**
     * 方式三: 结合方式二中的方式再外加上 Connection.setAutoCommit(false) 设置不允许自动提交数据,默认是true是会自动提交的
     * Connection.commit(); 手动提交修改的数据
     * 通过这种方式减少 对网络 I/O 的访问次数，这里只访问了一次(也就是最后执行所有的sql语句,再进行一个commit 的提交操作)
     *
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // jdbc code
            // 1. 注册驱动Mysql8.0 多了一个cj包
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接上驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?rewriteBatchedStatements=true",
                    "root", "MySQL123");

            // 3. 获取操作数据库的预编译对象
            String sql = "insert into batch(name) values(?)"; // 占位符不要加 单引号不然就是字符串了,就失去了作为占位符的作用了
            preparedStatement = connection.prepareStatement(sql);  // 只是预编译sql语句,并没有执行

            connection.setAutoCommit(false); // 设置取消自动提交数据

            long start = System.currentTimeMillis();   // 获取批量处理前的时间点;
            for (int i = 0; i < 2000000; i++) {  // 批量插入 2W条记录
                preparedStatement.setString(1, "name_" + i);   // 填充占位符

                // 批量处理优化:
                //1.使用PreparedStatement.addBatch()将预编译的sql语句暂存起来,等暂存到一定数目后再执行
                preparedStatement.addBatch();

                if (i % 50000 == 0) {  // 这里我们设置,每sql语句暂存到 500 就执行sql语句
                    // 2. 执行暂存起来的sql语句
                    preparedStatement.executeBatch();  // 执行暂存起来的 sql语句

                    // 3. 将暂存区中执行完的sql语句清空，清空暂存区
                    preparedStatement.clearBatch();
                }

                /*
                需要注意的是，我们这里刚好是可以被 500 模% 尽的,可以直接使用一个 if语句
                但是当批量处理的数值 %模 不尽的时候，需要额外加上一个 if 判断 i = 最后一个数值sql语句,进行 .executeBatch()执行，clearBatch()清空操作
                 */
                if (i == 1999999) {
                    preparedStatement.executeBatch();  // 执行暂存区中的sql语句
                    preparedStatement.clearBatch();  // 清空暂存区中的sql内容
                }

            }

            connection.commit(); // 最后所有sql语句都执行完了,再将所有修改的数据提交上commit

            long end = System.currentTimeMillis(); // 获取执行到批量插入的结束时间点:

            System.out.println(end - start);   // 200w的数据批量处理所消耗的时间:20637

            // 5.处理select查询的结果集e
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间
            if (preparedStatement != null) {  // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常
                }
            }

            if (connection != null) { // 防止null引用
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常
                }
            }
        }
    }


    /**
     * 方式二: 使用上preparedStatement预编译机制配合 PreparedStatement.addBatch()暂存 sql语句
     * PreparedStatement.executeBatch() 执行暂存起来的sql语句,PreparedStatement.clearBatch()清空暂存区中已经执行的sql语句.
     * 在使用上述的方法之前,我们需要做一些准备操作,Mysql服务器默认是关闭批量出来的，我们需要一个参数,让Mysql开始批处理的支持
     * 在url中的后面加上参数 ?rewriteBatchedStatements=true
     *
     * @param args
     */
    public static void main2(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // jdbc code
            // 1. 注册驱动Mysql8.0 多了一个cj包
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接上驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?rewriteBatchedStatements=true",
                    "root", "MySQL123");

            // 3. 获取操作数据库的预编译对象
            String sql = "insert into batch(name) values(?)"; // 占位符不要加 单引号不然就是字符串了,就失去了作为占位符的作用了
            preparedStatement = connection.prepareStatement(sql);  // 只是预编译sql语句,并没有执行

            long start = System.currentTimeMillis();   // 获取批量处理前的时间点;
            for (int i = 0; i < 20000; i++) {  // 批量插入 2W条记录
                preparedStatement.setString(1, "name_" + i);   // 填充占位符

                // 批量处理优化:
                //1.使用PreparedStatement.addBatch()将预编译的sql语句暂存起来,等暂存到一定数目后再执行
                preparedStatement.addBatch();

                if (i % 500 == 0) {  // 这里我们设置,每sql语句暂存到 500 就执行sql语句
                    // 2. 执行暂存起来的sql语句
                    preparedStatement.executeBatch();  // 执行暂存起来的 sql语句

                    // 3. 将暂存区中执行完的sql语句清空，清空暂存区
                    preparedStatement.clearBatch();
                }

                /*
                需要注意的是，我们这里刚好是可以被 500 模% 尽的,可以直接使用一个 if语句
                但是当批量处理的数值 %模 不尽的时候，需要额外加上一个 if 判断 i = 最后一个数值sql语句,进行 .executeBatch()执行，clearBatch()清空操作
                 */
                if (i == 19999) {
                    preparedStatement.executeBatch();  // 执行暂存区中的sql语句
                    preparedStatement.clearBatch();  // 清空暂存区中的sql内容
                }

            }

            long end = System.currentTimeMillis(); // 获取执行到批量插入的结束时间点:

            System.out.println(end - start);   // 批量处理所消耗的时间: 优化的时间是: 282

            // 5.处理select查询的结果集e
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间
            if (preparedStatement != null) {  // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常
                }
            }

            if (connection != null) { // 防止null引用
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常
                }
            }
        }
    }


    /**
     * 方式一: 直接使用 preparedStatement
     *
     * @param args
     */
    public static void main1(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // jdbc code
            // 1. 注册驱动Mysql8.0 多了一个cj包
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接上驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6", "root", "MySQL123");

            // 3. 获取操作数据库的预编译对象
            String sql = "insert into batch(name) values(?)"; // 占位符不要加 单引号不然就是字符串了,就失去了作为占位符的作用了
            preparedStatement = connection.prepareStatement(sql);  // 只是预编译sql语句,并没有执行

            long start = System.currentTimeMillis();   // 获取批量处理前的时间点;
            for (int i = 0; i < 20000; i++) {  // 批量插入 2W条记录
                preparedStatement.setString(1, "name_" + i);   // 填充占位符

                // 4. 执行sql语句
                preparedStatement.executeUpdate();  // 执行sql语句
            }

            long end = System.currentTimeMillis(); // 获取执行到批量插入的结束时间点:

            System.out.println(end - start);   // 批量处理所消耗的时间: 31724:30s

            // 5.处理select查询的结果集e
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间
            if (preparedStatement != null) {  // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常
                }
            }

            if (connection != null) { // 防止null引用
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常
                }
            }
        }


    }
}
