package day04;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * ORM映射user_table 数据表中的接口，功能实现: 对数据的，增，删，改，查，考虑上事务因素
 * 接口是不可以 new 的
 */
public interface UserDAO {

    /**
     * 将对象users 的属性值，添加到数据库中
     * @param connection
     * @param users
     */
    void insert(Connection connection, Users users);


    /**
     *  针对指定的user，删除user_table表中的一条记录
     * @param connection
     * @param user
     */
    void deleteByUser(Connection connection,String user);


    /**
     * 针对内存中的 users 对象,去修改user_table数据表中指定的记录，字段值
     * @param connection
     * @param users
     */
    void update(Connection connection,Users users);


    /**
     *  针对指定的user查询得到对应的 Users对象
     * @return Users
     */
    Users getUsers(Connection connection,String user);

    /**
     * 查询User_table表中所有记录构成集合
     * @param connection
     * @return
     */
    List<Users> getAll(Connection connection);

    /**
     * 查询user_table数据表中数据的条目数
     * @param connection
     * @return long
     */
    long getCount(Connection connection);


    /**
     * 查询User_table表中 balance最大的值
     * @param connection
     * @return int
     */
    int getMaxBalance(Connection connection);

    /**
     * 关闭资源
     * @param preparedStatement
     * @param resultSet
     */
    void close(PreparedStatement preparedStatement, ResultSet resultSet);

}
