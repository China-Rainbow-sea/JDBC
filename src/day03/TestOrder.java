package day03;

/**
 * 测试order的selec查询的封装工具
 */
public class TestOrder {
    public static void main(String[] args){
        TestOrder testOrder = new TestOrder(); // 实例化对象,静态方法调用非静态方法
        testOrder.selectOrder();
        testOrder.selectOrder2();

    }


    public void selectOrder() {
        QueryForOrder queryForOrder = new QueryForOrder();
//        String sql = "select order_id,order_name,order_date from `order` where order_id = ?";
        String sql = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id =" +
                " ?";  // 注意sql的执行顺序别名的作用范围 where 在 select 的前面
        // ？占位符不要加'单引号',加了就变成字符串了,没有效果了,
        // java当中的sql语句不用加;(分号结尾),加了报错
        Order order = queryForOrder.querForOrders(sql, "1");
        System.out.println(order);

    }


    public void selectOrder2() {
        QueryForOrder queryForOrder = new QueryForOrder();
        String sql = "select order_id orderId, order_name orderName, order_date as orderDate from `order`";
        Order order = queryForOrder.querForOrders(sql); // 可变参数可以不传参数的,但是不可以传null,不然就是null引用了(特别是对于引用类型)
        System.out.println(order);
    }

}
