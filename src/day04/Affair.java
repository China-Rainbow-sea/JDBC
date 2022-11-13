package day04;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 事务: 什么叫数据库事务
 * 事务: 一组逻辑操作单元，使数据从一种状态变换到另一种状态，
 * 一组逻辑操作单元,一个或多个DML操作
 * 2. 事务处理的原则: 保证所有事务都作为一个工作单位来执行,即使出现了故障，都不能改变这种执行方式，
 * 当一个事务中执行多个操作时，要么所有的事务都被提交(commit),那么这些修改就永远被保存下来了，
 * 那么数据库系统将放弃所作的所有修改，整个事务回滚(rollback) 到最初状态
 * 3. 数据一旦提交就不可回滚了
 * 4.哪些操作会导致数据的自动提交 commit
 * 1. DDL 操作一旦执行都会自动提交
 * 2. DML 默认情况下，一旦执行，就会自动提交
 * 3. 默认在关闭连接数据库前，也会自动提交
 */

/*
事务的 ACID 属性:
1.原子性: 指事务是一个不可分割的工作单位，事务中的操作，要么都发生，要么都不发生，没有中间可能(不可以一个任务只做一半)
2. 一致性: 事务必须使数据库从一个状态变换到另外一个状态，没有中间状态
3. 隔离性: 事务的隔离性是指一个事务的执行不能被其他事务干扰，即一个事务内部的操作及使用的数据
对并发的其他事务是隔离的，并发执行的各个事务之间不能互相干扰。
4. 持久性：是指一个事务一旦被提交，它对数据库中的数据的改变就是永久性的，接下来的其他操作和数据库故障不应该对其有任何影响。

数据库的并发问题：
对于同时运行多个事务，当这些事务访问数据库中相同的数据时，如果没有采取必要的隔离级别，就会导致各种并发问题：
1. 脏读：读取到了未提交的数据信息，如果回滚数据了，该读取到的数据信息就是临时的无效，无意义的，
2. 不可重复读：字段更新了，再次读取该数据时，是已经更新的数据，这个是可以接受的，如：双十一修改价格
3. 幻读：可以读取到更新插入的数据信息，这个也是可以接受的,如：双十一:突然增加了库存.

一个事务与其他事务隔离的程度称为隔离级别，数据库规定了多种事务隔离级别，不同隔离级别对应不同的干扰程度，隔离级别越高，
数据一致性就越好，但并发性就越差。
Mysql提供了四种隔离级别:
READ UNCOMITE (读未提交数据) 三个事务的并发问题都不做处理
READ COMMITE(读已提交的数据) 处理脏读问题(常用)
REDATABLE  READ(可重复读) 处理了脏读与不可重复读的并发问题。常用
SERTABLTABLE (串行化) 对三种并发问题都处理了。

Oracle 支持两种事务隔离级别：READ COMMITE(读已提交数据)，SERIABKTEABLE(串行化) ，Orsvlr默认是 READ COMMITE
MySQL 主要使用的是 READ COMMMITE(读已提交数据), REDATABLE READ(读已提交数据，可重复读数据信息)



 */
