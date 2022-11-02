package Blogs.blogs02;

// 一张数据表对应一个Java类
public class User {
    private String user;  // 用户名
    private String password; // 密码
    private int balance; // 账号余额

    public User() {
        // 无参构造器,就算没有使用,也要创建提高代码的健壮性
    }

    public User(String user,String password, int balance) {
        this.user = user;
        this.password = password;
        this.balance = balance;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }

}
