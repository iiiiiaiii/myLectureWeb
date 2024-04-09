package HelloShop.shop.project_1.controller.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class MemberForm {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    private int age;
    @NotEmpty(message = "아이디는 필수 입니다")
    private String loginId;
    @NotEmpty(message = "패스워드는 필수 입니다")
    private String password;
}
