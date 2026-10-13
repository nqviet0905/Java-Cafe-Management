# ☕ Java Cafe Management

## 1. Giới thiệu

**Java Cafe Management** là ứng dụng quản lý quán cà phê được xây dựng bằng Java Swing, sử dụng MySQL để lưu trữ và quản lý dữ liệu.

Dự án giúp thực hiện các nghiệp vụ quản lý cơ bản của quán cà phê thông qua giao diện ứng dụng desktop.

## 2. Công nghệ sử dụng

- **Ngôn ngữ lập trình:** Java
- **Giao diện:** Java Swing
- **Cơ sở dữ liệu:** MySQL
- **IDE:** NetBeans
- **Kết nối cơ sở dữ liệu:** JDBC

## 3. Chức năng chính

### 3.1. Quản lý tài khoản
- Đăng nhập và đăng xuất.
- Quản lý tài khoản.
- Đổi mật khẩu.
- Theo dõi lịch sử đăng nhập.

### 3.2. Quản lý nhân viên
- Quản lý thông tin nhân viên.
- Quản lý lương nhân viên.

### 3.3. Quản lý sản phẩm
- Quản lý sản phẩm.
- Quản lý loại sản phẩm.

### 3.4. Quản lý bàn và gọi món
- Quản lý bàn trong quán.
- Gọi món và quản lý đơn hàng.

### 3.5. Thanh toán
- Thực hiện thanh toán hóa đơn.

### 3.6. Quản lý nguyên liệu
- Quản lý thông tin nguyên liệu.

### 3.7. Thống kê doanh thu
- Theo dõi và thống kê doanh thu.

## 4. Cấu trúc dự án

```text
Java-Cafe-Management/
├── nbproject/
├── src/
├── test/
├── .gitignore
├── build.xml
├── manifest.mf
└── README.md


- `src/`: Chứa mã nguồn Java của ứng dụng.
- `test/`: Thư mục dành cho mã nguồn kiểm thử.
- `nbproject/`: Cấu hình dự án NetBeans.
- `build.xml`: Cấu hình quá trình biên dịch và xây dựng dự án.
- `manifest.mf`: Tệp thông tin manifest của ứng dụng.
- `.gitignore`: Danh sách các tệp và thư mục không cần đưa lên Git.

## 5. Hướng dẫn cài đặt và chạy chương trình

### Yêu cầu

- JDK tương thích với phiên bản Java của dự án.
- NetBeans IDE.
- MySQL Server.
- Các thư viện JDBC cần thiết cho dự án.

### Các bước thực hiện

1. Tải hoặc clone mã nguồn từ GitHub.
2. Mở NetBeans IDE.
3. Chọn **File → Open Project** và chọn thư mục dự án.
4. Chuẩn bị cơ sở dữ liệu MySQL theo cấu trúc mà ứng dụng yêu cầu.
5. Kiểm tra cấu hình kết nối MySQL trong mã nguồn.
6. Đảm bảo các thư viện cần thiết đã được cấu hình.
7. Chạy ứng dụng từ NetBeans.

**Lưu ý:** Cần cấu hình đúng thông tin kết nối và cơ sở dữ liệu trước khi chạy chương trình. Dự án hiện được hướng dẫn chạy qua NetBeans.

## 6. Mục tiêu học tập

Thông qua dự án này, tôi có cơ hội thực hành:

- Lập trình Java hướng đối tượng.
- Xây dựng giao diện desktop bằng Java Swing.
- Kết nối và thao tác dữ liệu với MySQL.
- Xây dựng các chức năng quản lý trong một ứng dụng.
- Sử dụng Git và GitHub để quản lý mã nguồn.

## 7. Tác giả

**GitHub:** [nqviet0905](https://github.com/nqviet0905)


## 8. Hình ảnh giao diện

### Giao diện đăng nhập
![Giao diện đăng nhập](screenshots/login.png)

### Giao diện chính
![Giao diện chính](screenshots/main-screen.png)




*Dự án phục vụ mục đích học tập và thực hành lập trình phần mềm.*
