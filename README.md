# springBoot-study
Spring boot Study project

- 스프링 부트 시작하기 : 차근차근 따라 하는 단계별 실습
- 출판사 : 프로그래밍 인사이트
- 예제코드 : https://github.com/insightbook/Spring-Boot

---

### 마이바티스 설정하기
- DTO 클래스를 만들 때 자바는 카멜 표기법 사용 / 데이터베이스는 스네이트 표기법 사용
- ex) DTO - boardIdx / DB - board_idx
- 이를 위한 설정
  - 마이바티스가 제공하는 설정 중 mapUnderscoreToCamelCase는 전통적인 데이터베이스의 컬럼과 자바 변수를 매핑해주는 기능
  - 기본적으로 false로 설정되어 있는것을 true로 변경
  - application.yml에 아래코드 추가
  ``` yml
  mybatis:
    configuration:
      map-underscore-to-camel-case: true
  ```
  
  #### 빈 등록하기
  - DatabaseConfiguration 클래스에 다음 코드 추가
  ``` java
  @Bean
  @ConfigurationProperties(prefix="mybatis.configuration")  // 1
  public org.apache.ibatis.session.Configuration mybatisConfig() {
    return new org.apache.ibatis.session.Configuration();   // 2
  }
  ```
  - 1 : application.yml의 설정 중 마이바티스와 관련된 설정 가져오기 
  - 2 : 가져온 마이바티스 설정을 자바 클래스로 만들어서 반환
  
