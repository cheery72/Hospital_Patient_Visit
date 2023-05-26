## 프로젝트 개요
- 환자 관리 시스템을 개발하는데 사용되는 백엔드 애플리케이션으로서 병원이나 의료 기관에서 환자 데이터를 관리하고 환자 정보에 접근할 수 있는 기능을 제공합니다. 

## 기술 스택
- Java11, Spring Framework, Spring Boot, Spring MVC
- H2 Database
- Spring Data JPA, Querydsl
- Gradle
- Lombok, Validation


## 주요 기능
### 환자 등록
- URL: /api/v1/{hospitalId}/patients
- Method: POST
- Body: {"patientName": "value", "genderCode": "value", "birthDate": "value", "phoneNumber": "value"}
- 이름, 성별코드, 생년월일, 핸드폰번호를 요청받아 환자 정보를 등록합니다.
- 병원별로 중복되지 않도록 서버에서 UUID를 생성하여 환자 등록번호로 사용합니다.

### 환자 수정
- URL: /api/v1/{patientId}/patients
- Method: PATCH
- Body: {"patientName": "value", "genderCode": "value", "birthDate": "value", "phoneNumber": "value"}
- 이름, 성별코드, 생년월일, 핸드폰번호를 요청받아 환자 정보를 수정합니다.

### 환자 삭제
- URL: /api/v1/{patientId}/patients
- Method: DELETE
- 환자 삭제는 플래그를 이용하여 삭제 여부를 표시하는 방식으로 처리됩니다.

### 환자 조회
- URL: /api/v1/{patientId}/patients
- Method: GET
- 환자 ID를 이용하여 한 환자의 정보와 병원 내원 정보를 조회합니다.

### 환자 목록 조회
- URL: /api/v1/{searchParams}/patients/search
- Method: GET
- 환자 목록을 검색할 때 환자이름, 환자등록번호, 생년월일을 기준으로 동적으로 검색 조건을 설정할 수 있습니다.
- 페이지 번호와 데이터 개수를 설정하여 결과를 페이징 처리할 수 있습니다.




## 설치 및 실행 방법
### 1. Java 설치
- Java11 버전을 설치합니다.

### 2. 프로젝트 코드 가져오기
- 프로젝트 코드를 가져올 Git 저장소를 클론하거나 다운로드합니다.

### 3. 의존성 관리 도구 설치
- 프로젝트는 Gradle을 사용하여 의존성 관리와 빌드를 수행합니다.

### 4. 데이터베이스 설정
- 프로젝트는 H2 데이터베이스를 사용하므로 H2 데이터베이스를 설정을 진행합니다.

### 5. 프로젝트 구성 파일 수정
- application.yml 파일을 열어 데이터베이스 연결 정보를 수정합니다.

### 6. 프로젝트 빌드:
- `./gradlew build`

### 7. 프로젝트 실행:
- `./gradlew bootRun`  

