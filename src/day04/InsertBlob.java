package day04;


import java.io.*;
import java.sql.*;

/**
 * 操作Blob 类型字段
 * 1.Mysql中,BlOB 是一个二进制的大型对象,是一个可以存储大量数据的容器,它能容纳不同大小的数据.
 * 插入BLOB类型的数据必须使用 PreparedStatement,因为BLOB 类型的数据,无法使用字符串拼接写的,而是使用I/O 流
 * MySQL 的四种类型(除了在存储的最大信息量上不同外,他们是等同的)
 * TingBlob 最大255字节
 * Blob 最大 65K
 * MediumBlob 最大16M
 * LongBlob 最大4G
 * 实际使用中根据需要存入的数据大小定义，不同的Blob类型,需要注意的是:如果存储的文件过大,数据库的性能下降
 * 如果在定义相关Blob类型以后，还报错,:xxx too large ，那么在Mysql的安装目录下,找my.ini文件加上,如下的配置
 * 参数: max_allowed_packet = 16M 同时注意:修改了my.ini 文件之后,需要重新启动mysql服务，才可以
 */
public class InsertBlob {

    /**
     * 当插入的图片大于1mb 时发生插入失败的处理:一般是在Mysql5.0 的，Mysql8.0 不会出现这种情况
     * 如果在指定了相关的Blob类型以后，还报错：xxx too large，那么在mysql的安装目录下，
     * 找my.ini文件加上如下的配置参数： **max_allowed_packet=16M**。同时注意：修改了my.ini文件之后，需要重新启动mysql服务。
     *
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2.连接驱动上的数据库,
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6", "root", "MySQL123");

            // 3. 获取操作数据库预编译的对象
            String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql); // 仅仅只是预编译sql语句,并没有执行

            // 3. 填充占位符,起始下标是从 1 开始的
            preparedStatement.setString(1, "bizhi");
            preparedStatement.setString(2, "bizhi@126.com");
            preparedStatement.setString(3, "2002-01-1");

            // 导入blob 二进制文件,通过I/O流
            FileInputStream io = new FileInputStream(new File("blob.jpg"));
            preparedStatement.setBlob(4, io);

            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate(); // 执行sql语句,返回影响数据库的对象
            System.out.println( count > 0 ? "成功" : "失败");

            // 5. 处理select 查询的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间
            if (preparedStatement != null) {  // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }


            if (connection != null) {  // 防止null引用
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }

    /**
     * 删除图片,信息
     *
     * @param args
     */
    public static void main4(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6", "root", "MySQL123");

            // 3. 获取操作数据库的对象
            String sql = "delete from customers where id = ?";  // 注意?占位符不要加单引号,不然就是字符串了,失去了占位符的作用了
            preparedStatement = connection.prepareStatement(sql);
            // 3. 填充占位符,占位符起始下标是从 1 开始的
            preparedStatement.setInt(1, 0); // 只是预编译sql语句,并没有执行sql语句的

            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate(); // 执行sql语句,返回影响数据库的行数.注意是无参的方法,因为前面已经编译过了

            System.out.println(count > 0 ? "成功" : "失败");
            // 5.处理select 查询显示的结果集,这里不是不用处理
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        } finally {
            // 6.关闭资源,最晚使用的最先释放空间
            if (preparedStatement != null) {  // 防止null引用报错
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 查询处理数据表中 customers 中 blob类型的字段
     *
     * @param args
     */
    public static void main3(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InputStream io = null;   // io 资源
        FileOutputStream fos = null;  // io 资源扩大作用域用于关闭资源

        // JDBC
        // 1. 注册驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接注册驱动的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6", "root", "MySQL123");

            // 3. 获取操作数据库预编译的对象
            String sql = "select id, name, email,birth,photo from customers where id = ?"; // 占位符不要加单引号,不然就是字符串了,失去了占位符的作用了
            preparedStatement = connection.prepareStatement(sql);

            // 3.1 通过获取到的预编译对象，填充占位符
            preparedStatement.setInt(1, 16);

            // 4.执行sql语句,这里是select 查询的结果集
            resultSet = preparedStatement.executeQuery();  // 返回一个结果集对象

            // 5. 处理select 查询显示的结果集
            // 5.1 获取select 结果集的元数据对象
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 5.2 通过元数据对象的获取到select 查询显示的 一行(记录)中所有列数的总数
            int count = resultSetMetaData.getColumnCount();     // 获取到一行显示的所有的列数总和

            if (resultSet.next()) {  // 指针指向select 查询显示的第一行数据,该行含有数据返回true,并向下移动指针,没有数据,返回false

    /*            方式一: 通过访问对应列的下标的索引获取到对应的内容的值,注意的是:起始下标位置是从 1，开始的
                for (int i = 0; i < count; i++) {
                    int id = resultSet.getInt(i+1);
                    String name = resultSet.getString(i+1);
                    String email = resultSet.getString(i+1);
                    Date birth = resultSet.getDate(i+1);

                }
                */

                // 方式二: 通过select 查询显示的别名的方式获取到,对应的别名的值,没有别名使用默认的字段名
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");

                //将Blob类型的字段下载下来,以文件的形式存储起来,保存到本地中去
                Blob photo = resultSet.getBlob("photo");  // 获取到的是 Blob 大型二进制的文件内容
                io = photo.getBinaryStream();
                fos = new FileOutputStream("zhuying.jpg");// 将图片存储到本地项目的文件中去,该文件名称为zhuying.jpg
                // 后缀名,的影响十分的大.所以不要写错了.
                byte[] buffer = new byte[1024];

                int len = 0;
                while ((len = io.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放.
            // !=nul 连接/使用了资源才需要关闭,==null没有连接/使用的资源不需要关闭
            if (fos != null) {  // 防止null引用的报错
                try {
                    fos.close(); // 关闭i/o资源
                } catch (IOException e) {
                    throw new RuntimeException(e); // 将编译异常转换为运行异常抛出
                }
            }


            if (io != null) {
                try {
                    io.close();   // 关闭IO资源
                } catch (IOException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }


            if (resultSet != null) {
                try {
                    resultSet.close();  // 关闭 select 查询返回的结果集的对象的资源连接
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


            if (preparedStatement != null) {
                try {
                    preparedStatement.close(); // 关闭获取操作数据库的连接资源
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();   // 关闭注册数据库驱动的连接资源
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    /**
     * 测试使用 PreparedStatement 操作Blob类型的数据
     *
     * @param args
     */
    public static void main2(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // 1. 注册驱动   // mysql8.0的驱动
            Class.forName("com.mysql.cj.jdbc.Driver");  // 通过反射加载驱动,执行com.mysql.jdbc.Driver包下的静态代码块,注册驱动

            // 2. 根据驱动连接对应的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6?useUnicode=true&characterEncoding=utf8"
                    , "root", "MySQL123");
            // 3. 获取操作数据库的预编译对象
            String sql = "insert into customers(name,email,birth,photo) value(?,?,?,?)"; // ?占位符不要加单引号不然就成了字符串了,占位符的作用就失效了
            // Java当中的sql语句的结尾不用加分号,不然报错的
            preparedStatement = connection.prepareStatement(sql); // 只是预编译sql语句,并没有执行sql语句
            // 3.2 填充占位符信息
            preparedStatement.setString(1, "张三"); // 占位符的填充起始下标是从 1开始的,不是 0
            preparedStatement.setString(2, "zhangsan@126.com");
            preparedStatement.setString(3, "1990-09-08");

            // 填充插入图片,使用I/O流进行一个导入图片文件大型的二进制文件内容信息
            FileInputStream io = new FileInputStream(new File("test.jpg"));
            preparedStatement.setBlob(4, io);  // 插入填充Blob 文件内容

            // 4. 执行sql语句的代码:
            int count = preparedStatement.executeUpdate();   // 返回影响数据库的行数

            System.out.println(count == 1 ? "插入成功" : "插入失败");

            // 5. 处理select 查询显示的结果集信息,这里不是 select 不用处理
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 6.关闭资源:最后使用的最先关闭
            if (preparedStatement != null) { // !=null连接/使用了资源,才需要关闭资源,==null 没有连接/使用的资源不需要关闭,
                try {   // 防止空引用
                    preparedStatement.close();  // 关闭操作数据库预编译的对象资源
                } catch (SQLException e) {
                    throw new RuntimeException(e); // 将编译异常转换为运行异常抛出;
                }
            }

            if (connection != null) {
                try {
                    connection.close();  // 关闭连接数据库的资源
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}




