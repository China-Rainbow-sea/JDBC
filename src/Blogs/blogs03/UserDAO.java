package Blogs.blogs03;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * 对user_table表的增删改,查的工具类的封装的接口,
 * 接口不能实例化
 */
public interface UserDAO {


    /**
     * 将对对象user 的属性值，添加到数据库中
     * @param connection
     * @param user
     * @return int
     */
    int insert(Connection connection,User user);

    /**
     * 针对指定的user,删除user_table表中的一条记录
     * @param connection
     * @param user
     * @return int
     */
    int deleteByUser(Connection connection,String user);


    /**
     * 针对内存中的users 对象，去修改user_table数据表中指定的记录
     * @param connection
     * @param user
     * @return int
     */
    int update(Connection connection,User user);


    /**
     * 针对指定的user查询得到对应的 User对象
     * @param connection
     * @return User
     */
    User getUser(Connection connection,String user);


    /**
     * 查询user_table 数据表中的所有数据信息
     * @param connection
     * @return List<User>
     */
    List<User> getAll(Connection connection);

    /**
     *  查询user_table 数据表中数据的条目数
     * @param connection
     * @return long
     */
    long getCount(Connection connection);


    /**
     * 查询user_table 表中的 balance 的最大值
     * @param connection
     * @return int
     */
    int getMaxBalance(Connection connection);


    /**
     * 关闭资源
     * @param preparedStatement
     */
    void close(PreparedStatement preparedStatement, ResultSet resultSet);
}
