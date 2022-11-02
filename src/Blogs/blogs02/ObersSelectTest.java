package Blogs.blogs02;

import java.util.Scanner;

public class ObersSelectTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入你要查询的 Order_id 的值: ");
        int orderId = scanner.nextInt();

        ObersSelect obersSelect = new ObersSelect(); // 实例化对象,静态方法调用非静态方法
        String sql = "select order_id as orderId,order_name as orderName, order_date as orderDate from obers where " +
                "order_id = ?";  // 注意这里需要使用上别名,因为在Java当中的属性命名是小驼峰的方式没有下划线,
        Obers obers = obersSelect.queryForObers(sql, orderId);
        System.out.println(obers);

        System.out.print("请输入你要查询的 Order_id 的值: ");
        orderId = scanner.nextInt();
        Obers obers1 = obersSelect.queryForObers(sql,orderId);
        System.out.println(obers1);
    }
}
