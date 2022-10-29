package day03;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestJdbcUtils {
    public static void main(String[] args){
        TestJdbcUtils testJdbcUtils = new TestJdbcUtils(); // 静态方法,调用非静态方法,实例化对象
//        testJdbcUtils.update();  // update 修改数据库数据测试
//        testJdbcUtils.insert(); // 插入数据测试
        testJdbcUtils.delete();  // 删除测试

    }

    /**
     * update 数据库 test
     */
    public void update(){
        // 1.注册驱动
        // 2. 连接数据库
        // 3. 获取操作数据库的对象(preparedStatement.executeUpdate的预编译对象)
        // 4. 执行sql语句
        // 5.处理结果集
        // 6. 关闭资源
        JdbcUtils jdbcUtils = new JdbcUtils();

        String sql = "update customers set birth = ? where id = ?";
        // ？ 占位符不要加 '单引号',不然失效了,在Java当中sql语句不要加;分号不然报错
        jdbcUtils.update(sql,"200-1-01",13);

    }

    /**
     * insert 插入数据 数据库 test
     */
    public void insert(){
        // 1. 注册驱动
        // 2. 连接驱动上的数据库
        // 3. 获取操作数据库的对象(预编译对象 preparedStatement.executeUpdate())
        // 4. 执行sql语句
        // 5. 处理select 的查询结果集
        // 6. 关闭资源
        JdbcUtils jdbcUtils = new JdbcUtils(); // 实例化对象,静态方法调用非静态方法
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        // ? 占位符从下标 1 开始,基本所有的jdbc都是从1下标开始的, ? 占位符不要加'单双引号',不然变成字符串了失效了,
        // 在Java当中的sql语句不可以加;分号,不然报错
        jdbcUtils.update(sql,"霍雨浩","123@126.com","2003-09-30");
    }


    /**
     * delete删除 数据库信息测试
     */
    public void delete(){
        // 1. 注册驱动
        // 2. 连接数据库
        // 3. 获取操作数据库的对象(预编译的对象 preparedStatement())
        // 4. 执行sql语句
        // 5. 处理select 查询结果集
        // 6. 关闭资源
        JdbcUtils jdbcUtils = new JdbcUtils(); // 实例化对象,静态方法调用非静态方法
        String sql = "delete from customers where id = ?";
        //  ？占位符从下标 1开始,基本上jdbc所有的索引都是从 1 下标开始的,?占位符不要加'单引号'不然就是字符串了,失效了
        //  Java当中的sql语句是不要加;(分号的)不然报错
        jdbcUtils.update(sql,"20");

    }
}

