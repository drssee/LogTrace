# LogTrace v1.0
로그추적기 라이브러리 프로젝트
spring-boot-starter,web,aop:3.3.5 / java17 / vue2.x

## 개요
LogTrace 라이브러리는 어노테이션 기반 메서드 관점 AOP 를 이용하여 요청 쓰레드별 로깅 및 알림 기능을 제공합니다. 
로그저장, 경고발송, 관리자페이지 기능을 포함하고 있습니다.
사용 애플리케이션의 yml 이나 properties 파일에 설정 후 간편하게 사용 가능합니다.


## 주요 기능
1. 로그저장:
   추적 패키지의 로그를 저장합니다.
   FileLogSave: 별도의 파일로 로그를 저장합니다.
   DbLogSave: 데이터베이스에 로그를 저장합니다.
   logtrace.save 속성을 통해 저장 유형을 설정합니다.
2. 경고발송:
   추적 패키지에 예외발생시 경고를 발송합니다.
   MessageLogAlert: 메시지 알림 전송됩니다.(유료 api 문제로 인해 구현 중단)
   MailLogAlert: 이메일을 통한 알림 전송됩니다.
   logtrace.alert 속성을 통해 알림 유형을 설정합니다.
3. 관리자페이지:
   Vue 기반 관리자 페이지
   관리자 페이지는 Vue.js로 작성되었습니다.
   Vue 프로젝트를 빌드한 결과물(index.html, CSS, JS 파일 등)은 웹팩을 사용해 dist 폴더에 생성됩니다.
   빌드된 결과물은 JAR 파일에 포함되어 제공됩니다.

## 사용방법
1. 제공된 JAR 파일을 프로젝트의 lib 폴더에 복사합니다.
   프로젝트 빌드 도구(예: Maven, Gradle)에서 해당 JAR 파일을 인식하도록 설정합니다.
2. 스프링 설정 클래스에 @EnableLogTrace 를 사용하여 애플리케이션에 로깅 기능을 활성화합니다.
3. yml or properties 파일에 logtrace 설정을 작성합니다.
logtrace.base-package=com.example.app(!로그 추적 대상 패키지를 지정)
logtrace.alert=message(default-mail)
logtrace.admin-url=/admin/**(default-/log/**)
logtrace.email-id=your-email@google.com(무료 메일 서비스인 구글만 이용 가능)
logtrace.email-pwd=your-password(구글 메일 api 키 등록 필요)
logtrace.save=db(default-file)