# LogTrace v1.0
로그추적기 라이브러리 프로젝트<br>
spring-boot:3.3.5(web,aop) / java17 / vue2.x<br>

## 개요
LogTrace 라이브러리는 어노테이션 기반 메서드 관점 AOP 를 이용하여 요청 쓰레드별 로깅 및 알림 기능을 제공합니다. <br>
로그저장, 경고발송, 관리자페이지 기능을 포함하고 있습니다.<br>
사용 애플리케이션의 yml 이나 properties 파일에 설정 후 간편하게 사용 가능합니다.<br>


## 주요 기능
1. 로그저장: 추적 패키지의 로그를 저장합니다.<br>
   `FileLogSave`: 별도의 파일로 로그를 저장합니다.<br>
   `DbLogSave`: 데이터베이스에 로그를 저장합니다.<br>
   `logtrace.save` 속성을 통해 저장 유형을 설정합니다.<br><br>
2. 경고발송: 추적 패키지에 예외발생시 경고를 발송합니다.<br>
   `MessageLogAlert`: 메시지 알림 전송됩니다.(유료 api 문제로 인해 구현 중단)<br>
   `MailLogAlert`: 이메일을 통해 알림이 전송됩니다.<br>
   `logtrace.alert` 속성을 통해 알림 유형을 설정합니다.<br><br>
3. 관리자페이지: Vue 기반 관리자 페이지<br>
   Vue 프로젝트를 빌드한 결과물(index.html, CSS, JS 파일 등)이 JAR 파일에 포함되어 제공됩니다.<br><br>
   
## 사용방법
1. 제공된 JAR 파일을 프로젝트의 lib 폴더에 복사합니다.<br>
   프로젝트 빌드 도구(예: Maven, Gradle)에서 해당 JAR 파일을 인식하도록 설정합니다.<br>
2. 스프링 설정 클래스에 `@EnableLogTrace` 를 사용하여 애플리케이션에 로깅 기능을 활성화합니다.<br>
3. yml or properties 파일에 logtrace 설정을 작성합니다.<br>
   `logtrace.base-package=com.example.app(!로그 추적 대상 패키지를 지정)`<br>
   `logtrace.alert=message(default-mail)`<br>
   `logtrace.admin-url=/admin/**(default-/log/**)`<br>
   `logtrace.email-id=your-email@google.com(무료 메일 서비스인 구글만 이용 가능)`<br>
   `logtrace.email-pwd=your-password(구글 메일 api 키 등록 필요)`<br>
   `logtrace.save=db(default-file)`<br>