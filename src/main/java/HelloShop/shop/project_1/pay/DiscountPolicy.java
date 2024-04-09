package HelloShop.shop.project_1.pay;


import HelloShop.shop.project_1.domain.member.Student;

public interface DiscountPolicy {
    public int DiscountPrice(int price, Student student);
}
