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
<code>* logger name에는 패키지명을 적어서 설정을 제어한다 *</code>
  
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

---
### 스프링 MVC 요청의 라이프사이클
![스프링 MVC 요청의 라이프사이클](https://stargatex.files.wordpress.com/2015/12/springmvcrequestlifecycle.jpg)
- 출처 : https://stargatex.wordpress.com/2015/12/08/spring-mvc-request-lifecycle/

### 필터와 인터셉터의 차이
- 필터는 디스패치 서블릿 앞 단에서 동작 / 인터셉터는 디스패처 서블릿에서 핸들러 컨트롤러로 가기 전에 동작
- 필터는 J2EE 표준 스펙에 있는 서블릿의 기능 중 일부 / 인터셉터는 스프링 프레임워크에서 제공되는 기능 -> 스프링 빈 사용 가능
- 일반적으로 문자열 인코딩과 같은 웹 전반에서 사용되는 기능은 필터로 구현
- 클라이언트의 요청과 관련이 있는 여러가지 처리(예를 들어 로그인이나 인증, 권한 등)는 인터셉터로 처리

### HandlerInterceptorAdapter로 인터셉터 구현하기
- 스프링에서 인터셉터는 HandlerInterceptorAdapter 클래스를 상속받아서 구현
- HandlerInterceptorAdapter는 다음의 세가지 메서드를 제공함

| 메서드 | 역할 |
| --- | --- |
| preHandle | 컨트롤러 실행 전에 수행 |
| postHandle | 컨트롤러 수행 후 결과를 뷰로 보내기 전에 수행됨 | 
| afterCompletion | 뷰의 작업까지 완료된 후 수행 |

---

### AOP란?
- AOP : Aspect Oriented Programming 의 약자 , 관점지향 프로그래밍
- AOP는 애플리케이션 전반에서 사용되는 기능을 여러 코드에 쉽게 적용할 수 있도록 함
  - ex) 로그, 권한 체크, 인증, 예외처리 등
- AOP는 비즈니스 로직을 구현한 코드에서 공통 기능 코드를 직접 호출 x
  - AOP를 적용하면 공통 기능과 비즈니스 기능을 따로 개발한 후 컴파일하거나 컴파일된 클래스를 로딩하는 시점 등에 AOP가 적용되어 비즈니스 로직 코드 사이에 공통 기능 코드가 삽입됨
  
### AOP 관련 용어

| 용어 | 의미 |
| --- | --- |
| 관점 (Aspect) | 공통적으로 적용될 기능, 횡단 관심사의 기능이라고 할 수 있음, 한 개 이상의 포인트 컷과 어드바이스의 조합으로 만들어짐 |
| 어드바이스 (Advice) | 관점의 구현체로 조인포인트에 삽입되어 동작하는 것을 의미, 스프링에서 사용하는 어드바이스는 동작하는 시점에 따라 다섯 종류로 구분됨 |
| 조인포인트 (Joinpoint) | 어드바이스를 적용하는 지점, 스프링 프레임워크에서 조인포인트는 항상 메서드 실행 단계만 가능 |
| 포인트컷 (Pointcut) | 어드바이스를 적용할 조인포인트를 선별하는 과정이나 그 기능을 정의한 모듈을 의미, 정규표현식이나 AspectJ의 문법을 이용해서 어떤 조인포인트를 사용할 것인지 결정 |
| 타깃 (Target) | 어드바이스를 받을 대상 의미 |
| 위빙 (Weaving) | 어드바이스를 적용하는 것을 의미, 즉, 공통 코드를 원하는 대상에 삽입하는 것 | 

### AOP의 주요 개념
#### 어드바이스
- 관점의 구현체로 조인포인트에 삽입되어 동작하는 것
- 스프링에서 사용하는 어드바이스는 동작하는 시점에 따라 다섯 종류로 구분

| 종류 | 어노테이션 | 설명 |
| --- | --- | --- |
| Before Advice | @Before | 대상 메서드가 실행되기 전에 적용할 어드바이스를 정의 |
| After returning Advice | @AfterReturning | 대상 메서드가 성공적으로 실행되고 결괏값을 반환한 후 적용할 어드바이스 정의 |
| After throwing Advice | @AfterThrowing | 대상 메서드에서 예외가 발생했을 때 적용할 어드바이스 정의, try/catch문의 catch와 비슷한 역할 |
| After Advice | @After | 대상 메서드의 정상적인 수행 여부와 상관없이 무조건 실행되는 어드바이스 정의, finally와 비슷한 역할(예외가 발생해도 무조건 실행) |
| Around Advice | @Around | 대상 메서드의 호출 전후, 예외 발생 등 모든 시점에 적용할 수 있는 어드바이스 정의, 가장 범용적으로 사용 가능 |

