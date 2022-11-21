package day05.ActorDAOExample;

import java.sql.Date;

public class Actor {
    private int id;
    private String name;
    private String sex;
    private Date birth;
    private String phone;

    public Actor() {
        // 必须定义一个无参的构造器,用于反射获取对应的属性值，进行一个赋值操作
    }


    public Actor(int id, String name, String sex, Date birth, String phone) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.birth = birth;
        this.phone = phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                ", phone='" + phone + '\'' +
                '}';
    }
}
