package org.example.ex2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity //테이블에 매핑할 클래스에 붙임. JPA가 관리하게 됨. 엔티티 객체임을 명시
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_todos")
@Builder
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String writer;

    private LocalDate dueDate;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeWriter(String writer) {
        this.writer = writer;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
