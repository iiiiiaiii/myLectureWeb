package HelloShop.shop.project_1.controller.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import HelloShop.shop.project_1.domain.myClass;


@Getter
@Setter
@NoArgsConstructor

public class LecturerForm extends MemberForm{
    @NotNull(message = "과목선택은 필수 입니다")
    private myClass myClass;
}
