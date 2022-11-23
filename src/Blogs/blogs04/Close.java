package Blogs.blogs04;

import org.apache.commons.dbutils.DbUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Close {
    public static void close(Connection connection,Statement statement,ResultSet resultSet) {
        // 归还连接，释放资源 最晚使用的资源最先关闭，
        // 静态方法可以“类名.方法名”直接调用
        DbUtils.closeQuietly(resultSet);   // 释放处理查询结果集的资源
        DbUtils.closeQuietly(statement);   // 释放操作数据库的资源
        DbUtils.closeQuietly(connection);  // 关闭连接/归还连接(数据库连接池)
    }


    public static void closeResource(Connection connection, Statement statement, ResultSet resultSet) {
        // 归还连接，释放资源 最晚使用的资源最先关闭，
        try {
            DbUtils.close(resultSet);  // 静态方法可以“类名.方法名”直接调用// 释放处理查询结果集的资源
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

        try {
            DbUtils.close(statement);  // 释放操作数据库的资源
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

        try {
            DbUtils.close(connection); // 关闭连接/归还连接(数据库连接池)
        } catch (SQLException e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }
    }
}
