package Blogs.blogs02;

import java.util.Scanner;

/**
 * 对JdbcUpdate 封装的工具类测试
 */
public class JdbcUpdateTest {

    /**
     * delete 删除数据
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JdbcUpdate jdbcUpdate = new JdbcUpdate(); // 实例化对象,静态方法访问非静态方法

        System.out.print("输入user_table中要删除的user: ");
        String user = scanner.next();

        String sql = "delete from user_table where user = ?";
        jdbcUpdate.jdbcUpdate(sql,user);
    }



    /**
     * update 修改数据
     */
    public static void main2(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JdbcUpdate jdbcUpdate = new JdbcUpdate(); // 实例化对象,静态方法访问非静态方法

        System.out.print("输入user_table中的user: ");
        String user = scanner.next();
        System.out.print("输入user_table中修改后的的password:");
        String password = scanner.next();

        String sql = "update user_table set password = ? where user = ?";
        jdbcUpdate.jdbcUpdate(sql,password,user);

    }


    /**
     * insert 插入数据测试
     * @param args
     */
    public static void main1(String[] args) {
        Scanner scanner = new Scanner(System.in);
        JdbcUpdate jdbcUpdate = new JdbcUpdate(); // 实例化对象,静态方法访问非静态方法

        System.out.print("输入user_table中的user: ");
        String user = scanner.next();
        System.out.print("输入user_table中的password:");
        String password = scanner.next();
        System.out.print("输入user_table中的balance:");
        int balance = scanner.nextInt();

        String sql = "insert into user_table(user,password,balance) values(?,?,?)";  // ? 不要加单引号,Java当中的sql语句不用加;分号结尾
        jdbcUpdate.jdbcUpdate(sql,user,password,balance);
    }
}
