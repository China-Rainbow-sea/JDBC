package day03;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 preparedStatement 实现针对不同表的通用的查询操作的封装
 */
public class SelectJdbc {


    /**
     * 单个select 查询操作的封装
     */
    public <T> T getInstance(Class<T> clazz,String sql,Object...args) { // Object...args可变参数,位于最后(不然无法识别),只能有一个,
        // 可以不传参数,旧数组
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;  // 扩大作用域,用于关闭资源

        try {
            // 1.注册驱动,2.连接数据库
            connection = JdbcUtils.getConnection();

            // 3. 获取操作数据对象(获取到Connection.PreparedStatement()预编译的对象)
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并没有执行sql语句

            // 4.执行sql语句
            // 填充占位符
            for(int i = 0;i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // ? 占位符从 1下标开始,jdbc基本都是从下标 1 开始的,可变参数(旧数组)
            }
            resultSet = preparedStatement.executeQuery(); // 执行sql语句,注意空参数的方法,因为上面我们已经预编译过了

            // 5. 处理select查询的结果集
            // 5.1 获取到当前select 查询的结果集的元数据
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 5.2 通过元数据获取到当前select 查询到的行的(总列/字段)数
            int columnCount = resultSetMetaData.getColumnCount();

            if(resultSet.next()) { // 指向当前select查询到的行(记录位置),并判断是否含有数据,有true,没有false,每调用一次都会自动向下移动

                // 处理查询到的一行(所有列(字段))的数据存入到,orm映射的对象当中去
                T t = clazz.newInstance();
                for(int i = 0; i < columnCount; i++) {
                    // 1.通过元数据获取到当前select 查询的显示的列名(字段名)
                    String columnName = resultSetMetaData.getColumnLabel(i+1); // 从下标 1 开始(这个方法可以获取到别名)
    //                String columnName = resultSet.getObject(i+1); // 这个方法无法获取到别名(获取到的是数据表的结构字段/列名)
                    //2.获取到对应下标的列名/字段名的数据的内容
                    Object columnValue = resultSet.getObject(i+1);

                    // 3. 通过反射将数值赋值到orm对应属性名上
                    Field field = clazz.getDeclaredField(columnName); // 将读取到的列名赋值上
                    field.setAccessible(true); // 修改为 true
                    field.set(t,columnValue); // 对应列名赋值上读取到的数值
                }

                return t;  // 返回存储到的对象地址
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            JdbcUtils.closeResource(connection,preparedStatement,resultSet);
        }

        return null;
    }


    /**
     * select 多个条记录的处理封装工具
     */
    public <T> List<T> getForList(Class<T> clazz, String sql, Object...args) { // Object...args表示可变参数,位于最后(不然无法识别),
        // 只有一个,可以不传参数,不要传null,特别是对于引用类型来说,旧数组
        Connection  connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 1. 注册驱动，2. 连接驱动的数据库
            connection = JdbcUtils.getConnection();

            // 3. 获取操作数据库的对象(Connection.PreparedStatement预编译对象)
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql,并没有执行sql语句

            // 4. 执行sql语句
            // 4.1 填充占位符(从下标 1 开始,填充)jdbc基本上都是从下标 1 开始的
            if ( args != null) { // 防止 args 传的是 null 空值
                for(int i = 0; i < args.length; i++){
                    preparedStatement.setObject(i+1,args[i]);
                    // 可变参数从 0 下标开始,这里使用 setObject因为我们不知道是什么类型的数据
                }
            }
            resultSet = preparedStatement.executeQuery(); // 注意是无参数的方法,因为上面我们已经编译了
            // 5. 处理当前select 查询的结果集
            // 5.1 通过获取到的预编译对象获取到当前select 查询的结果集的元数据
            ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();
            // 5.2 通过元数据获取到当前select查询到的行中(列/字段)的总个数
            int columnCount = resultSetMetaData.getColumnCount();

            ArrayList<T> list = new ArrayList<T>(); // 定义存储数据表获取到的数据的对象 orm
            while(resultSet.next()) { // 指向当前的行记录(并判断该行是否有数据,有true,没有false),每调用一次都会向下移动一行
                T t = clazz.newInstance(); // 定义存储数据表获取到的数据的对象 orm
                // 集中处理一行的(所有的列/字段)内容
                for(int i = 0; i < columnCount; i++) {
                    // 1. 通过元数据的对象获取到对应下标的列名(列名是字符串类型) 从下标 1 开始
                    String columnName = resultSetMetaData.getColumnLabel(i+1); // 获取到当前select查询显示的结果的列名(包括别名)
    //                String columnName = resultSetMetaData.getColumnName(i+1);  // 不包括别名(获取到的是数据表结构的字段名)
                    // 2. 通过预编译的对象,获取到对应下标的内容(数值)
                    Object columnValue = resultSet.getObject(i+1); // 注意这里是Object的类型,因为我们无法确定数据是什么类型的

                    // 3. 通过反射机制,将数值存入到 orm 映射的类对象当中
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t); // 将orm对象存入到该集合当中
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            JdbcUtils.closeResource(connection,preparedStatement,resultSet);
            // PreparedStatement 接口是Statement的子接口
        }
        return null;
    }
}
