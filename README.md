# Springboot-Security-OAuth2.0
기본 로그인 + 페이스북,네이버,구글 기반 로그인

## Spring security 일반 로그인 구현
https://github.com/leemin-jae/SpringSecurity-basic


## application.yml 설정
```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
      force: true

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/security?serverTimezone=Asia/Seoul
    username: cos
    password: cos1234

  mvc:
    view:
      prefix: /templates/
      suffix: .mustache

  jpa:
    hibernate:
      ddl-auto: create #create update none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: true

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: {}
            client-secret: {}
            scope:
            - email
            - profile

          facebook:
            client-id: {}
            client-secret: {}
            scope:
              - email
              - public_profile

          #네이버는 provider에 없음 따로 설정해야됨
          naver:
            client-id: {}
            client-secret: {}
            scope:
              - name
              - email
            client_name: Naver
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/naver

        provider:
          naver:
            authorization-uri: https://nid.naver.com/oauth2.0/authorize
            token-uri: https://nid.naver.com/oauth2.0/token
            user-info-uri: https://openapi.naver.com/v1/nid/me
            user-name-attribute: response #회원정보를 json으로 받는데 response라는 키값으로 네이버가 리턴함

```
