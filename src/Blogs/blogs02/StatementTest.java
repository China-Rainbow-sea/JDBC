package Blogs.blogs02;

import java.sql.*;
import java.util.Scanner;

public class StatementTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("用户名user: ");
        String user = scanner.nextLine();   // next无法读取到空格,nextLine可以读取到空格
        System.out.print("密码password: ");
        String password = scanner.nextLine();

        // jdbc 代码
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行com.mysql.jdbc.Driver类的静态代码(注册驱动)
            // Driver默认是java.sql的接口,com.mysql.jdbc.Driver包下的Driver类

            // 2. 连接驱动的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象
            statement = connection.createStatement();

            // 4. 执行sql语句的select查询语句,注意与名称与sql关键字冲突的使用`着重号区分`
            String sql = "select `user`,password from user_table where user = '"+user+"' and password = '"+password+"'";
            // 在mysql中sql语句的结尾需要加;分号，但是在java当中的sql语句是不要加;(分号)的,不然报错
            resultSet = statement.executeQuery(sql); // 执行sql语句

            // 5. 处理当前select查询到的结果集
            if(resultSet.next()) { // next指向当前select 查询显示的行/记录,并判断该行是否有数据,有true,没有false,每次调用都会向下移动指针
                System.out.println("登入成功");
            } else {
                System.out.println("用户不存在或密码/用户名错误");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭/释放资源,从小到大

            if(resultSet != null) { // !=null的表示连接/使用了资源需要关闭/释放资源,=null表示没有连接/使用资源不用关闭
                try {  // 防止 null引用
                    resultSet.close();  // 关闭资源
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if( statement != null) {
                try {
                    statement.close(); // 关闭/释放资源
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try {
                    connection.close(); // 关闭/释放资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }
        }

    }



    /**
     * delete 删除操作
     * @param args
     */
    public static void main4(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入user_table中的user: ");
        String user = scanner.next();

        Connection connection = null;
        Statement statement = null;  // 扩大作用域的范围,用于关闭资源
        try {
            // jdbc 代码
            // 1. 注册驱动(说明你要连接的是什么品牌的数据库)
            Class.forName("com.mysql.jdbc.Driver");  // 通过反射加载类,执行com.mysql.jdbc.Driver中的静态代码块(注册驱动)
            // 注意在com.mysql.jdbc.Driver 包下的Driver是类,而默认的Driver是在java.sql的接口

            // 2. 连接驱动的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象
            statement = connection.createStatement();


            // 4. 执行sql语句
            // 注意与sql关键字存在冲突的名称,使用着重号,区分
            String sql = "delete from user_table where user = '"+user+"'";
            // 字符串的拼接的技巧 "++"
            int count = statement.executeUpdate(sql); // 执行sql语句,返回影响数据库的行数/记录条数
            System.out.println(count > 0 ? "删除成功" : "删除失败"); // 当影响数据库的行数 >0 表示成功

            // 5. 处理select查询的结果集

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            // 6. 关闭/释放资源,从小到大
            if(statement != null) { // !=null表示连接/使用了资源需要关闭,=null表示没有连接/使用资源不用释放资源
                try {  // 同时防止null引用
                    statement.close(); // 关闭资源
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try{
                    connection.close(); // 关闭资源
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    /**
     * update 修改数据
     * @param args
     */
    public static void main3(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入user_table中的user: ");
        String user = scanner.next();
        System.out.print("输入user_table中修改后的balance:");
        String balance = scanner.next();


        Connection connection = null;
        Statement statement = null;  // 扩大作用域的范围,用于关闭资源
        try {
            // jdbc 代码
            // 1. 注册驱动(说明你要连接的是什么品牌的数据库)
            Class.forName("com.mysql.jdbc.Driver");  // 通过反射加载类,执行com.mysql.jdbc.Driver中的静态代码块(注册驱动)
            // 注意在com.mysql.jdbc.Driver 包下的Driver是类,而默认的Driver是在java.sql的接口

            // 2. 连接驱动的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象
            statement = connection.createStatement();


            // 4. 执行sql语句
            // 注意与sql关键字存在冲突的名称,使用着重号,区分
            String sql = "update user_table set balance = "+balance+" where `user` = '"+user+"'";
            // 字符串的拼接的技巧 "++"
            int count = statement.executeUpdate(sql); // 执行sql语句,返回影响数据库的行数/记录条数
            System.out.println(count > 0 ? "修改成功" : "修改失败"); // 当影响数据库的行数 >0 表示成功

            // 5. 处理select查询的结果集

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            // 6. 关闭/释放资源,从小到大
            if(statement != null) { // !=null表示连接/使用了资源需要关闭,=null表示没有连接/使用资源不用释放资源
                try {  // 同时防止null引用
                    statement.close(); // 关闭资源
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try{
                    connection.close(); // 关闭资源
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }




    /**
     * insert 插入数据
     * @param args
     */
    public static void main2(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入user_table中的user: ");
        String user = scanner.next();
        System.out.print("输入user_table中的password:");
        String password = scanner.next();
        System.out.print("输入user_table中的balance:");
        String balance = scanner.next();


        Connection connection = null;
        Statement statement = null;  // 扩大作用域的范围,用于关闭资源
        try {
            // jdbc 代码
            // 1. 注册驱动(说明你要连接的是什么品牌的数据库)
            Class.forName("com.mysql.jdbc.Driver");  // 通过反射加载类,执行com.mysql.jdbc.Driver中的静态代码块(注册驱动)
            // 注意在com.mysql.jdbc.Driver 包下的Driver是类,而默认的Driver是在java.sql的接口

            // 2. 连接驱动的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象
            statement = connection.createStatement();


            // 4. 执行sql语句
            String sql = "insert into user_table(user,password,balance) values('"+user+"','"+password+"','"+balance+"')";
            // 字符串的拼接的技巧 "++"
            int count = statement.executeUpdate(sql); // 执行sql语句,返回影响数据库的行数/记录条数
            System.out.println(count > 0 ? "插入成功" : "插入失败"); // 当影响数据库的行数 >0 表示成功

            // 5. 处理select查询的结果集

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            // 6. 关闭/释放资源,从小到大
            if(statement != null) { // !=null表示连接/使用了资源需要关闭,=null表示没有连接/使用资源不用释放资源
                try {  // 同时防止null引用
                    statement.close(); // 关闭资源
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }

            if(connection != null) {
                try{
                    connection.close(); // 关闭资源
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
