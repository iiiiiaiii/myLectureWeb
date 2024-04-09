package HelloShop.shop.project_1.pay;


import HelloShop.shop.project_1.domain.member.Student;

public class PolicyFix extends Save implements DiscountPolicy{

    @Override
    public int DiscountPrice(int price, Student student) {
        save(price, student);
        return price - 1000;
    }
}
