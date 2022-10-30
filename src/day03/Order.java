package day03;

import java.sql.Date;

/**
 * order数据表的 ORM映射
 * 一个数据表对应一个java类
 * 一行(一条)记录对应一个Java类对象
 * 一列记录对应一个Java的属性
 *
 */
public class Order {
    private int orderId;
    private String orderName;
    private Date orderDate;


    public Order(){
        // 无参构造器,无论是否使用定义创建
    }

    public Order(int orderId, String orderName, Date orderDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }

    public void setOrderId(int orderId){
        this.orderId = orderId;
    }

    public int getOrderId(){
        return this.orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override  // 覆盖
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }

}