public class Affair {
    /**
     * 考虑上事务性,通过 set acutocoumnit = false 的方式取消DML操作的自动提交,
     * Connection.setAutoCommit(false);默认是true 会自动提交数据信息给数据库保存
     *  connection.commit();   // 手动提交数据信息
     *  connection.rollback(); 数据回滚，只有关闭了自动提交数据才可以实现回滚数据
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // jdbc code
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");   // 通过反射加载类的方式,执行com.mysql.cj.jdbc.Driver包下的类中的静态代码块,z
            // 注册驱动

            // 2. 连接驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6", "root", "MySQL123");

            // 取消数据的自动提交
            connection.setAutoCommit(false);    // 默认是 true 会自动提交数据
            // 3. 获取预编译sql语句的操作数据库对象
            String sqlAA = "update user_table set balance = balance - 100 where user = ?";  // ? 占位符不要加单引号不然就变成了,字符串了,
            // 失去了占位符的作用
            preparedStatement = connection.prepareStatement(sqlAA);  // 仅仅只是预编译sql语句,并没有执行sql语句
            // 填充占位符
            preparedStatement.setString(1, "AA");  // 占位符起始下标是从 1开始的
            // 4. 执行AA 的转账 sql语句
            int countAA = preparedStatement.executeUpdate();       // 返回影像数据库的行数(记录)

            // 模拟网络出现异常情况，数据传输不稳定,转账的异常
            int n = 10 / 0;   //  分母不可以为 0

            // BB 操作
            String sqlBB = "update user_table set balance = balance + 100 where user = ?";
            preparedStatement = connection.prepareStatement(sqlBB);
            preparedStatement.setString(1, "BB");
            int countBB = preparedStatement.executeUpdate();  // 执行sql语句


            // 执行到这里说明没有异常问题出现，将修改的数据信息提交给数据库存储起来
            connection.commit();   // 提交数据
            // 5. 处理select 查询显示的结果集
        } catch (ClassNotFoundException e) {
            // 出现的异常,为了保证事务性，对上述操作失败的数据，进行回滚操作
            if(connection != null) {
                try {
                    connection.rollback();  // 数据回滚
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (SQLException e) {
            if(connection != null) {  // 出现异常，回滚数据
                try {
                    connection.rollback();  // 数据回滚
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间

            if(preparedStatement != null) { // 防止null引用
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);   // 将编译异常转换为运行时异常抛出
                }
            }


            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * 演示事务问题：正对数据表 user_table 来说，进行一个转账操作的演示：
     * AA用户给BB用户转账 100 元
     * update user_table set balance = balance -100 where user='AA'
     * update user_table set balance = balance + 100 where user = 'BB'
     *
     * @param args
     */
    public static void main1(String[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // jdbc code
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");   // 通过反射加载类的方式,执行com.mysql.cj.jdbc.Driver包下的类中的静态代码块,z
            // 注册驱动

            // 2. 连接驱动中的数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbtest6", "root", "MySQL123");

            // 3. 获取预编译sql语句的操作数据库对象
            String sqlAA = "update user_table set balance = balance - 100 where user = ?";  // ? 占位符不要加单引号不然就变成了,字符串了,
            // 失去了占位符的作用
            preparedStatement = connection.prepareStatement(sqlAA);  // 仅仅只是预编译sql语句,并没有执行sql语句
            // 填充占位符
            preparedStatement.setString(1, "AA");  // 占位符起始下标是从 1开始的
            // 4. 执行AA 的转账 sql语句
            int countAA = preparedStatement.executeUpdate();       // 返回影像数据库的行数(记录)

            // 模拟网络出现异常情况，数据传输不稳定,转账的异常
            int n = 10 / 0;   //  分母不可以为 0

            // BB 操作
            String sqlBB = "update user_table set balance = balance + 100 where user = ?";
            preparedStatement = connection.prepareStatement(sqlBB);
            preparedStatement.setString(1, "BB");
            int countBB = preparedStatement.executeUpdate();  // 执行sql语句


            // 5. 处理select 查询显示的结果集
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源,最晚使用的最先释放空间

            if(preparedStatement != null) { // 防止null引用
             try {
                 preparedStatement.close();
             } catch (SQLException e) {
                 throw new RuntimeException(e);   // 将编译异常转换为运行时异常抛出
             }
            }


            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}



/*
setAutoCommit
void setAutoCommit(boolean autoCommit)
            throws SQLException将此连接的自动提交模式设置为给定状态。 如果连接处于自动提交模式，
 则其所有SQL语句将作为单个事务执行并提交。 否则，它的SQL语句被分组成通过调用方法commit或方法rollback 。
默认情况下，新连接处于自动提交模式。
当语句完成时，发生提交。 语句完成的时间取决于SQL语句的类型：
对于DML语句（如插入，更新或删除）和DDL语句，语句在执行完成后立即完成。
对于Select语句，当关联的结果集关闭时，该语句将完成。
对于CallableStatement对象或返回多个结果的语句，当所有关联的结果集都已关闭并且已检索到所有更新计数和输出参数时，该语句将完成。
注意：如果在事务中调用此方法并更改了自动提交模式，则事务将被提交。 如果调用了setAutoCommit ，
并且自动提交模式没有改变，则调用是无操作的。
参数
autoCommit - true启用自动提交模式; false禁用它
 */


/*

void commit()
     throws SQLException
使上次提交/回滚之后所做的所有更改都将永久性，并释放此Connection对象当前持有的任何数据库锁。
只有当自动提交模式被禁用时，才应该使用此方法。
 */

/*
rollback
void rollback()
       throws SQLException
撤销在当前事务中所做的所有更改，并释放此Connection对象当前持有的任何数据库锁。
只有当自动提交模式被禁用时，才应该使用此方法。
 */