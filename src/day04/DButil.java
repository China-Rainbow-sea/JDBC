package day04;

import java.sql.*;

/**
 * jdbc中工具类的封装: 模糊查询的实现:注意事项
 *
 */
public class DButil {

    public static void main(String[] args) {
        DButil dButil = new DButil();   // 实例化对象,静态方法调用非静态方法,同时执行其中的静态代码块(注册驱动)

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;  // 扩大作用域,用于关闭资源

        try {
            connection = dButil.GetConnection();   // 连接数据库的对象,static 静态方法不要使用this的引用
/*        String sql = "select department_name name from depts where department_name like '?%'";
        // 这是错误的,?占位符不能加单引号,加了就是字符串了,就失去了占位符的作用了.*/

            // 正确的方式是:
            String sql = "select department_name name from depts where department_name like ?"; // 不要加单引号
            preparedStatement = connection.prepareStatement(sql);
            // 注意是先预编译后，再进行填充占位符,不然可能存在SQL注入的问题.
            // 填充占位符
            preparedStatement.setString(1,"A%");  // 通过这里添加对应格式的 % 模糊查询

            // 4.执行sql语句
            resultSet = preparedStatement.executeQuery();  // 没有参数的方法,因为上面我们已经预编译过了

            // 5.处理select查询的结果集
            while(resultSet.next()) {  // next()指向select查询显示的一行记录,如果该行(记录)含有数据true,并向下移动指针,没有数据false
                String string = resultSet.getString("name");  // getString的特点就是无论从数据库获取到的是什么类型的数据都
                // 会转换为字符串类型的数据，赋值,同理的还有 getInt,getDobule,这里通过列名(别名)/下标位置起始位置是 1
                System.out.println(string);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 6. 释放资源,最晚使用的最先释放空间

            dButil.close(connection,preparedStatement,resultSet);
        }


    }

    /**
     * 静态代码块: 注册驱动
     */
    static{
        try{
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            throw new RuntimeException(e);  // 将编译异常转化运行时异常抛出
        }
    }

    /**
     * 连接驱动上的数据库
     * @return Connection
     * @throws SQLException
     */
    public static Connection GetConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");
    }


    /**
     * 关闭资源,最晚使用的最先关闭
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        /*
        !=null :连接/使用了资源的需要关闭,==null : 没有连接/使用的资源不需要关闭
        !=null 防止null引用报错
         */

        if(resultSet != null) {  // 防止null引用报错
            try {
                resultSet.close();   // 关闭处理select 查询处理的结果集连接
            } catch (SQLException e) {
                throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出
            }
        }


        if(statement != null) {
            try{
                statement.close();    // 关闭操作数据库的对象
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }


        if(connection != null) {
            try{
                connection.close();    // 关闭连接的数据库
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


}
