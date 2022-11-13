package day04;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * UserDAOImpl 实现对 user_table表进行增，删，改，查的操作
 * 实现了 UserDAO接口，通过继承 BaseDAO实现 该接口中抽象方法
 */
public class UsersDAOImpl extends BaseDAO implements UserDAO {

    /**
     * 将users类中属性的值，插入到user_table数据表中
     * @param connection
     * @param users
     */
    @Override
    public void insert(Connection connection, Users users) {
        String sql = "insert into user_table(user,password,balance) values(?,?,?)"; // 占位符不要加单引号(不然就成了字符串了)
        super.update(connection,sql,users.getUser(),users.getPassword(),users.getBalance());  // 父类BaseDAO中的方法
        //     connection 连接数据对象,sql执行的语句,get填充占位符.

    }


    /**
     *  删除user_table数据表中 指定user 的字段内容
     * @param connection
     * @param user
     */
    @Override
    public void deleteByUser(Connection connection, String user) {
        String sql = "delete from user_table where user = ?";
        super.update(connection,sql,user);   // 父类BaseDAO中的方法
    }


    /**
     *  针对内存中的 users中的uses对象,去修改user_table数据表中指定的记录，字段值
     * @param connection
     * @param users
     */
    @Override
    public void update(Connection connection, Users users) {
        String sql = "update user_table set user = ?,password = ?,balance = ? where user = ?";
        super.update(connection,sql,users.getUser(),users.getPassword(),users.getBalance(),users.getUser());

    }


    /**
     * 针对指定的user查询得到对应的 Users对象
     * @param connection
     * @param user
     * @return Users
     */
    @Override
    public Users getUsers(Connection connection, String user) {
        String sql = "select user,password,balance from user_table where user = ?";
        Users users = super.getInstance(connection,Users.class,sql,user);
        return users;
    }


    /**
     *查询User_table表中所有记录构成集合
     * @param connection
     * @return List<Users>
     */
    @Override
    public List<Users> getAll(Connection connection) {
        String sql = "select user,password,balance from user_table";
        List<Users> list = super.getForList(connection,Users.class,sql);  // 可变参数可以为空
        return list;
    }


    /**
     * 查询user_table数据表中数据的条目数
     * @param connection
     * @return long
     */
    @Override
    public long getCount(Connection connection) {
        String sql = "select count(*) from user_table";
        return super.getValue(connection,sql);
    }


    /**
     * 查询User_table表中 balance最大的值
     * @param connection
     * @return int
     */
    @Override
    public int getMaxBalance(Connection connection) {
        String sql = "select max(balance) from user_table";
        return super.getValue(connection,sql);
    }

    @Override
    public void close(PreparedStatement preparedStatement, ResultSet resultSet) {
        super.close(preparedStatement,resultSet);
    }

}
