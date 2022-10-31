package day03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Exercise {

    /**
     * 通过输入学生的考号，完成学生信息的删除功能
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Exercise exercise = new Exercise(); // 实例化对象,静态方法调用非静态方法;
        System.out.println("请输入你要删除的学生的考号");

        String examCard = scanner.next();
        String sql = "delete from examstudent where ExamCard = ? "; // ? 占位符不要加'单引号'不然就变成了字符串,失效了
        // 在Java当中的sql语句是不要加;分号,不然报错
        boolean sign = exercise.updateExercise(sql, examCard);

        if(sign) {
            System.out.println("删除成功!!!");
        } else {
            System.out.println("查无此人,请重新启动程序");
        }



    }




    /**
     * 根据身份证号或者准考证号查询学生信息
     * @param args
     */
    public static void main3(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SelectJdbc selectJdbc =  new SelectJdbc(); // 实例化对象,静态方法调用非静态方法
        System.out.println("请选择你输入的类型:");
        System.out.println("a.准考证号 / b.身份证号:");
        String selection = scanner.next();

        if("a".equalsIgnoreCase(selection)) { // equalsIgnoreCase 比较字符串是否相等,忽略大小写
            // 注意这里我们将需要判断比较的正确内容放到左边,目的是为了防止右边输入/返回的是null,就会导致null引用的报错,
            System.out.println("请输入准考证号: ");
            String examCard = scanner.next();
            String sql = "select FlowID flowId,Type type, IDCard idCard, ExamCard examCard, StudentName studentName," +
                    "Location location, Grade grade from examstudent where ExamCard = ? ";
            // ? 占位符,不要加'单引号'不然旧变成了字符串了失效了,Java当中sql语句不要加;分号,不然报错
            Student student = selectJdbc.getInstance(Student.class,sql,examCard);
            if(student != null) {
                System.out.println(student);
            } else {
                System.out.println("查无此人,请重新启动程序");
            }
        } else if("b".equalsIgnoreCase(selection)){
            System.out.println("请输入身份证号:");
            String idCard = scanner.next();
            String sql = "select FlowID flowId,Type type, IDCard idCard, ExamCard examCard, StudentName studentName," +
                    "Location location, Grade grade from examstudent where IDCard = ? ";
            Student student = selectJdbc.getInstance(Student.class,sql,idCard);

            if( student != null) {
                System.out.println(student);
            } else {
                System.out.println("查无此人,请重新启动程序");
            }

        } else {
            System.out.println("您的输入有误，请重新输入");
        }
    }


    /**
     *为表examstudent中插入一个新的student 信息
     * @param args
     */
    public static void main2(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("四级/六级: ");
        int type = scanner.nextInt();
        System.out.print("身份证号:");
        String idCard = scanner.next();
        System.out.print("准考证号: ");
        String examCard = scanner.next();
        System.out.print("学生姓名: ");
        String studentName = scanner.next();
        System.out.print("所在城市:");
        String location = scanner.next();
        System.out.print("考试成绩: ");
        int grade = scanner.nextInt();

        // jdbc 代码：插入数据
        Exercise exercise = new Exercise(); // 实例化对象,静态方法调用非静态方法
        String sql = "insert into examstudent(type,idCard,ExamCard,StudentName,location,grade) values(?,?,?,?,?,?)";
        exercise.update(sql,type,idCard,examCard,studentName,location,grade);
    }


    /**
     *从控制台向数据库的表customers中插入一条数据
     */
    public static void main1(String[] args){
        JdbcUtils jdbcUtils = new JdbcUtils(); // 实例化对象,静态方法调用非静态方法
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名: ");
        String name = scanner.nextLine(); // next 不可以读取到空格,nextLine()可以读取到空格
        System.out.print("请输入邮箱: ");
        String email = scanner.nextLine();
        System.out.print("请输入生日: ");
        String birth = scanner.nextLine();


        // jdbc 代码
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // 1.注册驱动,2. 连接数据库
        connection = JdbcUtils.getConnection();

        // 3. 获取操作数据库的对象,4.执行sql语句
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        jdbcUtils.update(sql,name,email,birth);
        // 5. 处理select 查询的结果集
        // 6. 关闭资源
        JdbcUtils.closeResource(connection,preparedStatement);
    }


    /**
     * updat的通用封装操作
     */
    public void update(String sql,Object...args){ // object...args(可变参数,位置位于最后一位(才能识别),只有一个,可以不传参数(不建议传null引用类型
        // null引用报错,旧数组Object 因为我们不知道所传的参数的类型的 ))
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域的范围,关闭资源

        try {
            // 1.注册驱动,2 .连接数据库
            connection = JdbcUtils.getConnection();

            // 3. 获取操作数据库的对象(Connection.PreparedStatement预编译对象)
            preparedStatement = connection.prepareStatement(sql);  // 只是预编译sql语句,并不会执行sql语句

            // 4. 执行sql语句
            // 4.1 填充占位符
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符的起始位置下标是 1,可变参数(旧数组) 起始位置从 0 下标开始
            }

            // 5. 执行sql语句
            int count = preparedStatement.executeUpdate(); // 执行sql语句,注意是没有参数的,上面我们编译过了

            System.out.println(count > 0 ? "成功" : "失败");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 6.处理select 的结果集

        finally {
            // 7. 关闭资源
            JdbcUtils.closeResource(connection,preparedStatement);
        }

    }



    /**
     * updat的通用封装操作
     */
    public boolean updateExercise(String sql,Object...args){ // object...args(可变参数,位置位于最后一位(才能识别),只有一个,可以不传参数
        // (不建议传null引用类型
        // null引用报错,旧数组Object 因为我们不知道所传的参数的类型的 ))
        Connection connection = null;
        PreparedStatement preparedStatement = null; // 扩大作用域的范围,关闭资源
        boolean sign = false;

        try {
            // 1.注册驱动,2 .连接数据库
            connection = JdbcUtils.getConnection();

            // 3. 获取操作数据库的对象(Connection.PreparedStatement预编译对象)
            preparedStatement = connection.prepareStatement(sql);  // 只是预编译sql语句,并不会执行sql语句

            // 4. 执行sql语句
            // 4.1 填充占位符
            for(int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
                // 占位符的起始位置下标是 1,可变参数(旧数组) 起始位置从 0 下标开始
            }

            // 5. 执行sql语句
            int count = preparedStatement.executeUpdate(); // 执行sql语句,注意是没有参数的,上面我们编译过了

            sign = count > 0; // 当count > 0 直接就是返回 true, 否则返回 false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 6.处理select 的结果集

        finally {
            // 7. 关闭资源
            JdbcUtils.closeResource(connection,preparedStatement);
        }

        return sign;

    }

}


/*

public boolean equalsIgnoreCase(String anotherString)将此String与其他String比较，
忽略案例注意事项。 如果两个字符串的长度相同，并且两个字符串中的相应字符等于忽略大小写，则两个字符串被认为是相等的。

public boolean equals(Object anObject)将此字符串与指定对象进行比较。
 其结果是true当且仅当该参数不是null并且是String对象，表示相同的字符序列作为该对象。
 */