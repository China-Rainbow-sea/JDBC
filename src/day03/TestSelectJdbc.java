package day03;

import java.util.List;
/*
除了解决 Statement的拼串，sql注入问题之外,PreparedStatement 还有哪些好处
1.PreparedStatement 可以操作Blob(图片)的数据,而Statement做不到
2. PreparedStatement 可以实现更高效的批量操作
3. PreparedStatement 预编译,一次编译，多次执行,缓存
4. 可以提高代码的健壮性.setSting()指定类型
 */

/**
 * 测试SelectJdbc的select查询封装的工具
 */
public class TestSelectJdbc {
    public static void main(String[] args){
        TestSelectJdbc testSelectJdbc = new TestSelectJdbc();
        testSelectJdbc.selectJdbc();
        testSelectJdbc.selectJdbc2();
        testSelectJdbc.selectJdbc3();
        testSelectJdbc.selectJdbc4();
    }

    /**
     * 测试select的封装的工具类
     */
    public void selectJdbc() {
        SelectJdbc selectJdbcS = new SelectJdbc();
        String sql = "select user,password,balance from user_table where user = ?";
        // ? 占位符不要加'单引号',不然就变成字符串了,失效了,占位符从下标 1开始
        // Java当中sql语句不要加;分号,不然报错
        User user = selectJdbcS.getInstance(User.class, sql, "CC");
        System.out.println(user);
    }


    public void selectJdbc2() {
        SelectJdbc selectJdbc = new SelectJdbc();
        String sql = "select order_id as orderId, order_name as orderName, order_date as orderDate" +
                " from `order` where order_id = ?";
        Order order = selectJdbc.getInstance(Order.class,sql,"1");
        System.out.println(order);
    }


    /**
     * 测试select 多个结果集的处理
     */
    public void selectJdbc3() {
        SelectJdbc selectJdbc = new SelectJdbc(); // 实例化对象,静态方法调用非静态方法

        /*String sql = "select order_id as orderId, order_name as orderName, order_date as orderDate from `order` where" +
                " order_id < ?";
        List<Order> list = selectJdbc.getForList(Order.class,sql,3);
        list.forEach(System.out::println);*/

        String sql = "select id,name,email from customers where id < ?";
        List<Customers> list = selectJdbc.getForList(Customers.class,sql,5);
        list.forEach(System.out::println);
    }


    public void selectJdbc4(){
        SelectJdbc selectJdbc = new SelectJdbc(); // 实例化对象,静态方法调用非静态方法
        String sql = "select order_id as orderId, order_name as orderName, order_date as orderDate from `order`";
        List<Order> list = selectJdbc.getForList(Order.class,sql,null); // 可变参数可以不传参数,但是不要传null,特别是引用类型,防止null引用
        List<Order> list2 = selectJdbc.getForList(Order.class,sql); // 可变参数可以不传参数,但是不要传null,特别是引用类型,防止null引用
        list.forEach(System.out::println);
    }
}
