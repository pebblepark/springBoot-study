package com.example.springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect /* 자바 코드에서 AOP 설정 */
@Slf4j
public class LoggerAspect {

    @Around("execution(* com.example.springboot..controller.*Controller.*(..)) || " +
            "execution(* com.example.springboot..service.*Impl.*(..)) || " +
            "execution(* com.example.springboot..dao.*Mapper.*(..))")
    /* @Around 어노테이션으로 해당 기능이 실행될 시점, 즉 어드바이스 설정
    * 어드바이스는 다섯 종류가 존재, 여기서는 대상 메서드의 실행 전후 또는 예외 발생 시점에 사용할 수 있는 Around 어드바이스 적용 */
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("================================================= LoggerAspect :: " + joinPoint.toString());
        String type = "";
        // 실행되는 메서드의 이름을 이용해서 컨트롤러, 서비스, 매퍼를 구분한 후 실행되는 메서드의 이름 출력
        String name = joinPoint.getSignature().getDeclaringTypeName();
        if (name.indexOf("Controller") > -1) {
            type = "Controller    \t: ";
        } else if (name.indexOf("Service") > -1) {
            type = "ServiceImpl    \t: ";
        } else if (name.indexOf("Mapper") > -1) {
            type = "Mapper    \t\t: ";
        }

        log.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }
}