#### 포인트컷
- 어드바이스를 적용할 조인포인트를 선별하는 과정이나 그 기능을 정의한 모듈 의미
- 정규표현식이나 AspectJ의 문법을 이용해 어떤 조인포인트를 사용할 것인지 결정

  ##### execution
  - 가장 대표적이고 강력한 지시자
  - 접근제어자, 리턴 타입, 타입패턴, 메서드, 파라미터 타입, 예외 타입 등을 조합해서 가장 정교한 포인트컷 만들기 가능
  - <code>*</code> 는 모든 값 - ex) select* -> select로 시작하는 모든 메서드
  - ..은 0개 이상 - 파라미터, 메서드, 패키지 등 모든 것을 의미
  - 패키지 구조 표현 - 하위의 모든 패키지 의미, 파라미터를 표현 - 파라미터 개수와 관계없이 모든 파라미터
  - and와 or를 조합해서 표현식을 조합 가능함 (&&와 ||로도 표현 가능)
  - 예시

  ```
  execution(void select*(..))           // 1
  execution(* board.controller.*())     // 2
  execution(* board.controller.*(..))   // 3
  execution(* board..select*(*))        // 4
  execution(* board..select*(*,*))      // 5
  ```

    - 1 : 리턴 타입이 void이고 메서드 이름이 select로 시작, 파라미터가 0개 이상인 모든 메서드가 호출될 때
    - 2 : board.controller 패키지 밑에 파라미터가 없는 모든 메서드가 호출될 때
    - 3 : board.controller 패키지 밑에 파라미터가 0개 이상인 모든 메서드가 호출될 때
    - 4 : board 패키지의 모든 하위 패키지에 있는 select로 시작하고 파라미터가 한 개인 모든 프로젝트가 호출될 때
    - 5 : board 패키지의 모든 하위 패키지에 있는 select로 시작하고 파라미터가 두 개인 모든 프로젝트가 호출될 때
      
  ##### within
  - 특정 타입에 속하는 메서드를 포인트컷으로 설정
  - 예시
  
  ```
  within(board.service.boardServiceImpl)  // 1
  within(board.service.*ServiceImpl)      // 2
  ```
  
    - 1 : board.service 패키지 밑에 있는 boardServiceImpl 클래스의 메서드가 호출될 때
    - 2 : board.service 패키지 밑에 있는 ServiceImpl이라는 이름으로 끝나는 메서드가 호출될 때
    
  ##### bean
  - 스프링의 빈 이름의 패턴으로 포인트컷 설정
  - 예시
  
  ```
  bean(boardServiceImpl)    // 1
  bean(*ServiceImpl)        // 2
  ```
  
    - 1 : boardServiceImpl이라는 이름을 가진 빈의 메서드가 호출될 때
    - 2 : ServiceImpl이라는 이름으로 끝나는 빈의 메서드가 호출될 때
### AOP 관련 참고
https://galid1.tistory.com/498

---

### 트랜잭션 ACID
| ACID | 설명 |
| --- | --- |
| 원자성(Atomicity) | 트랜잭션은 하나 이상의 관련된 동작을 하나의 작업 단위로 처리함. 트랜잭션이 처리하는 하나의 작업 단위는 그 결과가 성공 또는 실패할 경우 관련된 동작은 모두 동일한 결과가 나옴. 작업 중 하나라도 실패하면 관련된 트랜잭션 내에서 먼저 처리한 동작들도 모두 처음 상태로 돌아감 |
| 일관성(Consistency) | 트랜잭션이 성공적으로 처리되면 데이터베이스의 관련된 모든 데이터는 일관성을 유지해야 함 |
| 고립성(Isolation) | 트랜잭션은 독립적으로 처리되며, 처리되는 중간에 외부에서의 간섭은 없어야 함. 서로 다른 트랜잭션이 동일한 데이터에 동시에 접근할 경우 적절한 동시 접근 제어를 해야함 |
| 지속성(Durability) | 트랜잭션이 성공적으로 처리되면 그 결과는 지속적으로 유지되어야 함 | 

---
### REST란?
> 잘 표현된 HTTP URI로 리소스를 정의하고 HTTP 메서드로 리소스에 대한 행위를 정의합니다. 리소스는 JSON, XML과 같은 여러 가지 언어로 표현할 수 있습니다.
- REST의 특징을 지키는 API를 'RESTful하다'라고 표현하기도 함

### 리소스
- 서비스를 제공하는 시스템의 자원을 의미하는 것
- URI(Uniform Resource Identifier)로 정의
- REST API의 URI는 리소스의 자원을 표현해야만 함

#### REST API의 URI를 설계할 때 일반적으로 적용하는 규칙
1. URI는 명사 사용
    - 예를 들어 회원 목록을 조회하는 REST API 표현
    ``` GET /members ```
    - GET은 HTTP 메서드로 URI의 리소스를 조회하는 것 의미
    - members 라는 명사를 통해 회원 목록임을 알 수 있음
    
2. 슬래시(/)로 계층 관계 ㅠㅛ현
  - 예를 들어 개 > 진돗개, 불독, 사모예드가 있음
  - 개와 진돗개의 계층관계 표현
  ``` GET /dogs/jindo ```

3. URI의 마지막에는 슬래시 사용 X
  - URI의 끝에 슬래시를 넣어도 실행할 때 문제 X
  - 슬래시 때문에 다음 계층이 있는 것으로 오해 가능 -> 명확한 의미 전달을 위해 슬래시 넣지 않기
  
4. URI는 소문자로만 작성
  - URI 문법 형식을 나타내는 RFC 3986에서는 URI 스키마와 호스트를 제외하고는 대소문자를 구별하도록 규정함
  - 대소문자 섞어서 사용 가능하지만 URI를 기억하기 어렵고 호출할 때 잘못 쓰기 쉬움
  
### HTTP
- REST 서비스에서는 CRUD(Create, Read, Update, Delete)에 해당하는 네 개의 메서드 사용

| HTTP 메서드 | 의미 | 역할 |
| --- | --- | --- |
| POST | Create | 리소스를 생성함 |
| GET | Read | 해당 URI의 리소스 조회 |
| PUT | Update | 해당 URI의 리소스 수정 |
| DELETE | Delete | 해당 URI의 리소스 삭제 | 
