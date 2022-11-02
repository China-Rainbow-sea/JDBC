package Blogs.blogs02;

import java.sql.Date;

public class Obers {
    private int orderId;  // 用户id
    private String orderName; // 订单名字
    private Date orderDate; // 订单日期

    public Obers() {
        // 无参数构造器,无论是否会被使用到都创建出来,提高代码的健壮性
    }

    public Obers(int orderId,String orderName, Date orderDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
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


    @Override
    public String toString() {
        return "Obers{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", orderDate=" + orderDate +
                '}';
    }
}
