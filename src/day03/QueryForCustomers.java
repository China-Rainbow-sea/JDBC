package day03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 针对 test数据库数据表的通用的查询操作
 */
public class QueryForCustomers {

    /**
     * 针对customer表的通用的查询操作
     */
    public Customers queryForCustomers(String sql,Object...args){ //Object类型的可变参数的位于最后(不然无法识别到),只有一个,旧数组
        Connection connection = null;
        PreparedStatement preparedStatemet = null;
        ResultSet resultSet = null;


        // 1.注册驱动, 2.连接驱动的数据库
        connection = JdbcUtils.getConnection();

        // 3.获取操作数据库的对象(connection.PreparedStatement预编译的对象)
        preparedStatemet = connection.prepareStatement(sql);
        // 填充 ？占位符的,从下标 1 开始,jdbc基本上都是从 1下标开始的,?占位符不要使用'单引号',不然变为字符串的就失效了
        for(int i = 0 ; i <args.length;i++){
            preparedStatemet.setObject(i+1,args[i]); // setObject为占位符填充
            // ? 占位符从 下标 1开始,args可变参数(旧数组从 0下标开始)
        }

        // 4.执行sql语句
        resultSet = preparedStatemet.executeQuery(); // 注意是无参数的方法,因为上面我们已经编译过了

        // 5. 处理select 的查询的结果集
        // 5.1 获取当前select 查询到的元数据 ResultSetMetData
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        // 5.2 通过元数据，获取到当前select查询到的结果集的列(行)的总数
        int columnCount = resultSetMetaData.getColumnCount();

        if(resultSet.next()){ // 指向当前select查询到的行的记录,并判断该行是否有数据,有ture,没有false,每调用一次就向下移动
            Customers customers = new Customers(); //创建数据表的对象,存放数据
            // 处理读取到的一行的所有列的数据
            for(int i = 0; i<columnCount; i++){
                Object columName = resultSet.getObject(i+1); // 获取到 i+1 列的列名,(注意这里不包含select查询显示的别名),从下标1 开始
                Object columValue = resultSet.getObject(i+1); //读取 i+1 列的内容的数据,从下标 1开始

                // 通过反射将获取到的数据表的内容,一一匹配赋值到orm类对象中去
            }
        }


    }
}
