package day05.ActorDAOExample;


import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 有关Actor数据表的 “增，删，改，查”操作的接口的封装
 */
public interface ActorDAO {  // interface 接口是不可以new的

    /**
     * 对数据表 actor 数据表的增删改
     * @param sql
     * @param args
     * @return int 返回影响数据库的行数
     */
    public int updateActor(String sql,Object...args);


    /**
     *  求数据表 actor 中最大的 birth
     * @param sql
     * @param args
     * @return Date 返回 actor 数据表中最大 birth
     */
    public Date maxBirth(String sql, Object...args);


    /**
     * 求数据表中的含有的记录条数
     * @param sql
     * @param args
     * @return long
     */
    public long countActor(String sql, Object...args);

    /**
     * 使用 ResultSetHandler接口中的BeanHandler 处理查询 “一条”记录
     * @param sql
     * @param args
     * @return Actor
     */
    public Actor getBeanHandler(String sql ,Object...args);


    /**
     * 使用ResultSetHandler接口中的BeanListHandler 处理查询 “多条记录”
     * @param sql
     * @param args
     * @return List
     */
    public List getBeanListHandler(String sql, Object...args);


    /**
     * 使用ResultSetHandler接口中的ArrayHandler 处理“一条”记录
     * @param sql
     * @param args
     * @return Object[]
     */
    public Object[] getArrayHandler(String sql,Object...args);


    /**
     * 使用ResultSetHandler接口中的ArrayListHandler 处理"多条" 记录
     * @param sql
     * @param args
     * @return List
     */
    public  List getArrayListHandler(String sql,Object...args);


    /**
     * 使用ResultSetHandler 接口中的Map<String,Actor>  处理“一条记录”
     * @param sql
     * @param args
     * @return Map<String,Object>
     */
    public Map<String, Object> getMapHandlers(String sql, Object...args);


    /**
     * 使用ResultSetHandler 接口中的MapListHandler 处理 “多条记录”
     * @param sql
     * @param args
     * @return List
     */
    public List getMapListHandlers(String sql,Object...args);


}
