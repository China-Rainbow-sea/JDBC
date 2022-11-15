package Blogs.blogs03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * UserDaoImpl 实现对 user_table数据表的增，删，改，查的操作，
 * 通过继承 BaseDAO封装的工具类,实现UserDAO接口中的抽象方法
 */
public class UserDaoImpl extends BaseDAO implements UserDAO{

    /**
     *
     * @param connection
     * @param user
     * @return int 影响数据库的行数
     */
    @Override
    public int insert(Connection connection, User user) {
        String sql = "insert into user_table(user,password,balance) values(?,?,?)"; // 占位符不要加单引号不然就成字符串了
        return super.update(connection,sql,user.getUser(),user.getPassword(),user.getBalance());
    }


    /**
     * 根据user字段名的值，删除user_table数据表中的记录/行
     * @param connection
     * @param user 影响数据库的行数
     */
    @Override
    public int deleteByUser(Connection connection, String user) {
        String sql = "delete from user_table where user = ?";
        return super.update(connection,sql,user);

    }



    /**
     * 通过User对象中的属性值修改更新数user_table 数据表信息
     * @param connection
     * @param user
     */
    @Override
    public int update(Connection connection, User user) {
        String sql = "update user_table set user = ? password = ? balance = ? where user = ?";
        return super.update(connection,sql,user.getUser(),user.getPassword(),user.getPassword(),user.getUser());
    }


    /**
     *  查询指定user_table 数据表中指定的 user的值
     * @param connection
     * @return User
     */
    @Override
    public User getUser(Connection connection,String user) {
        String sql = "select user,password,balance from user_table where user = ?";
        return super.getInstance(connection,User.class,sql,user);
    }


    /**
     * 查询user_table 数据表中的所有记录构成集合信息返回
     * @param connection
     * @return List<User>
     */
    @Override
    public List<User> getAll(Connection connection) {
        String sql = "select user,password,balance from user_table";
        return super.getForList(connection,User.class,sql);
    }


    /**
     *  查询user_table 数据表中记录总个数
     * @param connection
     * @return long
     */
    @Override
    public long getCount(Connection connection) {
        String sql = "select count(*) from user_table";
        return super.getValues(connection,sql);
    }


    /**
     *  查询user_table表中 balance 的最大值
     * @param connection
     * @return int
     */
    @Override
    public int getMaxBalance(Connection connection) {
        String sql = "select max(balance) from user_table";
        return super.getValues(connection,sql);
    }


    /**
     * 关闭资源,最晚使用的最先关闭
     * @param preparedStatement
     */
    @Override
    public void close(PreparedStatement preparedStatement, ResultSet resultSet) {
        super.close(resultSet,preparedStatement);

    }
}
