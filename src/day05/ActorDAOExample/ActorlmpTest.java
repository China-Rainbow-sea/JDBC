package day05.ActorDAOExample;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;

import java.sql.Array;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ActorlmpTest {
    private static Actorlmp actorlmp = new Actorlmp();    // 创建Actorlmp 对象作为类的属性存在
    public static void main(String[] args) {
        // insert 插入数据
        String sql = "insert into actor(name,sex,birth,phone) values(?,?,?,?)"; // 占位符不要加单引号不然就成了字符串了，失去了占位符的作用了
        // System.out.println(actorlmp.updateActor(sql,"小红","女","2020-10-1","985"));

        // update 修改数据
        String sql2 = "update actor set phone = ? where id = ? ";
       // System.out.println(actorlmp.updateActor(sql2,"211",2));

        // delect 删除数据
        String sql3 = "delete from actor where id = ?";   // 占位符不要加单引号
      //  System.out.println(actorlmp.updateActor(sql3,5));


        String sql4 = "select max(birth) from actor";
        Date date = actorlmp.maxBirth(sql4);
       // System.out.println(date);

        // 测试特殊查询
        String sql5 = "select count(*) from actor";
      //  System.out.println(actorlmp.countActor(sql5));   // 可变参数是可以不传参数的,但是不要传null,防止null引用报错


        // 测试getArrayHandler 一条记录的查询:
        String sql6 = "select id,name,sex,birth,phone from actor where id = ?";
        Object[] actor= actorlmp.getArrayHandler(sql6, 3);
        // System.out.println(Arrays.toString(actor));

        // 测试 getArrayListHandler 查询多条记录
        String sql7 = "select id,name,birth,phone from actor where id < ?";
        List arrayListHandler = actorlmp.getArrayListHandler(sql7, 5);
        arrayListHandler.forEach(System.out::println);


        // 测试 getBeanHandler 查询一条记录
        Actor beanHandler = actorlmp.getBeanHandler(sql6, 3);
        // System.out.println(beanHandler);

        // 测试 getBeanListHandler 查询多条记录
        List beanListHandler = actorlmp.getBeanListHandler(sql7, 5);
        // beanListHandler.forEach(System.out::println);

        // 测试 getMapHandlers 查询一条记录
        Map<String, Object> mapHandlers = actorlmp.getMapHandlers(sql6, 3);
        // System.out.println(mapHandlers);


        // getMapListHandlers 查询多条记录
        List mapListHandlers = actorlmp.getMapListHandlers(sql7, 5);
        // mapListHandlers.forEach(System.out::println);



    }
}
