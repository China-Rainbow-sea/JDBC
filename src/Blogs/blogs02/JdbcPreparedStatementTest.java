package Blogs.blogs02;

import day03.Order;

import java.util.List;
import java.util.Scanner;

/**
 * JdbcPreparedStatement 的测试
 */
public class JdbcPreparedStatementTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JdbcPreparedStatement jdbcPreparedStatement = new JdbcPreparedStatement(); // 实例化对象,静态方法调用非静态方法
        // user_table表
        System.out.print("请输入你要在user_table查询的大于balance的值: ");
        int balance = scanner.nextInt();
        String sql = "select user,password,balance from user_table where balance >=  ?"; // ?占位符不要加单引号,不然就是字符串了,失效了
        List<User> userList = jdbcPreparedStatement.getForList(User.class,sql,balance);
        userList.forEach(System.out::println);
        System.out.println("***********************************");

        //  // obers表
        System.out.print("请输入你要在obers表中查询的id的值: ");
        int id = scanner.nextInt();
        String sql2 =
                "select order_id as orderId,order_name as orderName,order_date as orderDate from obers where order_id" +
                        " < ?"; //
        // 注意sql语句的执行顺序,别名的作用范围
        List<Obers> obersList = jdbcPreparedStatement.getForList(Obers.class,sql2,id);
        obersList.forEach(System.out::println);

    }


    /**
     * 对jdbcPreparedStatement.getInStance 处理单个查询记录的测试
     * @param args
     */
    public static void main2(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JdbcPreparedStatement jdbcPreparedStatement = new JdbcPreparedStatement(); // 实例化对象,静态方法调用非静态方法

        // user_table表
        System.out.print("请输入你要在user_table查询的user: ");
        String user = scanner.next();
        String sql = "select user,password,balance from user_table where user = ?"; // ?占位符不要加单引号,不然就是字符串了,失效了
        User users = jdbcPreparedStatement.getInStance(User.class, sql, user);

       // obers表
        System.out.println(users);
        System.out.print("请输入你要在obers表中查询的id: ");
        int id = scanner.nextInt();
        String sql2 = "select order_id as orderId,order_name as orderName,order_date as orderDate from obers where " +
                "order_id = ?"; // 注意sql语句的执行顺序,别名的作用范围
        Obers obers = jdbcPreparedStatement.getInStance(Obers.class, sql2, id);
        System.out.println(obers);


    }
}
