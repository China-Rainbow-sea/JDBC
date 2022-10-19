import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConnection {
    /*
    运行连接数据库,执行 sql语句
     */

    /**
     * 连接数据库进行插入数据操作
     */
    public static void main1(String[] args) throws ClassNotFoundException, SQLException {
        // 1. 注册驱动(说明连接的什么品牌的数据库)
        /*
        这里通过，反射加载类,运行com.mysql.jdbc.Driver package包下的类的静态
        代码块,java.sql.DriverManager.registerDriver(new Driver());注册驱动 注意其中的参数是 Driver 接口
        默认Driver 是 java.sql 下的interface 接口，接口引用实现类,多态(动态绑定)
         */
        Class.forName("com.mysql.jdbc.Driver");

        String url = "jdbc:mysql://localhost:3306/dbtest7?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "MySQL123";

        // 2. 连接(注册驱动)时的数据库
        Connection connection = DriverManager.getConnection(url,user,password);
//        System.out.println(connection);

        // 3.创建操作数据库的对象
        Statement statement = connection.createStatement();

        // 4. 通过创建的对象执行SQL语句中的 DML，DQL
        String sql = "insert admin(id,user_name,pwd) value(3,'王五','12366')";
        // 注意在Java中执行sql语句不要加分号,不然可能报错;
        int count = statement.executeUpdate(sql);  // 返回影响行数的个数
        System.out.println(count == 1 ? "插入成功" : "插入失败");
        // 如果个数不是 1 (我们只是插入了一行的数据),表示插入失败,

        // 5. 如果第4步是 select 查询结果集,则我们需要处理结果集,这里省略
        // 6. 关闭资源,从小到大,一般放在finally 中一定会被执行(无论你是否异常);
        statement.close();  // 关闭创建操作数据库的对象的连接
        connection.close();  // 关闭连接数据库的连接


    }


    /**
     * 连接数据库执行 更新数据的操作
     */
    public static void main2(String[] args) throws ClassNotFoundException, SQLException {
        // 1. 注册驱动(说明你要连接的是什么品牌的数据库)
        // 使用反射加载其com.mysql.jdbc.Driver package 包下的类,并执行其中注册驱动的静态代码块：
        // java.sql.DriverManager.registerDriver(new Driver()) 参数是Driver 接口是在java.sql package 包下的接口

        Class.forName("com.mysql.jdbc.Driver");

        // 2. 连接(注册驱动)时的数据库
        String url = "jdbc:mysql://localhost:3306/dbtest7?useUnicode=true&characterEncoding=utf8";
        String user = "root";
        String password = "MySQL123";

        Connection connection = DriverManager.getConnection(url,user,password);

        // 3. 获取操作数据库的对象
        Statement statement = connection.createStatement();

        // 4. 通过获取到的对象执行 SQL 语句
        String sql = "update admin set user_name = '老李' where id = 3";
        // java 中执行sql语句不要加分号,不然报错
        int count = statement.executeUpdate(sql); // 返回影响数据库的行数

        System.out.println(count == 1 ? "更新成功" : "更新失败");

        // 5. 如果步骤 4 是 select 查询语句,返回的是结果集,需要我们处理,这里不是
        // 6. 关闭资源,从小到大,一般写在finally中一定会被执行(无论是否异常)
        statement.close();  // 关闭操作数据库的连接
        connection.close(); // 关闭连接的数据库

    }

    /**
     * 连接数据库执行删除操作
     */

    public static void main3(String[] args) throws ClassNotFoundException, SQLException {
        // 1.注册驱动(说明你要连接的是什么品牌的数据库),使用反射加载类,执行com.mysql.jdbc.Driver package 包下
        // 的类,中的静态代码块,:注册驱动:java.sql.DriverManager.registerDriver(new Driver());
        // 其中的参数是接口interface Driver,默认 Driver 是 java.sql包下的接口

        Class.forName("com.mysql.jdbc.Driver"); // 我们只是想要执行里面的静态代码块,注册驱动,所以不需要接受返回值

        String url = "jdbc:mysql://localhost:3306/dbtest7?useUnicode=true&characterEncoding=utf8";// 数据库在网络上所在的位置
        // url 统一资源定位符
        String user = "root"; //  用户名
        String password = "MySQL123";  // 密码

        // 2. 连接(注册驱动)上的数据库
        // Connection 是接口 interface
        Connection connection = DriverManager.getConnection(url,user,password);

        // 3. 获取操作数据库的对象,Interface Statement 是接口
        Statement statement = connection.createStatement();


        String sql = "delete from admin where id = 3"; // Java中执行sql语句不要加分号,不然报错
        // 4. 通过对象执行SQL语句
        int count = statement.executeUpdate(sql); // 返回影响数据库的行数
        System.out.println(count == 1 ? "删除成功" : "删除失败"); // 我们只是删除了一行,所以只是改变一行

        // 5. 如果第四步是 select 查询结果集,我们就需要处理结果集,这里不是
        // 6. 关闭资源,从小到大,一般放在 finally 中,一定会被执行(无论是否存在异常)
        statement.close();  // 关闭操作数据库的连接
        connection.close(); // 关闭建立的数据库连接


    }


    /**
     * 另外一种处理异常，连接数据库,执行sql语句的方式
     */

    public static void main(String[] args){
        // 1. 注册驱动(说明你要连接的是什么品牌的数据库),利用反射加载类,调用 com.mysql.jdbc.Driver package 包下的类
        //中的静态代码块是加载类: java.sql.DriverManager.registerDriver(new Driver()) 默认Driver 是在java.sql interface 的接口
        // 静态代码块中的参数 Driver()是接口,接口不可以 new 多态,接口引用实现类
        // url 统一资源定位符
        String url = "jdbc:mysql://localhost:3306/dbtest7?useUnicode=true&characterEncoding=utf8"; // 数据库在网络上的地址
        String user = "root";  // 用户名
        String password = "MySQL123";  // 密码

        Connection connection = null; // 连接数据库的接口,在这里先定义好类型,提高作用域的范围,用于在finally中关闭
        Statement statement = null;  // 获取操作数据库的接口


        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver");  // 反射加载类,只是加载类,执行其中的静态代码块,不需要接受返回值

            // 2. 连接上(注册驱动)上的数据库
            connection = DriverManager.getConnection(url,user,password); //

            // 3. 获取操作数据库的对象
            statement = connection.createStatement();

            String sql = "update admin set user_name = '张三' where id = 1";
            // 4. 通过对象执行sql语句,
             int count = statement.executeUpdate(sql); // 返回影响数据库的行数
             System.out.println(count == 1 ? "更新成功" : "更新失败");

             // 5. 如果步骤 4 执行的是 select 返回的结果集,处理结果集,这里不是

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException z){
            z.printStackTrace();
        } finally {
            // 6. 关闭资源,从小到大,一般放在 finally中一定会被执行(无论是否异常)
            if(statement != null){
                try{
                    statement.close(); // 关闭创建操作数据库的连接
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if( connection != null){
                try{
                    connection.close(); // 关闭创建连接的数据库
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }



}



/*
Statement createStatement()
                   throws SQLException
                   创建一个Statement对象，用于将SQL语句发送到数据库
 */


/*
int executeUpdate(String sql)
           throws SQLException
           执行给定的SQL语句，这可能是INSERT ，
           UPDATE ，或DELETE语句，或者不返回任何内容，如SQL DDL语句的SQL语句。
 */