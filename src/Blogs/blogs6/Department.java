package Blogs.blogs6;

public class Department {
    private int deptId;
    private String deptName;
    private int empId;



    public Department() {
        // 无参构造器必须，创建用于 apache-dbutls 底层调用使用
    }



    // set/get必须创意用于 apache-dbutls底层反射赋值，取值
   public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }


    @Override
    public String toString() {
        return "Department{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", empId=" + empId +
                '}';
    }
}
