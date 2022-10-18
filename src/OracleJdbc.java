public class OracleJdbc implements JbdcInterface {

    // 实现接口 JbdcInterface 中的抽象方法,
    // 注意: 接口中的抽象方法必须实现,不然会报错的
    @Override
    public Object  getConnection(){
        System.out.println("得到连接上 Oracle 的数据库");
        return null;
    }

    @Override
    public void crud(){
        System.out.println("对 Oracle 的数据库中进行增删改查");
    }

    @Override
    public void close(){
        System.out.println("关闭 Oracle 中的连接库");
    }
}
