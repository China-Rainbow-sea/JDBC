package day03;

public class Student {
    private int flowId;  // 流水号
    private int type;  // 考试类型
    private int grade;   // 成绩
    private String idCard; // 身份账号
    private String examCard;  // 准考证号
    private String studentName; // 学生姓名
    private String location; // 所在城市

    public Student() {
        // 无参构造器,就算我们没有使用也需要定义,提高代码的健壮性
    }

    public Student(int flowId, int type, int grade, String idCard, String examCard, String studentName, String location) {
        this.flowId = flowId;
        this.type = type;
        this.grade = grade;
        this.idCard = idCard;
        this.examCard = examCard;
        this.studentName = studentName;
        this.location = location;
    }


    public int getFlowId() {
        return this.flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "==========查询结果======\n"+"flowId= " + flowId +
                "\ntype= " + type +
                "\ngrade= " + grade +
                "\nidCard= " + idCard +
                "\nexamCard= " + examCard +
                "\nstudentName= " + studentName +
                "\nlocation= " + location ;
    }
}
