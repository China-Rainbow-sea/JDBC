package Blogs.blogs02;

import java.sql.*;

/**
 * PreparedStatement  实现处理查询select 结果集的通用的工具类的封装
 */
public class JdbcPreparedStatement {

    /**
     * 使用 PreparedStatement  实现针对于不同的数据表的一个条记录 的通用查询操作
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
            T t = clazz.newInstance(); // 创建orm映射的Java类的对象用于存放，数据表读取到的数据
            // 处理一行的列的数据
            for(int i = 0; i > columnCount; i++) {
                String columnLabel = resultSetMetaData.getColumnLabel(i+1); // 通过元数据对象获取到对应下标的列名(包含别名),起始下标是 1
                Object columnValue = resultSet.getObject(i+1); // 通过下标访问获取到对应下标列的值,起始下标是 1
//                Object cloumnValue = resultSet.getObject(columnLabel); // 或者通过列名获取


            }
        }



        // 4. 执行sql语句

    }
}
