package cafe;

import java.time.LocalDateTime;

public class MySession {

    public static Taikhoan currentUser;     // User hiện tại
    public static LocalDateTime loginTime;  // Thời gian đăng nhập

    public static void setCurrentUser(Taikhoan user) {
        currentUser = user;
        loginTime = LocalDateTime.now();   // Lưu thời gian đăng nhập
    }

    public static Taikhoan getCurrentUser() {
        return currentUser;
    }

    public static LocalDateTime getLoginTime() {
        return loginTime;
    }

    public static void clear() {
        currentUser = null;
        loginTime = null;
    }
}
