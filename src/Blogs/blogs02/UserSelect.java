package Blogs.blogs02;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 对 user_table 数据表 select 处理查询结果集的工具类的封装
 */
public class UserSelect {

    /**
     * 针对 user_talbe 表的通用查询操作
     * @param sql
     * @param args
     * @return User
     */
    public User queryForUser(String sql,Object...args) {
        /*
        Object...args 可变参数,用于表示填充的占位符的信息,因为我们不可以将填充的信息写死了,所以我们使用 Object的类型表示
        可变参数(必须位于最后一个参数,不然无法识别其中参数是否属于可变参数的),可变参数只能有一个,可变参数可以不传参数,但是不要
        传null,特别是对于引用类型来说,存在null引用的异常,可变参数的起始下标是 0 ,旧数组
         */

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null; // 扩大作用域,用于关闭资源

        // 1. 注册驱动, 2. 连接驱动中的数据库
        connection = this.Login();

        try {
            // 3. 获取操作数据库的对象(Connection.prepareStatement(sql)预编译对象)
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并没有执行
            // 3.1 填充占位符
            for(int i = 0 ; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符起始下标是 1,可变参数的起始下标是 0
            }

            // 4. 执行sql语句
            resultSet = preparedStatement.executeQuery(); // 注意是:无参数的方法,因为上面我们已经编译sql语句了

            // 5. 处理select 查询显示的结果集

            // 5.1 获取到select 查询显示的结果集的元数据
            ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
            // 5.2 通过获取到的元数据对获取到一行记录中select 查询所显示的列数
            int columnCount = resultSetMetaData.getColumnCount();

            while(resultSet.next()) { // 指向当前记录行的指针,同时判断当前行是否存有数据，有返回true,并向下移动指针,没有返回false,跳出
                User user = new User(); // 创建对应 orm 数据表映射的Java类的对象,存储从select 查询结果获取到的信息,一行记录对应一个Java对象
                // 5.3 处理一行所有的列的字段信息
                for(int i = 0 ; i<columnCount; i++) {
                    // 通过元数据对象,获取到对应下标位置的select 查询显示的列名getColumnLabel()的方法包含select 显示的别名,起始下标位置是 1
                    String columnName = resultSetMetaData.getColumnLabel(i+1);
    //                String columnName2 = resultSetMetaData.getColumnName(i+1); //getColumnName() 这个方法是仅仅获取到的是数据表结构的的列名信息
                    // 再通过执行sql语句返回ResultSet的接口,获取到对应列名的数值,参数可以是(列名(包括别名,使用了别名就必须使用别名了)),也可以是下标位置(从1 开始的)
                    Object columnValue = resultSet.getObject(columnName);
    //                Object columnValue = resultSet.getObject(i+1); // 也可以是列所对应的下标位置 1,起始是 1 下标

                    // 再通过反射的机制，将对象指定名为 xxx的属性赋值从数据表中获取到的值 columnValue，数据表一个列名对应一个Java类当中的属性
                    Field field = User.class.getDeclaredField(columnName); // columnName 列名
                    field.setAccessible(true);
                    field.set(user,columnValue); // 赋值到对应属性当中去
                }
                return user; // 返回 user 存储到的数据信息
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {

            // 6. 关闭/释放资源,从小到大,最晚使用的最先释放资源
            this.close(resultSet,preparedStatement,connection);
        }

        return null;
    }


    /**
     * 关闭资源
     * @param resultSet
     * @param preparedStatement
     * @param connection
     */
    public void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        // 关闭/释放资源，从小到大,最后使用的,最先关闭/释放

        /*
        !=null,防止null引用,连接/使用资源的需要关闭资源,而==null 没有连接/使用的资源是不需要关闭的
         */
        if(resultSet != null ) {
            try {
                resultSet.close(); // 关闭处理select 结果集的资源
            } catch(SQLException e ) {
                e.printStackTrace();
            }
        }

        if(preparedStatement != null) {
            try {
                preparedStatement.close(); // 关闭连接操作数据库的对象的资源
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }


        if(connection != null) {
            try{
                connection.close(); // 关闭注册数据库的对象的资源
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

    }



    /**
     * 1.注册驱动, 2. 连接数据库
     */
    public Connection Login() {

        Connection connection = null;
        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 静态加载类,执行com.mysql.jdbc.Driver包下的类中的静态代码块(注册驱动)

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306" +
                    "/dbtest6?useUnicode=true&characterEncoding=utf8","root","MySQL123");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
