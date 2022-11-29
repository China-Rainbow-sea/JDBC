package Blogs.blogs6;

public class DeptAndEmp {
    private int empId;
    private String empName;
    private String email;
    private String gender;

    private Department dept;



    public DeptAndEmp() {
        // 无参构造器必须，创建用于 apache-dbutls 底层调用使用
    }


    // set/get必须创意用于 apache-dbutls底层反射赋值，取值
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

    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "DeptAndEmp{" +
                "empId=" + empId +
                ", empName='" + empName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dept=" + dept +
                '}';
    }
}
