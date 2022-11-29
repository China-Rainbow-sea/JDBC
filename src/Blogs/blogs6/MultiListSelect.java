package Blogs.blogs6;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MultiListSelect {

    /*
    方案二: 创建一个新的javaBean 包含所有的 select多表查询的字段
     */
    public static void main(String[] args) {
        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties"));
            Properties properties = new Properties();
            properties.load(is);

            // 创建 druid 数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 获取到数据库当中的连接
            Connection connection = dataSource.getConnection();


            QueryRunner queryRunner = new QueryRunner();
            // 存储方式: BeanListHandler 将查询的结果集存储到javaBean当中，再将所有的javaBean对象存储到list链表当中
            BeanListHandler<EmpAndDept> beanListHandler = new BeanListHandler<>(EmpAndDept.class);

            // 多表连接查询SQL语句
            String sql = "select dept_id as deptId,dept_name as deptName,employee.emp_id as empId, " +
                    "emp_name as empName,email,gender  " +
                    "from department " +
                    "join employee " +
                    "on employee.emp_id = department.emp_id";

            List<EmpAndDept> query = queryRunner.query(connection, sql, beanListHandler);

            for(EmpAndDept empAndDept : query) {
                System.out.println(empAndDept);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }

    }




    /*
    方案一：一个JavaBean中包含另外一个 select 多表查询的表的JavaBean对象
    不可以使用 BeanListHandler 存储形式，导致 null 空指针的出现
     */
    public static void main1(String[] args) {
        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties"));
            Properties properties = new Properties();
            properties.load(is);

            // 创建 druid 数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 获取到数据库当中的连接
            Connection connection = dataSource.getConnection();


            QueryRunner queryRunner = new QueryRunner();
            // 存储方式: BeanListHandler 将查询的结果集存储到javaBean当中，再将所有的javaBean对象存储到list链表当中
            BeanListHandler<DeptAndEmp> beanListHandler = new BeanListHandler<DeptAndEmp>(DeptAndEmp.class);

            // 多表连接查询SQL语句
        String sql = "select dept_id as deptId,dept_name as deptName,employee.emp_id as empId, " +
                     "emp_name as empName,email,gender  " +
                     "from department " +
                     "join employee " +
                     "on employee.emp_id = department.emp_id";

            List<DeptAndEmp> deptAndEmpList = queryRunner.query(connection, sql, beanListHandler);

            for(DeptAndEmp deptAndEmp : deptAndEmpList) {
                System.out.println(deptAndEmp);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }





    /*
    方案二: 使用 MapListHandler 解决使用 BeanListHandler出现空指针异常的现象。
     */
    public static void main2(String[] args) {
        try {
            FileInputStream is = new FileInputStream(new File("src/druid.properties"));
            Properties properties = new Properties();
            properties.load(is);

            // 创建 druid 数据库连接池
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

            // 获取到数据库当中的连接
            Connection connection = dataSource.getConnection();


            QueryRunner queryRunner = new QueryRunner();
            // 存储方式: BeanListHandler 将查询的结果集存储到javaBean当中，再将所有的javaBean对象存储到list链表当中
            BeanListHandler<DeptAndEmp> beanListHandler = new BeanListHandler<DeptAndEmp>(DeptAndEmp.class);

            // 多表连接查询SQL语句
            String sql = "select dept_id as deptId,dept_name as deptName,employee.emp_id as empId, " +
                    "emp_name as empName,email,gender  " +  // 注意最后的空格，分隔
                    "from department " +
                    "join employee " +
                    "on employee.emp_id = department.emp_id";

            // 存储形式为 ：  MapListHandler
            MapListHandler mapListHandler = new MapListHandler();
            List<Map<String, Object>> query = queryRunner.query(connection, sql, mapListHandler);  // 执行sql语句
            List<DeptAndEmp> list = new ArrayList<DeptAndEmp>();  // 定义集合链表存储 DeptAndEmp 多对象值

            for(Map<String,Object> map : query) {

                Department department = new Department();  // 定义存储对象
                DeptAndEmp deptAndEmp = new DeptAndEmp();

                // Department: 调用 set方法赋值
                department.setDeptId((int)map.get("dept_id"));
                department.setDeptName((String)map.get("dept_name"));

                // DeptAndEmp: 调用 set方法赋值
                deptAndEmp.setDept(department);
                deptAndEmp.setEmpId((int)map.get("emp_id"));   // 注意是查询中的字段名，使用别名没有用
                deptAndEmp.setEmail((String)map.get("email"));
                deptAndEmp.setEmpName((String) map.get("emp_name"));
                deptAndEmp.setGender((String) map.get("gender"));

                // 最后封装到链表当中
                list.add(deptAndEmp);
            }

            for(DeptAndEmp deptAndEmp : list) {
                System.out.println(deptAndEmp);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);  // 将编译异常转换为运行异常抛出
        }


    }

}
