package HelloShop.shop.project_1.api;

import HelloShop.shop.project_1.domain.member.Member;
import HelloShop.shop.project_1.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/api/members")
    public Result memberApi(@RequestBody @Valid CreateMemberRequest request) {

        List<? extends Member> allMember = memberService.findAllMember();
        List<MemberDto> collect = allMember.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

//    @PostMapping("/api/members")
//    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request
//                    ,Class<? extends Member> entityClass) {
//
//        if (entityClass.getSimpleName().equals("Student")) {
//            Student student = new Student();
//            student.setName(request.getName());
//            memberService.join(student);
//            return new CreateMemberResponse(student.getLoginId());
//        }
//    }

    @PostMapping("/api/members/{id}")

    public UpdateMemberResponse updateMember(
            Class<?> entityClass,
            @PathVariable("id") String id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        memberService.updateMemberName(entityClass, id, request.getName());
        Optional<?> findId = memberService.findId(entityClass, id);
        Object member = findId.get();
        Member findMember = (Member) member;
        return new UpdateMemberResponse(findMember.getLoginId(), findMember.getName());

    }


    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private String loginId;
        private String name;
    }

    @Data
    public class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T date;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private String loginId;

        public CreateMemberResponse(String loginId) {
            this.loginId = loginId;
        }
    }


}
