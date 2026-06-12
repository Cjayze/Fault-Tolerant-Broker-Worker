# Fault-Tolerant Broker-Worker System

## Giới thiệu

Đây là dự án mô phỏng hệ thống điều phối tác vụ phân tán dựa trên kiến trúc **Broker–Worker** tích hợp cơ chế **Fault Tolerance**.

Hệ thống bao gồm một Broker đóng vai trò điều phối trung tâm và nhiều Worker thực hiện xử lý tác vụ. Broker có nhiệm vụ phân phối công việc, theo dõi trạng thái Worker và tự động khôi phục tác vụ khi xảy ra lỗi.

Dự án được xây dựng nhằm minh họa các kiến thức quan trọng của môn **Hệ phân tán** như:

* Task Scheduling
* Load Balancing
* Parallel Processing
* Heartbeat
* Failure Detection
* Fault Tolerance
* Task Reassignment

---

## Kiến trúc hệ thống

```text
               ** +------------+
                |   Client   |
                +------------+
                       |
                       v
                +------------+
                |   Broker   |
                +------------+
                 /     |     \
                /      |      \
               v       v       v
         +--------+ +--------+ +--------+
         |Worker 1| |Worker 2| |Worker 3|
         +--------+ +--------+ +--------+
```
**
### Thành phần

#### Broker

* Nhận tác vụ từ Client
* Quản lý danh sách Task
* Phân phối Task cho Worker
* Theo dõi trạng thái Worker
* Phát hiện Worker lỗi
* Thực hiện Task Reassignment

#### Worker

* Nhận Task từ Broker
* Xử lý Task
* Gửi Heartbeat định kỳ
* Trả kết quả về Broker

#### Task

* Đại diện cho công việc cần xử lý
* Có các trạng thái:

  * Waiting
  * Running
  * Completed

---

## Chức năng chính

### 1. Tạo và quản lý tác vụ

Người dùng có thể tạo nhiều Task thông qua giao diện.

Ví dụ:

```text
Task 1
Task 2
Task 3
```

### 2. Phân phối tác vụ

Broker sử dụng thuật toán Round Robin để phân phối công việc đồng đều cho các Worker.

Ví dụ:

```text
Task 1 -> Worker 1
Task 2 -> Worker 2
Task 3 -> Worker 3
Task 4 -> Worker 1
```

### 3. Xử lý song song

Các Worker hoạt động độc lập trên các luồng khác nhau, cho phép nhiều tác vụ được xử lý đồng thời.

### 4. Giám sát Worker

Worker gửi Heartbeat định kỳ tới Broker.

Broker sử dụng Heartbeat để theo dõi trạng thái hoạt động của từng Worker.

### 5. Phát hiện lỗi

Nếu Worker ngừng gửi Heartbeat trong khoảng thời gian xác định:

```text
Current Time - Last Heartbeat > Timeout
```

Broker sẽ đánh dấu Worker là FAILED.

### 6. Khôi phục tác vụ

Nếu Worker gặp lỗi trong khi đang xử lý Task:

```text
Task 5
   |
Worker 2
   |
 FAILED
```

Broker sẽ tự động:

```text
Task 5
   |
 Waiting
   |
Worker 1
   |
Completed
```

Đảm bảo không mất tác vụ.

---

## Công nghệ sử dụng

* Java
* Java Swing
* Multithreading
* Object-Oriented Programming (OOP)

---

## Cấu trúc dự án

```text
src/
│
├── Main.java
├── MainFrame.java
├── Broker.java
├── Worker.java
├── Task.java
│
└── Class/
    ├── Main.class
    ├── MainFrame.class
    ├── Broker.class
    ├── Worker.class
    └── Task.class
```

---

## Cách chạy chương trình

### Yêu cầu

* Java JDK 17 hoặc mới hơn

Kiểm tra:

```bash
java -version
javac -version
```

### Biên dịch

```bash
cd src

javac Main.java Broker.java Worker.java Task.java MainFrame.java
```

### Chạy chương trình

```bash
java Main
```

---

## Hướng dẫn sử dụng

### Thêm Task

Nhấn nút:

```text
Add Task
```

Task mới sẽ được tạo và đưa vào hệ thống.

### Theo dõi trạng thái

Danh sách Task hiển thị:

```text
Task 1 - Completed by Worker 1
Task 2 - Running on Worker 2
Task 3 - Waiting
```

### Mô phỏng lỗi Worker

Nhấn:

```text
Kill Worker
```

Broker sẽ:

```text
Worker 2 FAILED
```

và tự động giao lại các Task chưa hoàn thành cho Worker khác.

---

## Kết quả đạt được

✔ Phân phối tác vụ giữa nhiều Worker

✔ Xử lý song song

✔ Cân bằng tải bằng Round Robin

✔ Giám sát Worker bằng Heartbeat

✔ Phát hiện lỗi bằng Timeout Detection

✔ Tự động khôi phục tác vụ

✔ Hỗ trợ Fault Tolerance

---

## Kiến thức hệ phân tán được áp dụng

| Kiến thức           | Áp dụng trong dự án          |
| ------------------- | ---------------------------- |
| Task Scheduling     | Broker phân phối Task        |
| Load Balancing      | Round Robin                  |
| Parallel Processing | Nhiều Worker xử lý đồng thời |
| Heartbeat           | Theo dõi Worker              |
| Failure Detection   | Timeout Detection            |
| Fault Tolerance     | Khôi phục khi Worker lỗi     |
| Recovery Mechanism  | Task Reassignment            |
| Scalability         | Dễ dàng thêm Worker          |

---

## Hướng phát triển

* Giao tiếp bằng TCP Socket
* Nhiều Broker dự phòng
* Leader Election
* Persistent Queue với MySQL hoặc MongoDB
* RabbitMQ hoặc Apache Kafka
* Docker & Kubernetes
* Dashboard giám sát hệ thống
* Prometheus và Grafana

---
