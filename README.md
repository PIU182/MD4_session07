# Medical Spring Cloud Config – 4 bài tập

Bộ project gồm:

```text
medical-config-center/
├── config-server/            # port 8888
├── patient-service/          # Config Client
├── medical-config-repo/      # Git repo cấu hình
├── docker-compose.yml        # PostgreSQL + MySQL
├── copy-config-repo-to-c-drive.bat
├── api-tests.http
└── README.md
```

## Công nghệ

- JDK 17
- Spring Boot 3.3.5
- Spring Cloud 2023.0.6
- Gradle
- Spring Cloud Config
- Actuator + `@RefreshScope`
- MySQL cho `dev`
- PostgreSQL cho `prod`

---

# Bài 1 – Config Server + Git local

Trong bộ project đã có `medical-config-repo` được `git init`, branch `main` và có commit mẫu. Để đúng đường dẫn của đề trên Windows, chạy:

```text
copy-config-repo-to-c-drive.bat
```

hoặc tự copy thư mục thành:

```text
C:\medical-config-repo
```

Config Server:

```properties
spring.application.name=config-server
server.port=8888
spring.cloud.config.server.git.uri=${CONFIG_REPO_URI:file:///C:/medical-config-repo}
spring.cloud.config.server.git.default-label=main
```

Nếu branch của bạn là `master`, sửa `default-label=master`.

Chạy `ConfigServerApplication`, rồi mở:

```text
http://localhost:8888/patient-service/default
http://localhost:8888/patient-service/dev
http://localhost:8888/patient-service/prod
```

---

# Bài 2 – Patient Service lấy DB config từ Config Server

`patient-service/build.gradle` có:

```gradle
implementation 'org.springframework.cloud:spring-cloud-starter-config'
```

Local `application.properties` chỉ còn:

```properties
spring.application.name=patient-service
spring.config.import=configserver:http://localhost:8888
```

Không có `spring.datasource.*` trong source của Patient Service. Toàn bộ DB config nằm ở Git repo.

Default config dùng PostgreSQL và port 8081.

---

# Bài 3 – Profiles Dev / Prod

## Dev

File:

```text
patient-service-dev.properties
```

- port 8081
- MySQL
- DB `patient_db_dev`

Chạy IntelliJ với Active profiles:

```text
dev
```

hoặc:

```text
--spring.profiles.active=dev
```

## Prod

File:

```text
patient-service-prod.properties
```

- port 8082
- PostgreSQL
- DB `patient_db`

Chạy:

```text
--spring.profiles.active=prod
```

Config Server tự kết hợp `patient-service.properties` với file theo profile.

---

# Bài 4 – @RefreshScope + /actuator/refresh

Patient Service có Actuator và controller:

```java
@RestController
@RefreshScope
public class WelcomeController {

    @Value("${app.welcome}")
    private String welcomeMessage;

    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }
}
```

Trong Git config:

```properties
app.welcome=Chào mừng tới BV RikkeiAcademy
management.endpoints.web.exposure.include=health,info,refresh
```

Mở:

```text
http://localhost:8081/welcome
```

Sau đó sửa `C:\medical-config-repo\patient-service.properties`:

```properties
app.welcome=Chào mừng tới BV RikkeiEducation
```

Commit:

```bash
git add .
git commit -m "change welcome message"
```

Refresh:

```bash
curl -X POST http://localhost:8081/actuator/refresh
```

PowerShell:

```powershell
Invoke-RestMethod -Method POST http://localhost:8081/actuator/refresh
```

Mở lại `/welcome`, lời chào đổi mà không restart service.

Nếu đang chạy `prod`, dùng port 8082 cho `/welcome` và `/actuator/refresh`.

---

# Chạy database

```bash
docker compose up -d
```

Docker tạo:

```text
MySQL dev:
  localhost:3306
  database: patient_db_dev
  user: root
  password: 123456

PostgreSQL prod/default:
  localhost:5432
  database: patient_db
  user: postgres
  password: 123456
```

---

# Thứ tự chạy

1. `docker compose up -d`
2. Copy config repo sang `C:\medical-config-repo`
3. Chạy `ConfigServerApplication` – 8888
4. Test `http://localhost:8888/patient-service/default`
5. Chạy Patient Service với `dev` hoặc `prod`
6. Test `/welcome`
7. Sửa config + `git add` + `git commit`
8. POST `/actuator/refresh`

---

# Lỗi thường gặp

- File phải là `patient-service.properties`, không phải `.properties.txt`.
- Config Server phải chạy trước Patient Service vì `spring.config.import` không dùng `optional:`.
- Nếu Git branch là `master`, đổi `spring.cloud.config.server.git.default-label=master`.
- Nếu repo không ở C:, đặt biến `CONFIG_REPO_URI`, ví dụ `file:///D:/medical-config-repo`.
- Dev cần MySQL driver và DB `patient_db_dev`; Prod cần PostgreSQL và DB `patient_db`.
