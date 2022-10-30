package day03;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 对数据库 order表的通用select封装
 */
public class QueryForOrder {

    /**
     * 对 order select 的封装
     * @return Order
     */
    public Order querForOrders(String sql,Object...args){ // Object...args可变参数位于最后参数(不然无法识别),只有一个,旧数组
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;  // 扩大作用域,用于关闭资源


        try {
            // 1.注册驱动,2.连接数据库
            connection = JdbcUtils.getConnection();

            // 3. 获取操作数据库的对象(这里是Connection.PreparedStatement() 预编译对象)
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并没有执行sql语句

            // 4. 执行sql语句
            // 填充占位符:
            for(int i = 0; i < args.length; i++){
                preparedStatement.setObject(i+1,args[i]);
                // 占位符从1下标开始,而可变参数(旧数组)从0下标开始
            }



            resultSet = preparedStatement.executeQuery(); // 执行sql语句,注意是没有参数的,因为上面我们已经执行过了

            // 5. 处理当前select 查询的结果集
            // 5.1 获取到当前select的结果集的元数据
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 5.2 根据元数据获取到当前select查询显示的所有的行的(列数)
            int columnCount = resultSetMetaData.getColumnCount();

            while(resultSet.next()){  // 指向当前的行(记录),并判断该行是否含有数据,有true,没有false,每调用该方法,就会自动向下移动,指向新的一行数据
                Order order = new Order(); // 创建orm映射的对象,用于存放从数据库中获取到的数据内容
                // 5.3 处理当前select查询到的一行(所有列)的数据
                for(int i = 0; i < columnCount; i++){
                    // 1. 通过当前元数据对象,获取到第 i+1 列的select 显示的列名(列名是字符串)
//                    String columnName = resultSetMetaData.getColumnName(i+1); // 注意是从下标 1开始的,jdbc基本都是从下标 1 开始的
                    /*
                    java.lang.NoSuchFieldException: order_id 注意了(resultSetMetaData.getColumnName) 读取的是在实际数据表定义的
                    字段名(列名)的信息,并不会读取到我们实际select显示的列名(字段名),这就会导致我们在orm的对象存储的时候出现问题
                    在Java当中的属性命名是小驼峰的命名格式，而在mysql当中使用的是下划线的命名格式,就会出现Java属性名与数据表的(字段名/列名)
                    不匹配,导致赋值对应列的内容失败,所以这里我们使用另外一个方法
                    resultSetMetaData.getColumnLabel(i+1) 读取的是select实际查询显示的列名(字段名)(包含别名),没有别名就是(默认的字段名)
                     */
                    String columnName = resultSetMetaData.getColumnLabel(i+1);


                    // 2. 通过执行sql返回的对象,获取到对应下标(i+1)的列名下的数据内容,同样是从 下标 1 开始的
                    Object columnValue = resultSet.getObject(i + 1);

                    // 3. 通过反射机制,将我们从数据库读取到的内容赋值到orm映射的order对象中
                    Field field = Order.class.getDeclaredField(columnName);  // 通过列名对应上 order的属性名
                    field.setAccessible(true);  // 改为true
                    field.set(order,columnValue); // 根据上面的列名(对应order的属性名)将值赋值到对应的order属性上
                }
                return order;  // 返回存储的数据的对象地址
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            JdbcUtils.closeResource(connection,preparedStatement,resultSet);
        }
        return null;

    }
}
