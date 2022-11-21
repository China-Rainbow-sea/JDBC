package day05.ActorDAOExample;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class Actorlmp extends BasicDAO<Actor> implements ActorDAO{

    /**
     * 对数据表 actor 数据表的增删改
     * @param sql
     * @param args
     * @return int 返回影响数据的行数
     */
    @Override
    public int updateActor(String sql, Object... args) {
        return super.update(sql,args);
    }

    /**
     * 求数据表 actor 中最大的 birth
     * @param sql
     * @param args
     * @return Date 最大的出生日期
     */
    @Override
    public Date maxBirth(String sql, Object... args) {
        return (Date)super.getScalarHandler(sql,args);
    }


    /**
     * 求数据表中的含有的记录条数
     * @param sql
     * @param args
     * @return long
     */
    @Override
    public long countActor(String sql, Object... args) {
        return (long)super.getScalarHandler(sql,args);
    }

    /**
     * 使用 ResultSetHandler接口中的BeanHandler 处理查询 “一条”记录
     * @param sql
     * @param args
     * @return Actor
     */
    @Override
    public Actor getBeanHandler(String sql, Object... args) {
        return super.getBeanHandler(sql,Actor.class,args);
    }


    /**
     *使用ResultSetHandler接口中的BeanListHandler 处理查询 “多条记录”
     * @param sql
     * @param args
     * @return List
     */
    @Override
    public List getBeanListHandler(String sql, Object... args) {
        return super.getBeanListHandler(sql,Actor.class,args);
    }

    /**
     * 使用ResultSetHandler接口中的ArrayHandler 处理“一条”记录
     * @param sql
     * @param args
     * @return Object[]
     */
    @Override
    public Object[] getArrayHandler(String sql, Object... args) {
        return super.getArrayHandler(sql,Actor.class,args);
    }

    /**
     * 使用ResultSetHandler接口中的ArrayListHandler 处理"多条" 记录
     * @param sql
     * @param args
     * @return  List
     */
    @Override
    public List getArrayListHandler(String sql, Object... args) {
        return (List)super.getArrayListHandler(sql,Actor.class,args);
    }

    /**
     * 使用ResultSetHandler 接口中的Map<String,Actor>  处理“一条记录”
     * @param sql
     * @param args
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> getMapHandlers(String sql, Object... args) {
        return super.getMapHandler(sql,args);
    }


    /**
     * 使用ResultSetHandler 接口中的MapListHandler 处理 “多条记录”
     * @param sql
     * @param args
     * @return List
     */
    @Override
    public List getMapListHandlers(String sql, Object... args) {
        return super.getMapListHandler(sql,args);
    }
}

