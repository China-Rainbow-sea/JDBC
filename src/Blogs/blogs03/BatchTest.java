package Blogs.blogs03;


import java.sql.*;

/**
 * CREATE TABLE tests(
 * id INT PRIMARY KEY AUTO_INCREMENT,
 * NAME VARCHAR(25)
 * );
 *
 * SELECT COUNT(*)
 * FROM tests;
 */
public class BatchTest {


    /**
     * 批处理方式四: 在方式三的基础上加上: connection.setAutoCommit(false);取消自动提交数据
     * connection.commit();手动提交数据
     *
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2.连接驱动上的数据库,在url中添加上启动批处理:?rewriteBatchedStatements=true参数
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?rewriteBatchedStatements=true","root","MySQL123");

            // 设置取消自动提交数据
            connection.setAutoCommit(false);

            // 3.获取到操作数据库的对象(预编译sql对象)
            String sql = "insert into tests(name) values(?)";  // 占位符不要加上单引号,不然就成字符串了,失去了占位符的作用的
            preparedStatement = connection.prepareStatement(sql);  // 仅仅只是预编译sql语句,并没有执行sql语句

            // 计算执行sql语句前的时间点
            long begin = System.currentTimeMillis();  // 单位毫秒
            // 填充占位符,注意占位符的填充要在预编译sql语句的后面,不然会 sql注入

            for(int i = 0; i < 1000000; i++) {
                // 填充占位符;
                preparedStatement.setString(1,"name_"+i);

                // 1. “攒” sql:将预执行的sql语句暂存到缓存区当中去
                preparedStatement.addBatch();  // 是无参数的

                // 暂存区/缓存区当中每存储到 5000 条sql语句就执行,执行完并清空暂存区/缓存区
                if( i % 5000 == 0) {
                    // 2. 执行暂存区/缓存区当中的sql语句
                    preparedStatement.executeBatch();

                    // 3. 执行完后,清空暂存区/缓存区当中的sql语句,方便后面的继续存储sql语句
                    preparedStatement.clearBatch();  // 清空暂存区/缓存区当中的sql语句内容
                }

                // 注意最后一次, 没有被 % 5000 尽的sql语句也要执行
                if(i == 999999) {
                    preparedStatement.executeBatch();  // 执行暂存区/缓存区当中的sql语句
                    preparedStatement.clearBatch();    // 清空暂存区/缓存区当中的存储的数据

                }

            }

            // 执行完所有的100W记录,手动提交数据
            connection.commit();

            // 执行完100W记录的插入后的时间点:
            long end = System.currentTimeMillis();

            // 计算执行批量处理2W条记录的所消耗的时间点:
            System.out.println("100W条记录插入所花费的时间: "+(end-begin));

            // 5.处理select 查询显示的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先关闭;
            //!=null 连接/使用的资源释放空间,==null,没有连接使用的资源不用关闭释放空间
            if(preparedStatement != null) { // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
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

    /**
     * 批处理方式三: 使用PreparedStatement中的Batch()批处理方法
     * @param args
     */
    public static void main4(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2.连接驱动上的数据库,在url中添加上启动批处理:?rewriteBatchedStatements=true参数
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?rewriteBatchedStatements=true","root","MySQL123");

            // 3.获取到操作数据库的对象(预编译sql对象)
            String sql = "insert into tests(name) values(?)";  // 占位符不要加上单引号,不然就成字符串了,失去了占位符的作用的
            preparedStatement = connection.prepareStatement(sql);  // 仅仅只是预编译sql语句,并没有执行sql语句

            // 计算执行sql语句前的时间点
            long begin = System.currentTimeMillis();  // 单位毫秒
            // 填充占位符,注意占位符的填充要在预编译sql语句的后面,不然会 sql注入

            for(int i = 0; i < 20000; i++) {
                // 填充占位符;
                preparedStatement.setString(1,"name_"+i);

                // 1. “攒” sql:将预执行的sql语句暂存到缓存区当中去
                preparedStatement.addBatch();  // 是无参数的

                // 暂存区/缓存区当中每存储到 500 条sql语句就执行,执行完并清空暂存区/缓存区
                if( i % 500 == 0) {
                    // 2. 执行暂存区/缓存区当中的sql语句
                    preparedStatement.executeBatch();

                    // 3. 执行完后,清空暂存区/缓存区当中的sql语句,方便后面的继续存储sql语句
                    preparedStatement.clearBatch();  // 清空暂存区/缓存区当中的sql语句内容
                }

                // 注意最后一次, 没有被 % 500 尽的sql语句也要执行
                if(i == 19999) {
                    preparedStatement.executeBatch();  // 执行暂存区/缓存区当中的sql语句
                    preparedStatement.clearBatch();    // 清空暂存区/缓存区当中的存储的数据

                }

            }

            // 执行完2W记录的插入后的时间点:
            long end = System.currentTimeMillis();

            // 计算执行批量处理2W条记录的所消耗的时间点:
            System.out.println("2W条记录插入所花费的时间: "+(end-begin));

            // 5.处理select 查询显示的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先关闭;
            //!=null 连接/使用的资源释放空间,==null,没有连接使用的资源不用关闭释放空间
            if(preparedStatement != null) { // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
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


    /**
     * 批处理方式二使用PreparedStatement
     * @param args
     */
    public static void main3(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2.连接驱动上的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 3.获取到操作数据库的对象(预编译sql对象)
            String sql = "insert into tests(name) values(?)";  // 占位符不要加上单引号,不然就成字符串了,失去了占位符的作用的
            preparedStatement = connection.prepareStatement(sql);  // 仅仅只是预编译sql语句,并没有执行sql语句

            // 计算执行sql语句前的时间点
            long begin = System.currentTimeMillis();  // 单位毫秒
            // 填充占位符,注意占位符的填充要在预编译sql语句的后面,不然会 sql注入

            for(int i = 0; i < 20000; i++) {
                preparedStatement.setString(1,"name_"+i);

                // 4.  执行sql语句
                int count = preparedStatement.executeUpdate();  // 注意是无参数的,因为上面我们已经预编译过了

            }

            // 执行完2W记录的插入后的时间点:
            long end = System.currentTimeMillis();

            // 计算执行批量处理2W条记录的所消耗的时间点:
            System.out.println("2W条记录插入所花费的时间: "+(end-begin));

            // 5.处理select 查询显示的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先关闭;
            //!=null 连接/使用的资源释放空间,==null,没有连接使用的资源不用关闭释放空间
            if(preparedStatement != null) { // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
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


    /**
     * 批处理方式一: 使用Statement
     * @param args
     */
    public static void main2(String[] args) {
        Connection connection = null;
        Statement statement = null;  // 扩大作用域,用于关闭资源


        try {
            // 1.注册数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 3. 获取到操作数据库的对象
            statement = connection.createStatement();

            // 获取到执行sql插入 2W 记录时的时间点
            long begin = System.currentTimeMillis();
            // 4. 执行sql语句
            for(int i = 0; i < 20000; i++) {
                String sql = "insert into tests(`name`) values('i')";
                int count = statement.executeUpdate(sql);  // 执行sql语句
            }

            // 获取到执行完 2W 条记录的时间点:
            long end = System.currentTimeMillis();

            // 执行完批量2W记录的插入所消耗的时间 =  执行结束 - 执行开始的时间
            System.out.println("执行2W记录插入所消耗的时间: "+(end-begin));

            // 5.处理select 查询的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先关闭;
            //!=null 连接/使用的资源释放空间,==null,没有连接使用的资源不用关闭释放空间
            if(statement != null) {  // 防止null引用
                try{
                    statement.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);   // 将编译异常转换为运行时异常抛出
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
