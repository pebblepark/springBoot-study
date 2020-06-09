package com.example.springboot.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice   // 해당 클래스가 예외처리 클래스임을 알려줌
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    // 해당 메서드에서 처리할 예외 지정, NullPointerException, NumberFormatException 등 자바 기본 예외 및 프로젝트에 필요한 커스텀 예외를 포함해
    // 각각의 예외에 맞는 적절한 예외 처리 ㅂ피요, Exception.class를 처리하는 메서드는 가장 마지막에 있어야 함
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception) {
        ModelAndView mv = new ModelAndView("error/error_default");  // 예외 발생시 보여줄 화면 지정
        mv.addObject("exception", exception);

        log.error("exception", exception);

        return mv;
    }
}
