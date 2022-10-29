package day03;

import java.sql.*;
import java.util.Arrays;

/**
 * 处理结果集
 */
public class TestQuery {
    public static void main1(String[] args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;  // 扩大作用域,用于关闭资源

        try {
            // 1. 注册驱动,2.连接数据库
           connection = JdbcUtils.getConnection();

            // 3. 获取操作数据库的对象(PreparedStatement.executeQuery)
            String sql = "select id d,name,email,birth from customers where id = ?";
//            String sql = "select id,name,email,birth from customers";
            preparedStatement = connection.prepareStatement(sql); // 预编译 sql语句,并没有执行sql,解决的sql注入问题

            // 4. 执行sql语句
            // 填充 ？ 占位符,从下标 1 开始
            preparedStatement.setInt(1,13);
            resultSet = preparedStatement.executeQuery();// 注意是无参数的方法,因为上面我们已经预编译了 ,返回resultSet接口

            // 5. 处理结果集
            while(resultSet.next()){ // resultSet.next()指向当前行的数据,调用一次便向下移动,并判断当前行是否含有数据,有 true,没有false
                // 获取对应查询列中的数据内容
                int id = resultSet.getInt("d");  // 注意这里的字段名是select查询显示的结果集的字段名,包括别名,
    //            int id1 = resultSet.getInt(1); // 或者使用列名的下标
                String name = resultSet.getString(2);  // 表示获取到第2列的数据内容
                String email = resultSet.getString(3); // getString表示无论你查询到的内容是什么都是以字符串的形式赋值到变量中的
                Date birth = resultSet.getDate(4);  // getDate表示无论你所查询到的内容是什么都是以date日期时间类型赋值到变量中的
//                String birth = resultSet.getString(4);

                // 第一种处理方式：打印显示
                System.out.println(id+","+name+","+email+","+birth);

                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");

                // 第二种处理方式：Object 数组
                Object[] data = new Object[]{id,name,email,birth};
                System.out.println(Arrays.toString(data));  // 打印数组

                // 打印数组
                for (Object temp:data) {
                    System.out.print(temp);
                }
                System.out.println();
                // 打印数组
                for(int i = 0; i<data.length; i++){
                    System.out.print(data[i]);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            if(resultSet != null){  // !=null,连接使用才需要,关闭,没有使用是不需要关闭的
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                } catch(SQLException e){
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



    public static void main(String[] args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;


        try {
            // 1. 注册驱动，2.连接驱动的数据库
            connection = JdbcUtils.getConnection();

            // 3. 获取操作数据库对象(connection.preparedStatement预编译对象)
            String sql = "select id,name,email,birth from customers";
            preparedStatement = connection.prepareStatement(sql); // 预编译sql,并没有执行sql

            // 4. 执行sql 语句
            resultSet = preparedStatement.executeQuery();  // 注意是无参数类型的,因为上面我们已经预编译过了

            // 5. 处理select 查询的结果集
            while(resultSet.next()){ // next指向当前行的记录,并判断该行的数据是否含有数据,有true,没有false,每调用一次都会向下移动
                int id = resultSet.getInt("id");  // 获取当前select查询的结果集名为id字段的数据(包含别名)
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");

                // 将数据存入到 orm 映射数据表的类类型的对象中去
                Customers customers = new Customers(id,name,email,birth);
                System.out.println(customers);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            // 6. 关闭资源
            if(resultSet != null){ // != null 只有使用了连接了资源,才需要关闭资源,美哟使用连接(null)是不需要关闭的
                try{
                    resultSet.close();  // null 判断防止没有使用,反而关闭了,null引用
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if( preparedStatement != null){
                try{
                    preparedStatement.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if( connection != null){
                try{
                    connection.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

        }


    }
}



/*
executeQuery
ResultSet executeQuery()
                throws SQLException
执行此 PreparedStatement对象中的SQL查询，并返回查询 PreparedStatement的 ResultSet对象。
 */