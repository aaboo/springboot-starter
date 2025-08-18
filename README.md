# Starter
- 난 당신이 가장 싫어하는 웹개발 업무가 환경설정임을 압니다. 

- 새로운 WEB, API 웹프로젝트를 시작할 때 처음부터 환경설정 잡느라 고생하지 마시고, 먼저, 실장님한테는 일주일 걸린다 얘기하세요.

- 첫 날은 STARTER 복사해서 소스에 있는 프로젝트명과 패키지명을 당신이 원하는 명칭으로 싹다 변경해 주고, Server 구동해 봅니다. HomeController를 보면 자세한 기본 테스트 내역이 들어 있습니다.

- 남은 6일의 시간동안 이런 저런 테스트도 하면서 STARTER를 발전시킵니다. 발전된 STARER는 꼼꼼히 살펴서 README.md 파일에 친절히 설명 달아주시고, origin main으로 PUSH해 주세요. 그럼 당신의 생명이 연장될 뿐만 아니라, 죽어서 천당에도 갈 수 있습니다.



# 개발환경
- openjdk-21.0.1+12
- apache-tomcat-10.1.18
- SpringBoot 4.21.0
- Oracle/MySql
- thymeleaf



# Starter > [신규ID] 프로젝트 변경하기
##### 1. 이클립스 Package Exlplorer 에서 프로젝트 복사하기
- starter 복사 붙여넣기 > 프로젝트명 변경(f2) > [신규ID]

##### [프로젝트 폴더] 프로젝트명 변경
- [ ] src/main/java > com.nicepay.starter.* 를 com.nicepay.[신규ID].* 로 변경
- [ ] src/main/resources/logback.xml 수정
- [ ] src/main/resources/application-local.properties
- [ ] /pom.xml 수정 후 [프로젝트 우클릭] > maven > update project
- [ ] src/main/resources/com.nicepay.[신규ID].home 에서 .xml 파일(테스트용 Mapper)의 소스코드 중 starter 를 [신규ID] 로 변경
- [ ] src/main/resources/logback.xml 에서 로그파일 경로와 파일명 수정
- [ ] LogbackFilter.java 에서 denyTarget 수정
- [ ] OracleConfig.java, MysqlConfig.java 에서 start > [신규ID] 변경
- [ ] application-local.properties 에서 spring.thymeleaf.prefix 있는 start [신규ID] 변경
- [ ] README.md 도 수정

##### [Servers] 신규서버 생성
- Servers > new > Server > Apache > Tomcat v10.1 Server

```
	Server name : [신규ID] > [신규ID] 오른쪽으로
```

##### 4. [starter 서버 설정파일 창] 상세설정 변경
- [ ] Timeouts > start 999초로 변경
- [ ] Moudule탭 > edit > path > /[신규ID] 를 / 로 변경 저장(ctrl+s)
- [ ] Open launch configuration > arguments탭 > VM arguments에 아래 문구 추가

```
  -Dspring.profiles.active=local
```

## 소스위치 가이드
전체 파일위치와 역할을 설명합니다.

```
D:/VANDEV2WEB
    /download - 외부에서 반입한 검증된 다운로드 소스
    /engine - jdk, apache와 같은 APP/WAS 실행 엔진
        /apache-tomcat-10.1.18
        /OpenJDK21U-jdk_x64_windows_hotspot_21.0.1_12
    /maven
        /repository - maven 레피지토리
        /settings.xml - 메이븐 로컬 설정(중요)
    /SPRINGBOOT -  Spirng Tool Suite(STS) 소스
        ...
        /SpringToolSuite4.exe - 이 파일을 실행하서 SpringBoot 개발작업을 진행함
        ...
    /workspace - APP 소스
        /SPRINGBOOT - 스프링부트로 개발된 소스 모음
            /.metadata - 이클립스 설정등이 들어가 있을 것으로 봄
            /Servers - SpringBoot4.21.0 내장 tomcat을 사용하지 않기 위한, 외장 tomcat
            /sspringboot-starter - starter 프로젝트 소스파일
                ...
                .gitignore - git에 업로드되지 않을 예외 항목을 적어놓은 파일
                /src/main
                    /java - Stater Back-end 영역 소스
                    /resources - Starter Back-end 영역 환경설정
                    /webapp - Starter Front-end 영역 소스
                ...
```



