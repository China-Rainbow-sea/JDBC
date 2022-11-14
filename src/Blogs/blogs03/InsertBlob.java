package Blogs.blogs03;

import java.io.*;
import java.sql.*;

public class InsertBlob {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;  // 扩大作用域,用于关闭资源

        try {
            // 1.注册数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接注册的驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 3. 获取到操作数据库的对象(预编译sql语句对象)
            String sql = "delete from customers  where id = ?"; // 占位符不要加单引号(不然就成字符串)
            preparedStatement = connection.prepareStatement(sql);  // 仅仅是编译sql语句,并没有执行sql语句

            // 填充占位符，注意占位符的填充是在预编译sql语句之后的,不然可能就存在SQL注入的问题了
            preparedStatement.setInt(1,22);
            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate(); // 返回影响数据库的行数
            System.out.println(count > 0 ? "成功" : "失败");

            // 5. 处理select 查询显示的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先关闭/释放资源
            // !=null:连接/使用了资源需要关闭资源,==null:没有连接/使用资源不需要关闭资源

            if(preparedStatement != null) { // 防止null引用
                try{
                    preparedStatement.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }


            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }
        }


    }
    public static void main3(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InputStream io = null;
        FileOutputStream fos = null;  // 扩大作用域,用于关闭资源

        try {
            // 1.注册数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接注册的驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 3. 获取到操作数据库的对象(预编译sql语句对象)
            String sql = "select id,name,email,birth,photo from customers where id = ?"; // 占位符不要加单引号(不然就成字符串)
            preparedStatement = connection.prepareStatement(sql);  // 仅仅是编译sql语句,并没有执行sql语句
            // 填充占位符
            preparedStatement.setInt(1,16);
            // 4 执行sql语句
            resultSet = preparedStatement.executeQuery();

            // 5.处理select查询显示的结果集信息

      /*  获取查询的结果集的方式一:通过对应字段的下标索引到指定的列中，起始下标是从 1 开始的
        if(resultSet.next()) {  // next:指向select查询显示的记录(行),并判断其是否含有记录,有true,并移动向下移动指针,没有false
            int id = resultSet.getInt(1);   // jdbc 起始下标是 1
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            Date birth = resultSet.getDate(4);

        }*/

            // 获取查询的结果集的信息的方式二: 通过对应字段名(别名)的方式,锁定对应字段的内容并获取到该字段的信息
            if(resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                Date birth = resultSet.getDate("birth");

                // 将大型二进制数据类型(BLOB)的信息下载下来,以文件的形式存储到本地文件当中
                Blob  photo = resultSet.getBlob("photo");

                io = photo.getBinaryStream();
                fos = new FileOutputStream("zhuyin.jpg"); // 设定存储到本地的文件名

                byte[] buffer = new byte[1024];
                int len;
                while((len = io.read(buffer)) != -1) {
                    fos.write(buffer,0,len);
                }
                System.out.println(id+"->"+name+"->"+email+"->"+birth);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放
            // !=null:连接/使用了资源需要关闭资源,==null:没有连接/使用资源不需要关闭资源
            if(fos != null) {  // 防止null引用报错
                try{
                    fos.close();
                } catch(IOException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行时异常抛出
                }
            }

            if(io !=null ) {  // 防止null引用报错
                try{
                    io.close();
                } catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if(resultSet != null) {
                try{
                    resultSet.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }


            if(preparedStatement != null) { // 防止null引用
                try{
                    preparedStatement.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }


            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }
        }

    }

    public static void main2(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;  // 扩大作用域,用于关闭资源

        try {
            // 1.注册数据库驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 连接注册的驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6","root","MySQL123");

            // 3. 获取到操作数据库的对象(预编译sql语句对象)
            String sql = "update customers set photo = ? where id = ?"; // 占位符不要加单引号(不然就成字符串)
            preparedStatement = connection.prepareStatement(sql);  // 仅仅是编译sql语句,并没有执行sql语句

            FileInputStream photo = new FileInputStream(new File("test.jpg"));
            preparedStatement.setBlob(1,photo);

            // 填充占位符，注意占位符的填充是在预编译sql语句之后的,不然可能就存在SQL注入的问题了
            // 通过I/O流导入图片
            preparedStatement.setInt(2,22);   // jdbc占位符的起始下标是 1,

            // 4. 执行sql语句
            int count = preparedStatement.executeUpdate(); // 返回影响数据库的行数
            System.out.println(count > 0 ? "成功" : "失败");

            // 5. 处理select 查询显示的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先关闭/释放资源
            // !=null:连接/使用了资源需要关闭资源,==null:没有连接/使用资源不需要关闭资源

            if(preparedStatement != null) { // 防止null引用
                try{
                    preparedStatement.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }


            if(connection != null) {
                try{
                    connection.close();
                } catch(SQLException e) {
                    throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
                }
            }
        }


    }
}
