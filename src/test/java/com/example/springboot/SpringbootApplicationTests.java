package com.example.springboot;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    private SqlSessionTemplate sqlSession;

    @Test
    void contextLoads() {
    }

    @Test
    public void testSqlSession() throws Exception {
        System.out.println(sqlSession.toString());
    }

}
