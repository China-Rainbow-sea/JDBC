package day03;

import java.sql.*;

/**
 * 使用PreparedStatement 完成 insert delete update select
 */
public class HanshunpingJdbc {
    public static void main(String[] args) {
        HanshunpingJdbc hanshunpingJdbc = new HanshunpingJdbc(); // 实例化对象,静态方法调用非静态方法
      /*
        // insert 插入数据
        String sql = "insert into users(name,password) values(?,?)"; // ? 占位符,不要加'单引号'不然就是字符串了,失效了
        // 在Java当中的sql语句不用加;分号,不然报错
        hanshunpingJdbc.update(sql,"jk","999");
        */

        // update 修改数据表的数据
        String sql2 = "update users set name = ? where name = ?";
        hanshunpingJdbc.update(sql2,"jk6","jk");

        /*
        // delete 删除
        String sql3 = "delete from users where name = ?";
        hanshunpingJdbc.update(sql3,"jk");
*/


    }


    /**
     * 注册驱动,连接数据库
     * @return Connection
     */
    public Connection registration() {
        Connection connection = null;
        // 1.注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");  // 使用反射加载com.mysql.jdbc.Driver类,执行其中的静态代码块,注册驱动

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6??useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
        } catch (SQLException e) {
            throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
        }

        return connection;
    }


    /**
     * 获取增删改操作
     */
    public void update(String sql,Object...args) { // Object..args可变参数(位于最后不然无法识别,只有一个,可以不传参数(不要传null引用类型的,旧数组))
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于关闭资源

        try {
            // 1. 注册驱动,2.连接数据库
            connection = registration();

            // 3. 获取操作数据库的(预编译的Connection.PreparedStatement())对象
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql,并不会执行sql语句的

            // 4. 执行sql语句
            // 4.1 填充占位符
            for(int i = 0; i< args.length; i++){
                preparedStatement.setObject(i+1,args[i]);
                // 占位符从下标 1 开始,args从起始位置是 0 下标(旧数组)
            }

            int count = preparedStatement.executeUpdate(); // 执行sql语句,返回影响的行数
            System.out.println(count > 0 ? "成功" : "失败");

            // 5. 处理select 查询的结果集
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,从小到大
            close(connection,preparedStatement);
        }


    }


    /**
     * 关闭资源
     */
    public void close(Connection connection,PreparedStatement preparedStatement) {
        if( preparedStatement != null){ // !=null连接/使用了资源才需要关闭,==null没有连接/使用资源不需要关闭
            try {
                preparedStatement.close(); // 防止null引用报错
            } catch (SQLException e){
                throw new RuntimeException(e); // 将编译异常转为运行异常处理
            }
        }

        if( connection != null) {
            try{
                connection.close();
            } catch(SQLException e){
                throw new RuntimeException(e); // 将编译异常转为运行异常处理
            }
        }
    }

}
