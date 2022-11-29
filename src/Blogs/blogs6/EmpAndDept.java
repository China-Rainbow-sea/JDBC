package Blogs.blogs6;

public class EmpAndDept {
    private int deptId;
    private String deptName;
    private int empId;
    private String empName;
    private String email;
    private String gender;

    public EmpAndDept() {
        // 无参构造器，必须定义，用于 apache-dbtlis底层的调用
    }

    // 同样set/get 也是必须定义的用于 apache-dbtils底层反射赋值，取值


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

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "EmpAndDept{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", empId=" + empId +
                ", empName='" + empName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
