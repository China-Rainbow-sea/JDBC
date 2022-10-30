package day03;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/*
对比 Statement 和 PreparedStatement
1. Statement  存在sql注入的问题,以及拼接的问题,PreparedStatement 解决了这个问题
2. Statement 是编译一次执行一次,PreparedStatement 是编译一次,执行多次
3. PreparedStatement 会在编译阶段做类型的安全检查,setString()
4. PreparedStatement 比Statement 要快一些,预编译的缓存机制
5 PreparedStatement 可以处理 blob(图片),Statement 不可以
综上所述，PreparedStatement 使用的比较多,只有极少数的情况下需要使用Statement
什么情况下必须使用 Statement 呢 ？？
在业务方面必须支持sql注入的时候，比如升序，降序
Statement 支持sql注入,凡是业务方面要求是需要进行sql语句拼接，必须使用Statement的sql注入
 */

/**
 * 简单的用户登入判断
 */
public class JDBCTect {
    /**
     * 对Statement 实现 desc降序,asc 升序
     * @param args
     */
    public static void main(String[] args){
        JDBCTect jdbcTect = new JDBCTect(); // 实例化对象,静态方法调用非静态的方法
        jdbcTect.SqlDescAsc();
    }


 public static void main1(String[] args) {
     JDBCTect jdbcTect = new JDBCTect(); // 实例化对象,静态方法调用非静态方法
     // 初始化界面
     Map<String,String> user = jdbcTect.initUi();
     // 验证用户名和密码
//     boolean login = jdbcTect.login(user);
     boolean login = jdbcTect.loginOptimize(user);
     // 最后输出结果
     System.out.println(login ? "登入成功" : "登入失败");
 }



    /**
     * 登入界面
     */
    public Map<String,String> initUi(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("用户名: ");
        String name = scanner.nextLine(); // next无法读取到空格,nextLine可以读取到空格
        System.out.print("密码: ");
        String password = scanner.nextLine();

        Map<String,String> user = new HashMap<>();
        user.put("name",name);
        user.put("password",password);
        /* sql 注入问题
        用户名: a
        密码: 1' or '1' = '1
        jk->567
        登入成功
        导致sql注入的根本原因是什么 ？？
        用户输入的信息含有sql语句的关键字,并且这些关键字被编译执行了,扭曲了
        sql语句的原意,进而达到了sql注入的效果.黑客常用
         */

        return user;
    }





