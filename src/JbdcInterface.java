/*
模拟 java 的调用：JDBC 的使用调用
 */

public interface JbdcInterface {
    // interface 接口

    // 抽象方法
    // 连接数据库方抽象方法
    public Object getConnection();

    // 抽象方法: 数据库的增删改查
    public void crud();

    // 抽象方法:关闭连接数据库的资源
    public void close();



}
