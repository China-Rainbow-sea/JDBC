package day03;

public class TestCustomers {
    public static void main(String[] args){
        TestCustomers testCustomers = new TestCustomers(); // 静态方法调用非静态方法,实例化对象
        testCustomers.selectCustomers();
        testCustomers.selectCustomers2();
        testCustomers.selectCustomer3();

    }


    /**
     *     测试Customers的查询(select)处理结果集
     */
    public void selectCustomers(){
        String sql = "select name,id from customers where id = ?";
        //  ? 占位符不要加'单引号',不然会被当作是字符串处理了,失去效果了,
        // 对于占位符的填充,从下标 1开始,基本上jdbc的访问都是从 1下标开始的,
        // 在Java当中的sql语句不要加;(分号),不然报错
        QueryForCustomers queryforCustomers = new QueryForCustomers();// 实例化对象,调用构造器
        Customers customers = queryforCustomers.queryForCustomers(sql,"13");
        System.out.println(customers);
    }


    /**
     *测试Customers的查询(select)处理结果集
     */
    public void selectCustomers2() {
        String sql = "select name email from customers where name = ?";
        QueryForCustomers queryForCustomers = new QueryForCustomers();
        Customers customers = queryForCustomers.queryForCustomers(sql,"周杰伦");
        System.out.println(customers);
    }


    /**
     * 测试Customers的查询(select)处理结果集
     */
    public void selectCustomer3(){
        String sql = "select name from customers where id = ?";
        QueryForCustomers queryForCustomers = new QueryForCustomers(); // 通过实例化对象,调用非静态方法
        Customers customers = queryForCustomers.queryForCustomers(sql,"0");
        System.out.println(customers);
    }
}
