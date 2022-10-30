package day03;

import java.sql.*;

public class Donglijiediantest {
    public static void main(String[] args){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;  // 扩大作用域的范围,用于关闭资源

        try{
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行com.mysql.jdbc.Driver包下类的,静态代码块,注册驱动
            // 2. 通过驱动连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");
            // 3. 获取操作数据库的对象
            statement = connection.createStatement();

            // 4. 执行sql语句
            String sql = "select `name`, `password` from users";
            resultSet = statement.executeQuery(sql);
            // int executeUpdate(insert/delete/update)是执行增删改操作的方法
            // resultSet.executeQuery是专门执行select DQL语句的方法

            // 5.处理select所查询到的结果集
           /* if(resultSet.next()) { // next:表示指向行有数据,每调用一次该方法,会自动向下移动指针,并判断该行是否有数据,有true,没有false
                // 取数据,
                // getString()方法的特点是:不管数据库中的数据类型是什么,都以String的形式取出.同样的还有getInt(),getDouble()
                // 获取next指定的行的(列/字段)的数据
                String name = resultSet.getString(1); // jdbc中基本所有的下标都是从 1 开始的,不是从0 开始
                String password = resultSet.getString(2);
                System.out.println(name+":"+password);
            }*/

            // 或者通过while获取多个内容
            while(resultSet.next()) { // next:表示指向行有数据,每调用一次该方法,会自动向下移动指针,并判断该行是否有数据,有true,没有false
                // 取数据,
                // getString()方法的特点是:不管数据库中的数据类型是什么,都以String的形式取出.同样的还有getInt(),getDouble()
                // 获取next指定的行的(列/字段)的数据
                String name = resultSet.getString("name"); // jdbc中基本所有的下标都是从 1 开始的,不是从0 开始
                String password = resultSet.getString("password");
                System.out.println(name+":"+password);
            }

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            // 6. 关闭/释放资源
            if(resultSet != null){  // 只有连接/使用了的资源才需要释放/关闭,为null的没有使用/连接的资源不用关闭
                try{
                    resultSet.close(); // 防止null引用
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if(statement != null){
                try{
                    statement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }

            if(connection != null){
                try{
                    connection.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
