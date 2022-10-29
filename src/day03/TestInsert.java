package day03;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 使用 PreparedStatement 来替换 Statement,实现对数据表的增删改的操作
 * PreparedStatement 接口是 Statement 的子接口
 */
public class TestInsert {
    public static void main(String[] args){

        Connection connection = null;
        PreparedStatement ps = null;  // 扩大作用域,用于关闭资源

        try {
            // 1. 注册驱动(说明你要连接的什么品牌的数据库)
            Class.forName("com.mysql.jdbc.Driver"); // 使用反射加载类,执行其中com.mysql.jdbc.Driver包下的类的静态代码注册驱动
            // 2. 通过驱动连接对应的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8","root","MySQL123");
            // 3.获取操作数据库的对象(预编译操作数据库的对象)
            String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
        /*
        ？？？ 表示占位符,占位符不要加 '单引号',加了的话就会被当作是字符串了,占位符的作用就失效了,
        在mysql中sql语句要加;分号,在Java当中不要加;分号,不然报错的.
         */
            ps = connection.prepareStatement(sql); // 注意只是预编译sql语句,并没有执行
//        预编译:就是编译现在传递的sql关键字,其中占位符所表示的信息,不会被当作是关键字执行的,而是会加上'单引号',
            // 防止与关键字冲突,以及sql注入问题

            // 4.执行sql语句
            // 4.1 填充占位符的信息 / 注意占位符是从第一个占位符开始,从下标 1 开始,不是 0 ,在jdbc中基本上都是从 1 开始的
            ps.setString(1,"哪吒");   // 第一个占位符的填充
            ps.setString(2,"nazha126@.com"); // 第二个占位符的填充
            ps.setString(3,"1000-1-01");  // 在mysql当中一定格式的字符串可以隐式转换为 日期时间类型
            ps.setString(4,null);
//            ps.setInt() 当你填充的是什么类型的数据就用,什么类型的方法,填充,提高代码的健壮性

            int count = ps.executeUpdate();  // 执行 sql语句返回影响数据库的行数
            System.out.println(count == 1 ? "添加成功" : "添加失败" );
            // 或者 ps.execute // 返回的是 boolean类型,如果是增删改,就返回true,如果不是就返回false


            // 5. 处理结果集,这里不是的第四步不是 select 不用处理结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            if(ps != null){  // ps 不为空表示,该资源被连接使用了,才需要关闭,没有连接打开就不需要关闭
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if( connection != null){ // connection 不为空,该资源连接使用了,才需要关闭,没有打开和连接使用就不需要关闭
                try{
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }

}




/*
public interface PreparedStatement
extends Statement表示预编译的SQL语句的对象。
SQL语句已预编译并存储在PreparedStatement对象中。 然后可以使用该对象多次有效地执行此语句。
注意：setter方法（ setShort ， setString用于设置IN参数值必须指定与所定义的SQL类型的输入参数的
 */