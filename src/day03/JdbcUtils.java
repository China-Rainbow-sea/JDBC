package day03;

import java.sql.*;

/**
 * 对操作 test 数据库的工具类的封装
 */
public class JdbcUtils {

    /**
     * 连接数据库的静态方法
     * @return Connection
     */
    public static Connection getConnection(){
        Connection connection = null;
        try {
            // 1.注册驱动(说明你要连接的数据库是什么品牌的)
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行com.mysql.jdbc.Driver包下的类中的静态代码块,注册驱动
            // 2. 连接驱动的数据库

           connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }


    /**
     * 通用的增删改操作
     */
    public void update(String sql, Object...args){ // Object...args表示可变参数,只能有一个,位于最后(不然无法识别多个的参数),数组
        Connection connection = null;
        PreparedStatement preparedStatement = null;  // 扩大作用域,用于关闭资源
        try {
            // 1. 注册驱动 2.连接数据库
            connection = getConnection();

            // 3. 获取操作数据库的对象(预编译的对象)
            preparedStatement = connection.prepareStatement(sql); // 预编译sql语句,并不会执行sql语句

            // 填充占位符
            for(int i = 0; i< args.length; i++){
                preparedStatement.setObject(i+1,args[i]);
                // 占位符从下标 1 开始,表示第一个占位符的值
                // args 可变参数和数组一样(其实就是旧数组),从下标 0 开始
            }

            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate();// 返回影响的数据库的记录条数信息,注意是没有参数的因为上面我们已经编译了
            System.out.println( count > 0 ? "成功" : "失败");

            // 5. 处理select 查询的结果集,这里不是 select 不用处理
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            closeResource(connection,preparedStatement);
        }

    }

    /**
     * 关闭资源
     * @param connection
     * @param statement
     */
    public static void closeResource(Connection connection, Statement statement){
        if( statement != null) { // 只有当使用该资源,才需要关闭,如果没有使用(null)就不需要关闭了
            try{
                statement.close();
            } catch(SQLException e){
                e.printStackTrace();
            }
        }


        if ( connection != null){
            try{
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }



}



/*
void setObject(int parameterIndex,
               Object x)
        throws SQLException使用给定对象设置指定参数的值。
JDBC规范规定了从Java Object类型到SQL类型的标准映射。 在发送给数据库之前，给定的参数将被转换为相应的SQL类型。

 */