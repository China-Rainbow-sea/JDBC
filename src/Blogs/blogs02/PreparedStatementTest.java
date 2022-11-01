package Blogs.blogs02;

import java.sql.*;
import java.util.Scanner;

public class PreparedStatementTest {


    /**
     * 使用PreparedStatement  进行select 查询结果集的操作
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("用户名user: ");
        String user = scanner.nextLine();   // next无法读取到空格,nextLine可以读取到空格
        System.out.print("密码password: ");
        String password = scanner.nextLine();


        // jdbc 代码
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null; // 扩大作用域,用于关闭/释放资源


        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行其中com.mysql.jdbc.Driver类的静态代码块

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库对象(connection.prepareStatement( )预编译对象)
            String sql = "select `user`,password from user_table where `user` = ? and password = ?"; // ？占位符不要加单引号,
            // 不然就是字符串了失效了，Java当中的sql语句的结束是不要加;分号的不然报错
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并没有执行sql语句

            // 4. 执行sql语句
            // 填充占位符
            preparedStatement.setString(1,user); // 填充第一个占位符,起始下标位置是 1
            preparedStatement.setString(2,password); // 填充第二个占位符

            resultSet = preparedStatement.executeQuery(); // 注意是无参数的,因为上面我们以及编译

            // 5. 处理select 查询显示的结果集
            if(resultSet.next()) { // 指向当前的select 查询的结果集的一行记录,并判断当前行是否存在数据,有返回true,没有返回false,每次调用都会自动向下移动
                System.out.println("登入成功");
                String users = resultSet.getString(1); // 获取第一列的查询的数据,起始下标位置是 1,无论数据表中读取到是什么类型的数据都以字符串的形式返回并赋值
                String passwords = resultSet.getString("password"); // 也可以通过select 查询显示的列名(字段名)获取到其中对应的数值内容
                System.out.println(users+"->"+passwords);
            } else {
                System.out.println("用户不存在/用户名/密码错误");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 6.关闭/释放资源,从小到大,最后使用的最先关闭
        if(resultSet != null) { // 防止null引用,!=null 连接/使用了的资源需要关闭,==null没有连接/使用的资源不用关闭
            try {
                resultSet.close();  // 关闭/释放处理select结果集的资源
            } catch(SQLException e) {
                throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
            }
        }


        if(preparedStatement != null) {
            try{
                preparedStatement.close(); // 关闭/释放获取到的操作数据库的资源
            } catch(SQLException e) {
                throw new RuntimeException(e); // 将编译异常转为为运行异常抛出
            }
        }


        if( connection != null) {
            try{
                connection.close(); // 关闭/释放注册驱动的资源
            } catch(SQLException e) {
                throw new RuntimeException(e); // 将编译异常转为运行异常抛出
            }
        }
    }



    /**
     * 使用PreparedStatement 对数据表进行一个 delete 删除的操作
     * @param args
     */
    public static void main4(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入user_table中的user: ");
        String user = scanner.next();

        // jdbc代码
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于关闭/释放资源

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行com.mysql.jdbc.Driver包下的类中的静态代码块,注册驱动

            // 2. 连接驱动上的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象(获取到预编译的对象)
            // ? 表示占位符,一个?表示一个占位符,占位符不可以加'单引号'包裹,加了单引号就不是占位符了,就是一个字符串了,就失去了占位符的作用了
            // 在mysql当中sql语句要加;分号结束,在Java当中使用sql语句不用加;分号,加了会报错
            String sql = "delete from user_table where `user` = ?";
            preparedStatement = connection.prepareStatement(sql); // 注意只是预编译sql语句,并没有执行

            // 4. 执行sql语句
            // 4.1 填充占位符
            preparedStatement.setString(1,user);  // 第一个 ?占位符
            int count = preparedStatement.executeUpdate(); // 注意是没有参数的类型,因为上面我们已经编译过sql语句了

            System.out.println(count > 0 ? "删除成功" : "删除失败");

            // 5. 处理select 查询的结果集,这里没有select 不用处理
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭/释放资源,从小到大
            if(preparedStatement != null) { // !=null 连接/使用了资源的需要关闭资源,=null没有连接/释放的资源不用关闭
                try { // 同时防止null引用的报错
                    preparedStatement.close(); // 关闭/释放操作数据库的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }

            if(connection != null) {
                try {
                    connection.close(); // 关闭/释放连接数据库的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }
        }

    }


    /**
     * 使用PreparedStatement 对数据表进行一个 update 修改的操作
     * @param args
     */
    public static void main3(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入user_table中的user: ");
        String user = scanner.next();
        System.out.print("输入user_table中的balance:");
        int balance = scanner.nextInt();

        // jdbc代码
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于关闭/释放资源

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行com.mysql.jdbc.Driver包下的类中的静态代码块,注册驱动

            // 2. 连接驱动上的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象(获取到预编译的对象)
            // ? 表示占位符,一个?表示一个占位符,占位符不可以加'单引号'包裹,加了单引号就不是占位符了,就是一个字符串了,就失去了占位符的作用了
            // 在mysql当中sql语句要加;分号结束,在Java当中使用sql语句不用加;分号,加了会报错
            String sql = "update user_table set balance = ? where `user` = ?";
            preparedStatement = connection.prepareStatement(sql); // 注意只是预编译sql语句,并没有执行

            // 4. 执行sql语句
            // 4.1 填充占位符
            preparedStatement.setInt(1,balance);  // 第一个 ?占位符
            preparedStatement.setString(2,user); // 第二个 ?占位符
            int count = preparedStatement.executeUpdate(); // 注意是没有参数的类型,因为上面我们已经编译过sql语句了


            System.out.println(count > 0 ? "修改成功" : "修改失败");

            // 5. 处理select 查询的结果集,这里没有select 不用处理
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭/释放资源,从小到大
            if(preparedStatement != null) { // !=null 连接/使用了资源的需要关闭资源,=null没有连接/释放的资源不用关闭
                try { // 同时防止null引用的报错
                    preparedStatement.close(); // 关闭/释放操作数据库的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }

            if(connection != null) {
                try {
                    connection.close(); // 关闭/释放连接数据库的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }
        }

    }



    /**
     * 使用PreparedStatement 对数据表进行 insert 插入操作
     * @param args
     */
    public static void main2(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入user_table中的user: ");
        String user = scanner.next();
        System.out.print("输入user_table中的password:");
        String password = scanner.next();
        System.out.print("输入user_table中的balance:");
        int balance = scanner.nextInt();

        // jdbc 代码
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域,用于关闭/释放资源

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.jdbc.Driver"); // 通过反射加载类,执行com.mysql.jdbc.Driver包下的类中的静态代码块,注册驱动

            // 2. 连接驱动上的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true" +
                    "&characterEncoding=utf8","root","MySQL123");

            // 3. 获取操作数据库的对象(获取到预编译的对象)
            // ? 表示占位符,一个?表示一个占位符,占位符不可以加'单引号'包裹,加了单引号就不是占位符了,就是一个字符串了,就失去了占位符的作用了
            // 在mysql当中sql语句要加;分号结束,在Java当中使用sql语句不用加;分号,加了会报错
            String sql = "insert into user_table(`user`,password,balance) values(?,?,?) ";
            preparedStatement = connection.prepareStatement(sql); // 注意只是预编译sql语句,并没有执行

            // 4. 执行sql语句
            // 4.1 填充占位符
            preparedStatement.setString(1,user);  // 第一个 ?占位符
            preparedStatement.setString(2,password); // 第二个 ?占位符
            preparedStatement.setInt(3,balance); // 第三个 ? 占位符 setInt()int类型
            int count = preparedStatement.executeUpdate(); // 注意是没有参数的类型,因为上面我们已经编译过sql语句了

            System.out.println(count > 0 ? "插入成功" : "删除成功");

            // 5. 处理select 查询的结果集,这里没有select 不用处理
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭/释放资源,从小到大
            if(preparedStatement != null) { // !=null 连接/使用了资源的需要关闭资源,=null没有连接/释放的资源不用关闭
                try { // 同时防止null引用的报错
                    preparedStatement.close(); // 关闭/释放操作数据库的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }

            if(connection != null) {
                try {
                    connection.close(); // 关闭/释放连接数据库的资源
                } catch(SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转化为运行异常抛出
                }
            }
        }

    }
}
