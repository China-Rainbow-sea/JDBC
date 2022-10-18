public class MysqlJdbc implements JbdcInterface {

    // 实现JdbcInterface 接口的抽象方法,继承接口的抽象方法
    // 必须实现该抽象方法,不然报错的
    @Override
    public Object getConnection(){
        System.out.println("得到 mysql 的连接");
        return null;
    }

    @Override
    public void crud() {
        System.out.println("Java 对 mysql 的增删改查");
    }

    @Override
    public void close() {
        System.out.println("关闭 mysql 连接的资源");
    }

    // 注意: 需要关闭对应的 连接资源,如果不关闭所连接的资源的话,
    // 到连接的资源的数量到达一定的量的时候,我们想要再连接上资源可能就连接不上了
}