    /**
     * 验证实现
     */
    public boolean login(Map<String,String> user) {
        // jdbc代码
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;  // 扩大作用域,用于关闭资源
        boolean loginSuccess = false; // 标记该数据是否在数据库存在

        try{
            // 1. 注册驱动(说明你要连接的是是哪个品牌的数据库)
            Class.forName("com.mysql.jdbc.Driver");

            // 2. 连接驱动的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?jdbc:mysql://localhost:3306" +
                    "/dbtest6?useUnicode=true&characterEncoding=utf8","root","MySQL123");


            // 3. 获取操作数据库的对象
            statement = connection.createStatement();

            // 4. 执行sql语句
            String name = user.get("name");     // 先从Map哈希表中将需要查询的数据取出来,
            String password = user.get("password");
            String sql = "select `name`,`password` from users where `name` = '"+name+"' and `password` = '"+password+"'";
            // 字符串的拼接的技巧"++"
            resultSet = statement.executeQuery(sql); // 执行sql语句

            // 5. 处理select查询的结果集
            if(resultSet.next()) { // 指向select查询显示的行,并判断该行是否存在数据,有返回true,没有返回false,每次的调用都会自动向下移动指针
                loginSuccess = true; // 重置标记,进入了这里表示查询到了
                String string1 = resultSet.getString(1);
                String string2 = resultSet.getString(2);// 注意获取对应列的数值内容,起始下标位置是从 1 下标开始的

                System.out.println(string1+"->"+string2);
//                return loginSuccess;
            }

        } catch(SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 6.关闭资源,从小到大
            if( resultSet != null ){ // 只有连接/使用的资源才需要关闭,为null的没有使用/连接的不用关闭
                try{
                    resultSet.close();  // 防止null引用报错
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if( statement != null) {
                try{
                    statement.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }


            if( connection != null){
                try{
                    connection.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }

        return loginSuccess;

    }




    /**
     * 解决sql注入问题的方法
     * 使用 connection.prepareStatement 代替 Statement使用预编译
     */
    public boolean loginOptimize(Map<String,String> user) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于关闭资源
        boolean loginSuccess = false; // 标记

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true&characterEncoding=utf8"
            ,"root","MySQL123");

            // 3. 获取操作数据库的对象(connection.prepareStatement()预编译的对象)
            String sql = "select name ,password from users where name = ? and password = ?";
            // ? 占位符,不要加'单引号',不然会被识别成字符串,失去效果
            // 一个占位符一个 ？ 将来接收一个值,
            preparedStatement = connection.prepareStatement(sql);
            // connection.prepareStatement()只是预编译sql语句,不会执行,会保留 ? 占位符的信息(附加上"着重号,防止被识别成关键字")
            // 这样就避免了sql注入的问题了.

            // 4. 执行sql语句
            // 4.1 填充占位符,占位符的起始下标位置是 1,开始,jdbc中所有的下标基本上都是从 1 开始的

            // 先获取到 Map哈希表中的数值
            String name = user.get("name");
            String password = user.get("password");
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,password);
            resultSet =  preparedStatement.executeQuery(); // 执行sql语句,是没有参数的,因为上面我们已经编译过了

            // 5. 处理select 查询到的结果集
            if(resultSet.next()) { // 指向当前select查询显示的结果集,并判断是否含有数据,有true,没有false,每次调用都会自动向下移动指针
                loginSuccess = true; // 查到了,返回true
                // 获取到对应列名/字段的数据
                String userName = resultSet.getString("name"); // getString表示无论从数据库获取的是什么类型的数据,都是以String的形赋值到变量中
                String userPassword = resultSet.getString("password");// 可以是下标(起始从 1 下标开始),也可以是select查询显示的列名/字段名名称
                // 同理还有 getInt,getDouble 是一样的

                System.out.println(userName+"->"+userPassword);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源，从小到大

            if(resultSet != null){ // 只有不为null的连接/使用的资源,才需要关闭,为 null 的没有使用的资源不用关闭
                try{
                    resultSet.close(); // 防止null引用报错
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }


            if(preparedStatement != null){
                try{
                    preparedStatement.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }


            if(connection != null){
                try{
                    connection.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }

        return loginSuccess;
    }



    /**
     * 使用Statement的sql注入，实现对数据的降序显示
     */
    public void SqlDescAsc() {
        Scanner scanner = new Scanner(System.in); // 实例化对象
        System.out.println("输入 desc/asc, desc 表示降序,asc表示升序");
        System.out.print("请输入: ");
        String keyWords = scanner.nextLine(); // next()无法读取空格,nextLine()可以读取到空格

        // jdbc代码
        Connection connection = null;
        Statement statement = null;
        ResultSet  resultSet = null; // 扩大作用域的范围,用于关闭资源

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射机制,加载com.mysql.jdbc.Driver的类下的静态代码块,注册驱动

            // 2.连接驱动上的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8", "root","MySQL123");

            // 3. 获取操作数据库的对象
            statement = connection.createStatement();

            // 4. 通过获取到的对象，执行sql语句
            String sql = "select `name`, `password` from users order by name "+keyWords+""; // 插入字符串的技巧: "++"
            resultSet = statement.executeQuery(sql); // 执行sql语句

            // 5. 处理select的结果集
            while(resultSet.next()) { // 表示指向当前记录(行)的指针,并判断该行是否存有数据,有true,没有false,每调用一次就会向下移动指针
                String name = resultSet.getString("name");
                String password = resultSet.getString("password"); // getString无论你从数据表中读取到的是什么类型的数据都转换为String的形式赋值
                System.out.println(name+"->"+password);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源:从小到大

            if(resultSet != null){ // !=null连接/使用了的资源才需要关闭,==null的资源不用关闭
                try{
                    resultSet.close(); // 防止null引用报错
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if( statement != null) {
                try{
                    statement.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }

            if( connection != null) {
                try{
                    connection.close();
                } catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }

    }


}
