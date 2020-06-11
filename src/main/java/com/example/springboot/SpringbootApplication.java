package com.example.springboot;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@EntityScan (
        basePackageClasses = {Jsr310Converters.class},
        basePackages = {"com.example.springboot"}
)
@SpringBootApplication(exclude={MultipartAutoConfiguration.class})
/* @SpringBootApplication 어노테이션은 @SpqringBootConfiguration, @ComponentScan, @EnableAutoConfiguration 세 개의 어노테이션으로 구성되어 있음
* @EnableAutoConfiguration은 스프링 부트의 자동구성을 사용할 때 exclude를 이용해서 특정한 자동구성을 사용하지 않도록 할 수 있음 */

public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
