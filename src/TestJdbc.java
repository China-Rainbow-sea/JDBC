import javax.sql.rowset.JdbcRowSet;

public class TestJdbc {
    public static void main(String[] args){
        JbdcInterface mysqlJdbc = new MysqlJdbc(); // 多态,动态判定
        // 通过接口,调用实现类的方法,动态绑定,方法的重写,调用重写的方法

        System.out.println("============mysql 连接的的操作==========");
        mysqlJdbc.getConnection();  // 对应多态中的方法
        mysqlJdbc.crud();
        mysqlJdbc.close();

        System.out.println("=================oracle 数据库的Java调用=============");

        JbdcInterface oracleJdbc = new OracleJdbc();  // 多态中的动态绑定
        // 通过接口,调用实现的类中的方法,调用在对应多态类中重写的方法
        oracleJdbc.getConnection();
        oracleJdbc.crud();
        oracleJdbc.crud();


    }
}
