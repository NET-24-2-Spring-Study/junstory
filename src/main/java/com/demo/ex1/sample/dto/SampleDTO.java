package com.demo.ex1.sample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder //빌더 패턴을 적용
@AllArgsConstructor //모든 변수 생성자 생성
@NoArgsConstructor //어떤 변수도 쓰지 않는 기본 생성자 생성
public class SampleDTO {

    private Long ssn;
    private String name;

}
