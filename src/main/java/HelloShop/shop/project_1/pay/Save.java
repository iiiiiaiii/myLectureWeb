package HelloShop.shop.project_1.pay;


import HelloShop.shop.project_1.domain.member.Grade;
import HelloShop.shop.project_1.domain.member.Student;

public abstract class Save {
    protected void save(int price, Student student) {
        int pay = student.getPay();
        int plus = (int) (pay * 0.02);
        student.setPay(pay + price);
        int mileage = student.getMileage();
        student.setMileage(mileage + plus);
        gradeSet(student);
    }

    public static void gradeSet(Student student) {
        if(student.getGrade()== Grade.골드) return;
        if(student.getPay()<100000) student.setGrade(Grade.일반);
        else if(student.getPay()<500000) student.setGrade(Grade.실버);
        else student.setGrade(Grade.골드);
    }
}
