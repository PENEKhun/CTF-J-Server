# Jeopardy CTF 서버

## 설치 방법
### 소스코드를 클론합니다.
```
git clone https://github.com/PENEKhun/CTF-J-Server.git
```

### 설정파일을 생성합니다.
```
cd CTF-J-Server
vi application.yml
```

```yaml
spring:
  profiles:
    active: prod
  mvc:
    favicon:
      enabled: false
  pathmatch:
    matching-strategy: ant-path-matcher
  jpa:
    database-platform: com.penekhun.ctfjserver.Config.CustomMysqlDialect
    hibernate:
      ddl-auto: none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
      format_sql: false
      use_sql_comment: false
    show-sql: false
  redis:
    host: localhost
    port: 6379
    password:
  # file upload max size (파일 업로드 크기 설정)
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB

server:
  port: 8080
  enable-open-timer: false
  time-zone: Asia/Seoul
  open-time-format: "yyyy-MM-dd HH:mm:ss"
  open-time: "0000-00-00 00:00:00"
  end-time-format: "yyyy-MM-dd HH:mm:ss"
  end-time: "0000-00-00 00:00:00"

jwt:
  header: Authorization
  secret: {jwt secret}
  token-validity-in-seconds: 1800 # 30분
  refresh-token-validity-in-seconds: 10800 # 3시간

header:
  access: "Authorization"
  refresh: "Refresh"

# AWS Account Credentials (AWS 접근 키)
cloud:
  aws:
    credentials:
      accessKey: {access key}
      secretKey: {secret key}
    s3:
      bucket: {bucket name}
    region:
      static: {aws region}
    stack:
      auto: false

springdoc:
  default-produces-media-type: application/json
  default-consumes-media-type: application/x-www-form-urlencoded

---

spring:
  config:
    activate:
      on-profile: "prod"
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: "jdbc:mysql://실제환경-mysql-데이터베이스-주소/스키마?zeroDateTimeBehavior=convertToNull"
    username: "root"
    password: "Anstjdgns1!"
  redis:
    host: "실제환경-redis-host"
    port: "6379"
    password: ""

---

spring:
  config:
    activate:
      on-profile: "dev"
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: "jdbc:mysql://실제환경-데이터베이스-주소/스키마?zeroDateTimeBehavior=convertToNull"
    port: 3306
    username: ""
    password: ""
  redis:
    host: "개발환경-redis-host"
    port: 6379
    password: """
```

### 필수 설정값을 기입합니다.
- 실제 환경 mysql 연결 정보
- 실제 환경 redis 연결 정보
- 개발 환경 mysql 연결 정보
- 실제 환경 redis 연결 정보
- aws bucket 연결 정보
- jwt secret key


## 사용법
```
./gradlew bootRun
```



## 이럴때 사용합니다.
- 폐쇠적인 온라인 CTF 대회를 운영할때