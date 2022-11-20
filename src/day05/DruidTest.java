package day05;

// Druid（德鲁伊）数据库连接池
/*
Druid是阿里巴巴开源平台上一个数据库连接池实现，
它结合了C3P0、DBCP、Proxool等DB池的优点，同时加入了日志监控，可以很好的监控DB池连接和SQL的执行情况，
可以说是针对监控而生的DB连接池，可以说是目前最好的连接池之一
 */


import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DruidTest {
    // 方式直接使用配置文件的方式:创建druid 数据库连接池
    // 导入相关的druid-1.1.10.jar 包,
    // 创建配置文件信息:以.properties 为后缀的文件名,编写相关的配置信息url
    public static void main1(String[] args) {
        Properties properties = new Properties();   // 创建读取properties配置文件信息的对象
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");  // 读取指定的配置文件
        try {
            // (properties) 的信息
            properties.load(is);

            // 通过传入读取到配置文件(druid.properties)的对象,创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);  // 注意返回类型是DataSource jdbc提供的接口

            // 通过druid数据库连接池，获取其中连接池的连接对象
            Connection connection = dataSource.getConnection();
            System.out.println(connection);  // 打印连接对象地址
            System.out.println(connection.getClass());  // 打印显示对应类的包(路径)
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转化为运行时异常抛出.
        }

    }


    // 测试druid 数据库连接池 5000次/5w/50w次连接所消耗的时间
    public static void main(String[] args) {
        Properties properties = new Properties();  // 创建获取properties 配置文件信息的对象

        try {

            FileInputStream is = new FileInputStream(new File("src/druid.properties"));  // 读取指定的文件的参数信息
            properties.load(is); // 以简单的线性格式从输入字符流读取属性列表（关键字和元素对）
            // 通过传获取到配置文件信息的对象,创建druid数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            long start = System.currentTimeMillis();  // 连接前的时间点: 单位:毫秒

            for(int i = 0; i < 5000000; i++) {
                // 从druid数据库连接池中获取到连接对象
                Connection connection = dataSource.getConnection();

                // 释放/归还连接对象(给druid数据库连接池)
                if(connection != null) {
                    connection.close();
                }
            }

            long end = System.currentTimeMillis();   // 连接完成的时间点: 单位: 毫秒

            System.out.println("druid数据库连接池连接5000/5w50w次所消耗的时间: " + (end - start)); // 5000: 935/5W: 930/50W: 993/500w: 1563
        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转化为运行异常抛出
        }

    }
}
