package HelloShop.shop.project_1.service;

import HelloShop.shop.project_1.domain.member.Member;
import HelloShop.shop.project_1.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    public <T extends Member> T login(Class<T> entityClass, String loginId, String password) {
        return memberRepository.findByLoginId(entityClass, loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    public boolean existId(Class<?> entityClass, String loginId) {
        Optional<?> findId = memberRepository.findByLoginId(entityClass, loginId);
        return findId.isEmpty();
    }

}
