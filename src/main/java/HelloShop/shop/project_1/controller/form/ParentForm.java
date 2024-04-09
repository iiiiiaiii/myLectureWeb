package HelloShop.shop.project_1.controller.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor

public class ParentForm extends MemberForm{
    @NotEmpty(message = "자녀 입력은 필수 입니다")
    private String childId;

}
