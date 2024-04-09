package HelloShop.shop.project_1.service;

import HelloShop.shop.project_1.controller.form.BookForm;
import HelloShop.shop.project_1.controller.form.LectureForm;
import HelloShop.shop.project_1.domain.item.Book;
import HelloShop.shop.project_1.domain.item.Item;
import HelloShop.shop.project_1.domain.item.Lecture;
import HelloShop.shop.project_1.domain.member.Member;
import HelloShop.shop.project_1.dto.BookDTO;
import HelloShop.shop.project_1.dto.LectureDTO;
import HelloShop.shop.project_1.repository.ItemRepository;
import HelloShop.shop.project_1.repository.item.query.ItemQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemQueryRepository itemQueryRepository;

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void delete(Item item){
        itemRepository.delete(item);
    }


    public <T> List<T> findAll(Class<T> entityClass) {
        return itemRepository.findAll(entityClass);
    }

    public Lecture findByIdLecture(Long id) {
        return itemRepository.findOne(Lecture.class, id);
    }

    public Book findByIdBook(Long id) {
        return itemRepository.findOne(Book.class, id);
    }

    public <T>T findByName(Class<T> entityClass,String name) {
        return itemRepository.findByName(entityClass,name);
    }


    public List<BookDTO> findAllBookDto() {
        return itemQueryRepository.findAllByDto_optimization_Book();
    }

    public List<LectureDTO> findAllLectureDto() {
        return itemQueryRepository.findAllByDto_optimization_Lecture();
    }

    public List<BookDTO> findOneBookDto(Class<? extends Member> entityClass, String id) {
        return itemQueryRepository.findOneBookDto(entityClass,id);
    }

    public List<?> findOneItems(Class<? extends Member> memberClass,Class<? extends Item> itemClass,String id) {
        return itemRepository.findByIdLectures(memberClass, itemClass,id);
    }

    public List<LectureDTO> findOneLectureDto(Class<? extends Member> entityClass, String id) {
        return itemQueryRepository.findOneLectureDto(entityClass,id);
    }

    @Transactional
    public Lecture updateLecture(LectureForm param) {
        Lecture lecture = itemRepository.findOne(Lecture.class, param.getId());
        lecture.setName(param.getLectureName());
        lecture.setPrice(param.getPrice());
        return lecture;
    }

    @Transactional
    public Book updateBook(BookForm param) {
        Book book = itemRepository.findOne(Book.class, param.getId());
        book.setName(param.getName());
        book.setPrice(param.getPrice());
        book.setStockQuantity(param.getStockQuantity());
        return book;
    }
}
