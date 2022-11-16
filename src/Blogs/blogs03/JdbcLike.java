package Blogs.blogs03;

import java.sql.*;

public class JdbcLike {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;   // 扩大作用域,用于释放资源

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 3.获取操作数据库的对象(预编译sql语句对象)
            String sql = "select department_name as name from depts where department_name like ?";  // 错误的
            preparedStatement = connection.prepareStatement(sql);  // 仅仅只是预编译sql语句并不执行

            // 填充占位符,注意占位符的填充，在预编译sql语句之后，填充防止sql注入的问题
            preparedStatement.setString(1,"A%");

            // 4. 执行sql语句
            resultSet = preparedStatement.executeQuery();  // 没有参数因为前面已经预编译过了

            // 5. 处理select 查询显示的结果集
            while(resultSet.next()) { // 有数据true,并向下移动指针,没有数据false
                String name = resultSet.getString("name");  // 获取对应字段名/别名的数据值,
                // getString 无论从数据库获取到的是什么类型的数据都以字符串的形式赋值到 变量中,同理的还有 getInt,getDouble

                System.out.println(name);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的资源最先释放空间
            // !=null 连接/使用的资源释放,==null没有连接使用的资源不用关闭

            if(resultSet != null) { // 防止null引用报错
                try{
                    resultSet.close();  // 关闭select 结果集对象
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }

            if(preparedStatement != null) {   // 防止null引用
                try {
                    preparedStatement.close();  // 关闭操作数据库对象
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }

            if(connection != null) {
                try{
                    connection.close();  // 关闭连接数据库
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }
        }

    }
}
