package org.example.ex2.repository;

import org.example.ex2.entity.TodoEntity;
import org.example.ex2.repository.search.TodoSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<TodoEntity, Long>, TodoSearch {

    @Query("SELECT t from TodoEntity t")
    Page<TodoEntity> listAll(Pageable pageable);

    @Query("SELECT t from TodoEntity t where t.mno = :mno")
    Optional<TodoEntity> getDTO(@Param("mno") Long mno);
}
