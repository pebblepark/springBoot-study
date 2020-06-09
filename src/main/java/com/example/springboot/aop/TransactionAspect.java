package com.example.springboot.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Collections;

@Configuration
public class TransactionAspect {

    private static final String AOP_TRANSACTION_METHOD_NAME = "*";
    private static final String AOP_TRANSACTION_EXPRESSION = "execution(* com.example.springboot..service.*Impl.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor transactionAdvice() {
        MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
        RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
        transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME);
        transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        // 트랜잭션에서 롤백을 하는 룰 설정, 예외가 일어나면 롤백이 수행되도록 지정(Exception.class를 롤백의 룰로 등록)
        source.setTransactionAttribute(transactionAttribute);

        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor transactionAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_TRANSACTION_EXPRESSION); // AOP의 포인트컷 설정, 비즈니스 로직이 수행되는 모든 ServiceImpl클래스의 모든 메서드 지정
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
    }
}
