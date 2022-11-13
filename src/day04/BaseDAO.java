package day04;

import com.sun.deploy.net.MessageHeader;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义一个用来被继承的对数据库进行基本操作的Dao(功能: 增删改,查操作)
 * abstract 抽象类不可以new
 */
public abstract class BaseDAO {


    /**
     * 通用的查询指定特殊的字段值
     * @param connection
     * @param sql
     * @param args
     * @param <E>
     * @return <E> E
     */
    public <E> E getValue(Connection connection,String sql,Object...args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        // jdbc code
        // 获取操作数据库的对象(预编译sql)语句
        try {
            preparedStatement = connection.prepareStatement(sql);

            // 执行sql语句
            for(int i  = 0; i < args.length; i ++) {
                preparedStatement.setObject(i+1,args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            // 处理select 查询的结果集数据
            if(resultSet.next()) {
                return (E) resultSet.getObject(1); // 直接获取到第一行,第一条的查询记录.
                // 这里强制转换为我们需要的(E)的类型,
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // 将编译异常转换为运行时异常抛出
        } finally {
            // 关闭资源,最晚使用的最先释放空间
            this.close(preparedStatement,resultSet);
        }
        return null;
    }



    /**
     * 通用查询处理操作，处理多条记录的结果集
     * @param connection
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return  <T> List<T>
     */
    public <T> List<T> getForList (Connection connection, Class<T> clazz, String sql, Object...args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 3. 获取到操作数据库(预编译sql)的对象
            preparedStatement = connection.prepareStatement(sql);  // 仅仅只是预编译sql语句,并不执行

            // 4. 执行sql语句
            // 填充占位符
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符的起始下标是 1,数组的起始下标是 0
            }
            resultSet = preparedStatement.executeQuery(); // 执行sql语句,注意是无参数的因为上面我们已经预编译sql过了

            // 5. 处理select 查询显示的结果集
            // 5.1 获取到当前select 查询显示的结果集的元数据的对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // 5.2 通过获取到的元数据对象 REsultSetMetaData 获取到当前select 查询的结果集的一行中(所以列的总数)
            int columnCount = resultSetMetaData.getColumnCount();

            ArrayList<T> list = new ArrayList<T>();  // 创建集合对象,存放多个数据

            while(resultSet.next()) {  // next()指向当前select查询显示的结果集,判断该行是否含有数据,有true,并向下移动指针,没有false
                T t = clazz.newInstance();  // 创建orm对象,存储查询显示的结果集

                // 处理一行(所有的记录列数)
                for(int i = 0; i < columnCount; i++) {
                    // 1. 通过元数据对象获取到当前select结果集对应下标的列名(别名),字符串类型
                    String columnLabel = resultSetMetaData.getColumnLabel(i+1); // jdbc 起始下标位置是从 1开始的

                    // 2. 获取到指定下标位置中的列中的数据信息
                    Object columnValue = resultSet.getObject(i+1); // 起始下标是从 1 开始的

                    // 给 t 对象指定的columnLabel属性，赋值为 columnValue,通过反射的方式
                    Field field = clazz.getDeclaredField(columnLabel);  // columnLabel列名要与orm映射的类中的属性名一致
                    field.setAccessible(true);
                    field.set(t,columnValue); // 赋值
                }
                list.add(t);  // 将一行的记录，添加到集合链表中
            }
            return list;    // 返回集合中存放了查询的数据的集合的链表
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的先关闭
            this.close(preparedStatement,resultSet);
        }

        return null;
    }



    /**
     * 通用的查询操作,用于返回数据表中的一条记录,考虑上事务性，的回滚要求实现
     * @param connection
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return <T> T
     */
    public <T> T getInstance(Connection connection,Class<T> clazz,String sql,Object...args) {
        // 可变参数,最后,一个,可以不传参数,但不要传null,防止null引用
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;   // 扩大作用域,用于关闭资源

        try {
            // 3. 获取到操作数据库(预编译sql语句的)对象
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并没有执行sql

            // 4. 执行sql语句
            // 4.1 填充占位符信息
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符的填充:占位符的起始下标是 1,可变参数(数组)是 0
            }
            resultSet = preparedStatement.executeQuery(); // 执行sql语句,返回select 查询的结果集对象

            // 5. 处理select 查询显示的结果集
            // 5.1 获取到当前select 查询显示的结果集的元数据
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // 5.2 通过获取到的元数据对象,得到当前select 查询显示的一行(记录)的所有列数的总列数
            int columnCount = resultSetMetaData.getColumnCount();

            // 5.3
            if(resultSet.next()) {  // next()指向select查询显示的结果集,判断该行(记录)是否含有数据,有true,并向下移动指针,没有false
                T t = clazz.newInstance();   // 创建orm映射的对象,用于存放查询结果的数据

                // 处理一行(记录)的所有数据信息
                for(int i = 0; i < columnCount; i++) {
                    // 1. 通过元数据对象获取到当前select 查询显示的对应下标的列名(字符串),起始下标是从 1 开始的
                    String columnLabel = resultSetMetaData.getColumnLabel(i+1); // 该方法包含(别名)

                    // 2. 获取到对应下标的列(字段)中的数据信息
                    Object columnValue= resultSet.getObject(i+1);
                    // 或者使用(字段名/使用了别名,就要用别名了)
                    // Object columnName = resultSet.getObject(columnLabel);

                    // 3. 通过 t 对象指定的 columnName名对应的orm映射的属性,赋值为 columnValue,通过反射
                    Field field = clazz.getDeclaredField(columnLabel);  // 变量名
                    field.setAccessible(true);
                    field.set(t,columnValue);  // 赋值的内容

                }
                    return t;   // 返回存储的对象

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
            // 6. 关闭资源,最晚使用的最先释放空间,考虑到connection.close()关闭连接会自动commit提交数据
            this.close(preparedStatement,resultSet);
        }
        return null;

    }




    /**
     * 通用的增，删，改操作 考虑上事务问题
     * 需要注意的是,我们这里不要关闭连接Connection.close()，因为关闭连接默认就commit 提交数据了.
     * 无法回滚了.
     * @param sql
     * @param args
     * @return int
     */
    public int update(Connection connection, String sql, Object...args) {
        /*Object...args 可变参数,位于参数的最后(不然无法区别),可变参数只能有一个,
        可变参数可以不传值，但不要传null,防止null引用问题。可变参数就旧数组。*/
        PreparedStatement preparedStatement = null;
        try {

            // 3.获取操作数据库预编译sql语句的对象
            preparedStatement = connection.prepareStatement(sql); // 仅仅只是预编译sql语句,但是并不执行

            // 4. 执行sql语句
            // 4.1 填充占位符
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符起始下标是 1开始,可变参数(数组)起始下标是从 0 开始的
            }

            // 4.2 执行sql语句
            return preparedStatement.executeUpdate();

            // 5. 处理select 查询的结果集
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            // 6. 关闭资源从,最晚使用的最先释放空间
            this.close(preparedStatement,null);

        }

        return 0;
    }



    /**
     * 关闭资源,最晚使用的最先释放空间,这里考虑上 Connection.close()关闭连接(commit 数据),
     * 不在这里关闭连接
     * @param  preparedStatement
     * @param resultSet
     */
    public void close(PreparedStatement preparedStatement, ResultSet resultSet) {
        // !=null 连接/使用了的才需要关闭资源,==null没有连接/使用的不需要关闭资源

        if(resultSet != null) {
            try {
                resultSet.close();   // 关闭处理select 查询的结果集的对象资源
            } catch( SQLException e) {
                throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出
            }
        }

        if(preparedStatement != null) {  // 防止null引用
            try {
                preparedStatement.close();   // 关闭预编译sql语句操作数据库对象连接
            } catch(SQLException e) {
                throw new RuntimeException(e);  // 将编译异常转化为运行时异常抛出
            }
        }

    }






}
