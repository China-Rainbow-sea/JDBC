package day04;

public class Users {
    private String user;
    private String password;
    private int balance;

    public Users() {
        // 无参构造器,无论是否使用无参构造器都定义,提高代码的可读性
    }

    public Users(String user, String password, int balance) {
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
