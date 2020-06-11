package com.example.springboot.configuration;

import com.example.springboot.interceptor.LoggerInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("UTF-8");
        commonsMultipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024);  // 업로드되는 파일의 크기 5MB로 제한
        return commonsMultipartResolver;
    }


    // 스프링에서 인터셉터 구현 WebMvcConfigurerAdapter 또는 WebMvcConfigurer
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoggerInterceptor());   // 인터셉터 등록

        // 인터셉터를 등록할 때에는 addPathPatterns() 메서드와 excludePathPatterns() 메서드를 이용하여 요청 주소의 패턴과
        // 제외할 요청 주소의 패턴을 지정하여 선택적으로 적용 가능
        // ex )
        /* registry.addInterceptor(new LoggerInterceptor())
        *          .addPathPatterns("/**")
        *          .excludePathPatterns("/public/**"); */

    }
}

