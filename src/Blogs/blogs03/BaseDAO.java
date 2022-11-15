package Blogs.blogs03;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 对通用数据表增,删,改，查 考虑上事务问题.进行处理.
 */
public abstract class BaseDAO {  // 抽象方法是不可以 new 的


    /**
     * 通用的查询特殊信息内容，如：count个数
     * @param connection
     * @param sql
     * @param args
     * @param <E>
     * @return  <E> E
     */
    public <E> E getValues(Connection connection,String sql,Object...args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);  // 预编译sql语句,并没有执行sql语句

            // 填充占位符,注意是在预编译sql语句之后
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            // 执行sql语句
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return (E) resultSet.getObject(1);  // 直接获取到第一条记录就可以了,因为就只是查看一条记录而已
                // 需要强制转换为 E 类型返回
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源,最晚使用的最先释放资源
            close(resultSet,preparedStatement);
        }
        return null;
    }

    /**
     *  通用查询处理操作，处理多条记录的结果集
     * @param connection
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return <T> List<T>
     */
    public <T> List<T> getForList(Connection connection,Class<T> clazz,String sql,Object...args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);  // 预编译sql语句

            // 填充占位符,注意占位符的填充在预编译之后,防止sql注入问题
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }

            resultSet = preparedStatement.executeQuery();  // 执行sql语句

            // 获取到select查询的结果集的元数据对象
            ResultSetMetaData resultSetMetaData = preparedStatement.getMetaData();

            // 通过元数据对象,获取到select 查询显示的一行记录中的所有总列数
            int columnCount = resultSetMetaData.getColumnCount();

            ArrayList<T> list = new ArrayList<T>();   // 创建集合对象,存放数据

            // 处理select 查询显示的结果集
            while(resultSet.next()) {
                T t = clazz.newInstance(); // 创建orm对象存放select查询读取到的数据信息
                // 处理一行的记录
                for(int i = 0; i < columnCount; i++) {
                    String columnLabel = resultSetMetaData.getColumnLabel(i + 1);  // 获取到select 查询显示的字段名/别名

                    Object columnValues = resultSet.getObject(i+1);

                    // 给 t 对象指定的columnLabel属性，赋值为 columnValue,通过反射的方式
                    Field field = clazz.getDeclaredField(columnLabel);  // columnLabel列名要与orm映射的类中的属性名一致
                    field.setAccessible(true);
                    field.set(t,columnValues);  // 赋值
                }

                list.add(t);  // 将查询select 获取的信息存储到集合链表当中
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
            // 关闭资源,最晚使用的最先关闭
            close(resultSet,preparedStatement);
        }

        return null;

    }


    /**
     * 对通用数据表查询，返回一条记录的封装,考虑上事务的问题(查询没有简单的回滚事务)
     * @param connection
     * @param clazz
     * @param sql
     * @param args
     * @param <T>
     * @return <T> T
     */
    public <T> T getInstance(Connection connection,Class<T> clazz,String sql,Object...args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);   // 仅仅是预编译sql语句,并没有执行sql语句

            // 填充占位符,注意在预编译之后填充占位符,防止sql注入问题
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]); // 占位符的填充的起始下标是从 1 开始的,而可变参数数组是从 0 开始的
            }

            resultSet = preparedStatement.executeQuery();  // 执行sql语句,注意是没有参数的方法,因为上面我们已经预编译过了

            // 处理select 查询的返回的结果集

            // 1. 获取到select 查询显示的结果集的元数据对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // 2. 通过获取到的元数据对象获取到select 查询显示的一行记录中(所有列/字段)的个数
            int columnCount = resultSetMetaData.getColumnCount();

            if(resultSet.next()) {  // next指向当前select查询显示的行记录(判断是否有数据true,并向下移动指针,没有数据false,)
                T t = clazz.newInstance();   // 创建orm映射的数据表的对象的类,用于存储获取到select查询获取到的数据

                // 处理select 查询显示的一行数据
                for(int i = 0; i < columnCount; i++) {
                    // 通过元数据对象获取到指定下标的位置的列名/别名
                    String columnLabel = resultSetMetaData.getColumnLabel(i + 1);  //该方法可以获取到select查询的别名

                    // 通过对应字段名/别名/对应下标获取到其字段的内容的值
                    Object columnValues = resultSet.getObject(columnLabel);


                    // 通过 t 对象指定的 columnLabel 名对应orm映射的属性，赋值为 columnValue,通过反射
                    Field field = clazz.getDeclaredField(columnLabel);  // 变量名对应select查询显示的字段名/别名,不然无法赋值
                    field.setAccessible(true);
                    field.set(t,columnValues);  // 赋值
                }
                return t;  // 返回存储对象
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
            // 关闭资源,最晚使用的最先释放空间
            close(resultSet,preparedStatement);
        }

        return null;

    }

    /**
     * 通用数据表的增删改,进行了事务处理
     * @param connection
     * @param sql
     * @param args
     * @return int
     */
    public int update(Connection connection,String sql,Object...args) {
        /* 可变参数位于参数的最后(不然无法识别区分), 只有一个,可以不传参数,但不要传null,防止null引用报错
        可变参数和旧数组类似
        * */

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql); // 仅仅只是预编译sql语句,并没有执行sql语句

            // 填充占位符,注意占位符的填充在预编译之后,防止SQL注入问题
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);  // 注意jdbc的起始下标是 1,而数组的起始下标是 0
            }

            return preparedStatement.executeUpdate();  // 执行sql语句,返回影响数据库的行数
        } catch (SQLException e) {
            /*if(connection != null) {
                try {
                    connection.rollback();   // 发生异常事务回滚
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 编译异常转换为运行异常抛出
                }
            }*/
            e.printStackTrace();
        } finally {
            // 关闭资源
            close(null,preparedStatement);
        }

        return 0;
    }




    /**
     * 关闭资源,最晚使用的最先关闭资源/释放资源,
     * 注意因为这里我们考虑上了事务的问题,Connection 连接不要关闭:
     * 因为当数据库连接一旦关闭了,就会自动 commit 提交数据,导致无法回滚信息.
     * @param resultSet
     * @param preparedStatement
     */
    public void close(ResultSet resultSet, PreparedStatement preparedStatement) {
        // !=null 连接/使用了资源需要关闭资源,==null 没有连接/使用的资源是不需要关闭的

        if(resultSet != null) {   // 防止null引用的问题
            try{
                resultSet.close();  // 关闭select 查询对象的资源
            } catch(SQLException e) {
                throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
            }

        }


        if(preparedStatement != null) { // 防止null引用报错
            try {
                preparedStatement.close();   // 关闭操作数据库对象资源
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