## Gitlab 연동

- 가정1: gitlab 에 신규프로젝트를 생성하였다.
- 가정2: 로컬에는 거의 완성된 프로젝트가 이미 만들어져 있고 gitlab 프로젝트에 덮어쓰기 하고 싶다.
- 가정3: 원본 소스는 로컬의 works/dev 에만 있다.
- 가정4: 원격(GitLab)에 이미 프로젝트가 생성되어 있다고 본다.

### 
**권장방법**
 - [ ] Step1 : 프로젝트 소스 백업
 - [ ] Step2 : 개발자 로컬에서 원격저장소 연결하기
 - [ ] Step3 : 백업파일 복원
 - [ ] Step4 : Commit & Push

##### Step1 : 프로젝트 소스 백업
	작업폴더로 가서 프로젝트 폴더 통째로 복사본을 만들어준다.
	D:\VANDEV2WEB\workspace\SPRINGBOOT\starter
	D:\VANDEV2WEB\workspace\SPRINGBOOT\starter - 복사본
	
##### Step2 : 개발자 로컬에서 원격저장소 연결하기 (Git Bash로 개발자 로컬에서 진행)
	VSCode나 SourceTree에서 bash 터미널을 열어서 진행
	D:\VANDEV2WEB\workspace\SPRINGBOOT\starter (works/dev) bash창에서 작업 시작


##### Step2-1 : 원격브랜치 연결을 처음 시도할 경우

git remote -v로 연결된 프로젝트가 없는 경우 "git remote add" 명령으로 연결해 준다.
이미 연결되어 있을 경우는 Step2-2로 진행

```
[work/dev] git checkout main
		-- 현재 브랜치를 main 으로 변경한다.

[main] git remote add origin http://bxm:xWMqTxbmG4Cag1ysS7Lg@192.168.7.90:8082/springboot/starter.git
		-- 소스트리에서 Remotes가 활성화되어 origin 이 활성화 된다.(starter.git 으로 새로 연결됨)

[main] git fetch
		-- 최초 패치를 받는다 : 소스트리에서 Remotes의 origin/main 이 활성화 된다.

```

##### Step2-2 : 원격브랜치에 다른 서비스가 이미 연결되어 있을 경우

STARTER 프로젝트를 복사하여 다른 프로젝트를 생성할 경우 기존의 프로젝트로 연결되어 있을 수 있다. (여기서는 apx.git으로 예를 듦)
git remote -v로 아래처럼 **다른 프로젝트**에 연결되어 있을 경우 "git remote set-url" 명령으로 변경해 준다.

	origin  http://aaboo:123sdfssdfs12dfdf3123sdfs@192.168.1.2:8082/springboot/apx.git (fetch)
	origin  http://aaboo:123sdfssdfs12dfdf3123sdfs@192.168.1.2:8082/springboot/apx.git (push)



```
[works/dev] git checkout main
		-- 현재 브랜치를 main 으로 변경한다.

[main] git remote set-url origin http://aaboo:123sdfssdfs12dfdf3123sdfs@192.168.1.2:8082/springboot/starter.git
		-- 소스트리에서 Remotes가 활성화되어 origin 이 활성화 된다.(apx.git 에서 starter.git 으로 연결됨)

[works/dev] git fetch
		-- 최초 패치를 받는다 : 소스트리에서 Remotes의 origin/main 이 활성화 된다.

```

##### Step3: 백업파일 복원(main 브랜치에서 진행)

백업한 폴더에서 아래 폴더와 파일을 복사해 프로젝트 폴더로 붙여넣기 해준다. (main 브랜치 상태에서 실행)

```
/src
/pom.xml
/README.md
/.gitignore

```

##### Step4: Commit & Push

```

[main] git add .

[main] git commit -m "STARTER 복사 완료" 

[main] git push origin main

```



### 수고하셨습니다.
