package day03;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Scanner;

/**
 * 演示 Statement 的sql注入问题.以及拼写 sql语句
 * 导致 sql注入的根本原因是什么 ?
 * 用户输入的信息中含有 sql语句的关键字,并且这些关键字参与了编译以及执行的操作,sql语句原意被
 * 错误的扭曲并执行了,进入到达了 sql注入的问题。
 */
public class TestStatement {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名: ");
        String user = scanner.nextLine(); // next 不可以读取到空格,nextLine可以读取到空格
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();

        // 编写sql查询语句,在mysql中字符串需要加'单引号',在Java中使用sql不要加;(分号),
        String sql = "select user password from user_table where user = '"+user+"' and password = '"+password+"'";
        // 字符串的拼接使用 "++" (双引号++)

        User returnUser = get(sql,User.class);

        if( returnUser != null){
            System.out.println("登入成功");
        } else {
            System.out.println("用户不存在或者账号密码错误");
        }
    }





    // 使用 Statement 实现对数据表的查询操作
    public static <T> T get(String sql,Class<T> clazz) {
        T t = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;  // 扩大作用域,用于后面的关闭资源

        // 1. 加载驱动
        try {
            Class.forName("com.mysql.jdbc.Driver"); // 反射加载类,执行对应Driver类中的注册驱动的静态代码块
            // 2. 连接驱动中的数据库
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8",
                    "root",
                    "MySQL123");
            // 3. 获取操作数据库的对象
            st = conn.createStatement(); // 返回类型为 Statement 接口

            // 4. 通过获取到的对象执行 sql语句
            rs = st.executeQuery(sql);

            // 5. 查询select的处理结果集
            // 5.1 获取到当前查询到的结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 5.2 通过得到的元数据，获取到当前所查询到的结果集的列数(字段)总和
            int columnCount = rsmd.getColumnCount();

            // 处理一行的所有列数的数据
            if (rs.next()) { // next 每次调用都会向下移动,指向一行的指针,并判断该行的是否存在数据,存在返回true,不存在返回false
                t = clazz.newInstance();  // 创建orm 数据表类类型,用于存放从数据库表中获取到的数据
                for (int i = 0; i < columnCount; i++) {
                    // 1.获取当前查询的列名(包含显示的别名)
                    String columnName = rsmd.getColumnLabel(i + 1); // 注意第一列是从1开始的
                    // 2. 根据列名获取到对应的数据表中的数据(值)
                    Object columnValue = rs.getObject(columnCount);

                    // 3. 将数据表中得到的数据,封装进对象中
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }

                return t; // 查询到结果,将存放数据表的类类型 t,返回
            }

        } catch (ClassNotFoundException e) { // 快捷键 ctrl+alt+t
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}





