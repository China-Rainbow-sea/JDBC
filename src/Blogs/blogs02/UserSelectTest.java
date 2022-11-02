package Blogs.blogs02;

/**
 * 对应 UserSelect 对 user表的工具类封装的测试
 */
public class UserSelectTest {
    public static void main(String[] args) {
        UserSelect userSelect = new UserSelect(); // 实例化对象,静态方法调用非静态方法

        String sql = "select user,password,balance from user_table where user = ?";

        User user = userSelect.queryForUser(sql, "AA");
        System.out.println(user);

        User user2 = userSelect.queryForUser(sql,"CC");
        System.out.println(user2);
    }
}
