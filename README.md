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
  
--- 
### LogBack 설정
<code>src/main/resources/logback-spring.xml</code>
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="true">
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <Pattern>%d %5p [%c] %m%n</Pattern>
        </encoder>
    </appender>
    
    <appender name="console-infolog" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <Pattern>%d %5p %m%n</Pattern>
        </encoder>
    </appender>
    
    <!-- logger -->
    <logger name="board" level="DEBUG" appender-ref="console"/>
    
    <!-- root looger -->
    <root level="off">
        <appender-ref ref="console"/>
    </root>
</configuration>
```
### Logback의 주요 구성요소
- appender는 로그를 어디에 출력할지 (콘솔, 파일 기록, DB 저장 등) 결정하는 역할
- encoder는 appender에 포함되어 출력할 로그를 지정한 형식으로 변환하는 역할
  - console 은 debug용으로 사용
  - console-infolog 는 info 레벨로 사용
- logger는 로그를 출력하는 요소, Level 속성을 통해 출력할 로그의 레벨을 조절하여 appender에 전달
  - debug 레벨의 로그를 출력하는 형식은 console 이라는 이름의 appender 사용
  
### Logback에서 사용되는 로그의 레벨
| 로그레벨 | 설명 |
| --- | --- |
| trace | 모든 로그 출력 |
| debug | 개발할 때 디버그 용도로 사용 |
| info | 상태 변경 등과 같은 정보성 메시지 |
| warn | 프로그램 실행에는 문제가 없지만 추후 시스템 에러의 원인이 될 수 있다는 경고성 메시지 |
| error | 요청을 처리하던 중 문제가 발생 | 

### log4jdbc 로거
| 로거 | 설명 |
| --- | --- | 
| jdbc.sqlonly | SQL을 보여줌. Prepared statement의 경우 관련된 파라미터는 자동으로 변경되어 출력 |
| jdbc.sqltiming | SQL문과 해당 SQL문의 실행 시간을 밀리초 단위로 보여줌 |
| jdbc.audit | ResultSets를 제외한 모든 JDBC 호출 정보를 보여줌. 매우 많은 로그 발생 -> JDBC 관련 문제를 추적하기 위한 것이 아니라면 일반적으로 사용 x|
| jdbc.resultset | ResultSets를 포함한 모든 JDBC 호출 정보를 보여줌. jdbc.audit 보다 더 많은 로그 생성 |
| jdbc.resulttable | SQL의 조회 결과를 테이블로 보여줌 | 
| jdbc.connection | Conntection의 연결과 종료에 관련된 로그를 보여줌. Connection 누수(leak) 문제를 해결하는데 도움됨 | 

### log4jdbc 설정
https://twofootdog.tistory.com/52 참고
