package org.example.ex2.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.example.ex2.dto.TodoDTO;
import org.example.ex2.entity.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

@Log4j2
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    //상속받는 클래스의 생성자가 있어 새로 생성자를 만들어 줌.
    public TodoSearchImpl() {
        super(TodoEntity.class);
    }

    @Override
    public Page<TodoEntity> search1(Pageable pageable){
        log.info("search1............");

        QTodoEntity todoEntity = QTodoEntity.todoEntity;


        JPQLQuery<TodoEntity> query = from(todoEntity);

        query.where(todoEntity.mno.gt(0L));

        getQuerydsl().applyPagination(pageable, query);

        java.util.List<TodoEntity> result = query.fetch();

        long total = query.fetchCount();

        return null;
    }

    @Override
    public Page<TodoDTO> searchDTO(Pageable pageable){
        QTodoEntity todoEntity = QTodoEntity.todoEntity;

        JPQLQuery<TodoDTO> query = from(todoEntity);

        query.where(todoEntity.mno.gt(0L));
        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<TodoDTO> dtoQuery = query.select(Projections.constructor(TodoDTO.class,todoEntity));

        //bean 방식
//        JPQLQuery<TodoDTO> dtoDTOQuery = query.select(
//                Projections.bean(TodoDTO.class,
//                        todoEntity.mno,
//                        todoEntity.title,
//                        todoEntity.writer,
//                        todoEntity.dueDate));

        java.util.List<TodoDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList,pageable,count);
    }
}
