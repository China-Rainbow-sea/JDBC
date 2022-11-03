package Blogs.blogs02;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PreparedStatement  实现处理查询select 结果集的通用的工具类的封装
 */
public class JdbcPreparedStatement {

    /**
     * 使用 PreparedStatement  实现针对于不同的数据表的一条记录 的通用查询操作
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> T getInStance(Class<T> clazz,String sql,Object...args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null; // 扩大作用域,用于关闭资源

        try {
            // 1.注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 反射加载类,执行静态代码块

            // 2. 连接驱动上的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象(connection.prepareStatement()预编译对象)
            preparedStatement = connection.prepareStatement(sql); // 只是预编译了sql语句,并没有执行
            // 3.1 填充占位符
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符从起始下标是 1，可变参数的起始下标是0 旧数组
            }

            // 4. 执行sql语句
            resultSet = preparedStatement.executeQuery(); // 是无参数的,因为上面我们已经预编译过了

            // 5. 处理select 查询到的结果集
            // 5.1 获取到当前select 查询的元数据对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 5.2 通过获取到的元数据对象,再获取到select 查询显示的一行的(总列数)
            int columnCount = resultSetMetaData.getColumnCount();
            if(resultSet.next()) { // 指向select查询显示的结果集的一行记录,该行有数据返回true并向下移动,没有数据返回false
                T t = clazz.newInstance(); // 创建orm映射的Java类的对象用于存放，数据表读取到的数据,注意所放位置,不要在循环里
                // 处理一行的列的数据
                for(int i = 0; i < columnCount; i++) {
                    String columnLabel = resultSetMetaData.getColumnLabel(i+1); // 通过元数据对象获取到对应下标的列名(包含别名),起始下标是 1
                    Object columnValue = resultSet.getObject(i+1); // 通过下标访问获取到对应下标列的值,起始下标是 1
    //                Object cloumnValue = resultSet.getObject(columnLabel); // 或者通过列名获取

                    // 通过反射给 t 对象指定的 columnLabel(数据表中读取到的列名) 属性，赋值为 columnValue(对应列名的值)
                    Field field = clazz.getDeclaredField(columnLabel); // 数据表获取到的列名
                    field.setAccessible(true);
                    field.set(t,columnValue); // 数据表columnLabel列名的数据内容

                }
                return t;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            // 6.关闭资源,最晚使用的最先关闭
            // !=null 连接/使用了资源的需要关闭，而==null的没有连接/使用资源的不需要关闭,
            if( resultSet!= null) { // 防止 null引用
                try{
                    resultSet.close(); // 关闭处理select 查询结果集的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }

            if(preparedStatement != null) { // 防止null引用
                try{
                    preparedStatement.close();
                }catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }

            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }
        }

        return null;

    }


    /**
     * 使用 PreparedStatement  实现针对于不同的数据表的多条记录 的通用查询操作
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return
     */
    public <T> List<T> getForList(Class<T> clazz,String sql,Object...args) { // 可变参数类型为 Object

        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null; // 扩大作用域的范围,用于关闭资源


        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 反射加载类,执行其中类的静态代码

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象( connection.prepareStatement()预编译对象)
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并没有执行sql语句
            // 3.1 填充占位符
            if(args != null) {
                for(int i = 0 ; i < args.length; i++) {
                    preparedStatement.setObject(i+1,args[i]); // 占位符的起始下标是 1,可变参数的起始下标是 0 (旧数组)
                }
            }


            // 4. 执行sql语句
            resultSet = preparedStatement.executeQuery(); // 注意是无参的方法,因为上面已经预编译过了

            // 5. 处理select 查询显示的结果集
            // 5.1 获取到select 查询显示的结果集的元数据
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 5.2 通过元数据的对象,获取到select 查询显示的一行的所有列数的个数
            int columnCount = resultSetMetaData.getColumnCount();

            // 5.3 创建 集合对象链表,存放多条查询到的数据
            ArrayList<T> list = new ArrayList<T>();

            while(resultSet.next()) {
                T t = clazz.newInstance(); // orm映射的对象的Java类，存放数据

                // 处理select 一行的数据中的每一个列的数据
                for(int i = 0; i < columnCount; i++) {
                    // 1. 通过元数据对象获取到列名，列名字符串
                    String columnLabel = resultSetMetaData.getColumnLabel(i+1); // 起始下标是 1,该方法可以获取到select 查询使用的别名
                    // 2. 获取到对应列名下的值
                    Object columnValue = resultSet.getObject(i+1); // 这里是通过列名,获取对应列名下的数据内容
    //                或者 Object columnValue = resultSet.getObject(i + 1); // 通过访问列的下标,获取到对应列下的内容,起始下标是1

                    // 通过给 t 对象指定的columnLabel 属性名，赋值为columnValue ，通过反射
                    Field field = clazz.getDeclaredField(columnLabel); // columnLabel 从数据表中获取到的列名
                    field.setAccessible(true);
                    field.set(t,columnValue); // columnValue 从数据表中获取到的数据内容,并赋值该对应Java对象中的属性
                }
                list.add(t); // 将存储到的对象的数据,存入到集合List链表当中
            }

            return list; // 返回集合中的list链表的地址e
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚的使用的最先释放
            // != null 连接/使用了资源需要关闭,==null没有连接/使用的资源不需要关闭
            if(resultSet!=null) { // 防止null引用
                try {
                    resultSet.close();  // 关闭处理select 查询结果集的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转为为运行异常抛出
                }
            }

            if(preparedStatement != null) { // 防止null引用
                try{
                    preparedStatement.close(); // 关闭操作数据库预编译的资源
                }catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转为为运行异常抛出
                }
            }


            if(connection != null) { // 防止null引用
                try {
                    connection.close(); // 关闭连接的数据库资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转为运行异常抛出
                }
            }

        }

        return null;
    }
}
