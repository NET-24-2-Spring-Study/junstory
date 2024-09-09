package org.example.ex2.repositroy;

import org.example.ex2.entity.TodoEntity;
import org.example.ex2.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testInsert() {
        TodoEntity todoEntity = TodoEntity.builder()
                .title("부트 끝내기")
                .writer("user00")
                .dueDate(LocalDate.of(2025,12,23))
                .build();
        todoRepository.save(todoEntity);

        System.out.println("New TodoEntity MNO: "+ todoEntity.getMno());

    }

    @Test
    public void testInsertDummies() {
        for (int i = 0; i < 100; i++) {
            TodoEntity todoEntity = TodoEntity.builder()
                    .title("Test Todo..." + i)
                    .writer("tester"+i)
                    .dueDate(LocalDate.of(2025, 12, 23))
                    .build();
            todoRepository.save(todoEntity);

            System.out.println("New TodoEntity MNO: " + todoEntity.getMno());
        }//end for
    }

    @Test
    public void testRead() {
        Long mno = 6L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        result.ifPresent(todoEntity -> {
            System.out.println(todoEntity);
        });
    }

    //@org.springframework.transaction.annotation.Transactional
    @Test
    public void testRead2() {
        Long mno = 6L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        result.ifPresent(todoEntity -> {
            System.out.println(todoEntity);
        });
        Optional<TodoEntity> result2 = todoRepository.findById(mno);

        result2.ifPresent(todoEntity -> {
            System.out.println(todoEntity);
        });
    }

    @Test
    @Transactional
    @Commit
    public void testUpdateDirtyCheck() {
        Long mno = 6L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        TodoEntity todoEntity = result.get();

        System.out.println("OLD : " +todoEntity);

        todoEntity.changeTitle("Changed Title..."+ Math.random());
        todoEntity.changeWriter("Changed Writer..."+ Math.random());

        System.out.println("Changed : " +todoEntity);

        //without Transactional...
        //todoRepository.save(todoEntity);
    }

    @Test
    @Transactional
    @Commit
    public void testDelete() {
        Long mno = 6L;

        Optional<TodoEntity> result = todoRepository.findById(mno);

        result.ifPresent(todoEntity -> {
            todoRepository.delete(todoEntity);
        });
    }

    @Test
    @Transactional
    @Commit
    public void testDeleteById() {
        Long mno = 6L;

        todoRepository.deleteById(mno);
    }

    //===============================================
    //페이징 구현
    //===============================================

    @Test
    public void testPaging(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<TodoEntity> result = todoRepository.findAll(pageable);

        System.out.println(result.getTotalPages());
        System.out.println(result.getTotalElements());

        java.util.List<TodoEntity> todoEntities = result.getContent();

        todoEntities.forEach(System.out::println);
    }

    @Test
    public void testListAll() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<TodoEntity> result = todoRepository.listAll(pageable);
        System.out.println(result.getContent());

    }

    @Test
    public void testSearch1() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("mno").descending());

        Page<TodoEntity> result = todoRepository.search1(pageable);

        System.out.println(result.getTotalPages());
        System.out.println(result.getTotalElements());

        java.util.List<TodoEntity> todoEntities = result.getContent();
        todoEntities.forEach(System.out::println);
    }

    @Test
    public void testGetTodoDTO(){
        Long mno = 6L;

        Optional<TodoEntity> result = todoRepository.getDTO(mno);
        result.ifPresent(System.out::println);
    }

    @Test
    public void testSearchDTO(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("title").descending());
        Page<TodoEntity> result = todoRepository.search1(pageable);

        System.out.println(result.getTotalPages());
        System.out.println(result.getTotalElements());

        java.util.List<TodoEntity> dtoList = result.getContent();

        dtoList.forEach(System.out::println);


    }
}

