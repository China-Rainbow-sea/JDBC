package Blogs.blogs03;

public class User {
    private String user;
    private String password;
    private int balance;

    public User() {
        // 无参构造器,就算不使用也定义创建，提高代码的复用性
    }

    public User(String user,String password,int balance) {
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
