package day03;

import java.sql.*;

/**
 * 使用PreparedStatement 完成 insert delete update select
 */
public class HanshunpingJdbc {
    public static void main(String[] args){
        HanshunpingJdbc hanshunpingJdbc = new HanshunpingJdbc(); // 实例化对象,静态方法调用非静态方法
        hanshunpingJdbc.testDMl();
    }



    /**
     * insert delete update 的测试
     * @param args
     */
    public static void main1(String[] args) {
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


    public void testDMl() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;  // 扩大作用域,用于关闭资源

        try {
            // 1. 注册驱动, 2.连接数据库
            connection = registration();

            // 3. 获取操作数据库对象
            String sql = "select `name`, password from users where name = ?"; // 占位符不要加'单引号'不然就变成字符串了,失效了
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并不会执行sql

            // 4. 执行sql语句
            // 4.1 填充占位符,(占位符起始下标是 1, 基本所有的jdbc的下标都是从 1 开始的)
            preparedStatement.setString(1,"Tom");
            resultSet = preparedStatement.executeQuery();  // 执行sql语句,注意是无参数方法,因为上面已经预编译过了

            // 5. 处理select 查询显示的结果集
            while(resultSet.next()) { // next:表示指向的当前行的指针,并判断当前行(记录)是否有数据:有true,没有false,每次调用都会向下移动(指向下一行(记录))
                // 处理一行(记录)的所有列
                String name = resultSet.getString("name"); // 根据select查询显示的结果集的(列号(从下标 1 开始)/显示的列名(字段名 包括别名)获取对应的数据
                String password = resultSet.getString("password");  // getString表示:无论你从数据库中读取到的是什么类型的数据都转化为String的形式赋值,
                // 同理还有getInt,getDouble
                System.out.println(name+"->"+password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            if( resultSet != null){
                try{
                    resultSet.close();
                } catch(SQLException e){
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出;
                }
            }
            close(connection,preparedStatement);
        }


    }

}
