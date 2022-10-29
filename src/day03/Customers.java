package day03;

import java.sql.Date;

/**
 * orm编程思想对应 customers 数据表创建的类
 * 一个数据表对应一个Java类
 * 表中的一条记录对应java类的一个对象
 * 表中的一个字段对应Java类的一个属性
 */
public class Customers {
    private int id;
    private String name;
    private String email;
    private Date birth;

    public Customers() { // 无参构造器
    }

    public Customers(int id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public void setId(int id){
        this.id = id;
    }


    public int getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    // 重写 Customers 类中的 toString的方法;
    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}
