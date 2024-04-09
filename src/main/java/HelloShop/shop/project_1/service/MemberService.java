package HelloShop.shop.project_1.service;

import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.member.Lecturer;
import HelloShop.shop.project_1.domain.member.Member;
import HelloShop.shop.project_1.domain.member.Student;
import HelloShop.shop.project_1.repository.MemberRepository;
import HelloShop.shop.project_1.repository.member.query.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service //컴포넌트스캔대상, 자동으로 빈등록
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;

    @Transactional
    public void join(Member member) {
        memberRepository.save(member);
    }


    public boolean validateDuplicateMember(Class<?> entityClass,String memberId) {
        Optional<?> findId = Optional.ofNullable(findId(entityClass, memberId));
        return findId.isEmpty();
    }

    public  <T> Optional<T> findId(Class<T> entityClass, String memberId) {
        return memberRepository.findByLoginId(entityClass, memberId);
    }


    public <T> T findByNameOne(Class<T> entityClass, String name) {
        return memberRepository.findByNameOne(entityClass, name);
    }


    public <T> T findOne(Class<T> entityClass, Long id) {
        return memberRepository.findOne(entityClass, id);
    }

    public List<?> findAll(Class<?> entityClass) {
        return memberRepository.findAll(entityClass);
    }

    @Transactional
    public void deleteMember(Class<?> entityClass,Long id) {
        memberRepository.delete(entityClass,id);
    }

    public List<? extends Member> findAllMember() {
        return memberRepository.findAllMember();
    }


    @Transactional
    public void updateMemberBook(Class<?> entityClass, String id, Book book) {
        if (entityClass == Student.class) {

            Optional<Student> findId = memberRepository.findByLoginId(Student.class, id);
            Student student = findId.get();
            student.getBooks().add(book);

        } else {
            Optional<Lecturer> findId = memberRepository.findByLoginId(Lecturer.class, id);
            Lecturer lecturer = findId.get();
            lecturer.getBooks().add(book);
        }
    }
    @Transactional
    public void updateMemberName(Class<?> entityClass, String loginId,String name) {
        Optional<? extends Member> findId = memberRepository.findByLoginIdV2(entityClass, loginId);
        Member member = findId.get();
        member.setName(name);
    }

    public List<?> findAllDto(Class<? extends Member> entityClass) {
        return memberQueryRepository.findAllMember(entityClass);
    }
}
