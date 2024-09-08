package com.demo.ex1.sample.controller;

import com.demo.ex1.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
@Log4j2 //로깅용
@RequiredArgsConstructor
public class SampleController {
    private final SampleService sampleService;

    @RequestMapping("/hello")
    public String[] hello() {
        return new String[] {"Hello","World"};
    }
}
