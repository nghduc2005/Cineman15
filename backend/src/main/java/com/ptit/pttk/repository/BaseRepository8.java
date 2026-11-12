package com.ptit.pttk.repository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * BaseRepository8 duy trì kết nối JDBC thuần (#conn : Connection) và EntityManager dùng chung
 * để các Repository con kế thừa, phục vụ yêu cầu kiến trúc PTTK khi cần truy vấn trực tiếp
 * bằng JDBC (gọi Stored Procedure hoặc tối ưu hiệu năng) song song với JPA.
 * Cấu hình kết nối CSDL được đọc động từ application.properties để linh hoạt chuyển đổi giữa các hệ quản trị CSDL.
 */
@Transactional
public class BaseRepository8 {

    protected Connection conn;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired(required = false)
    protected DataSource dataSource;

    public BaseRepository8() {
        initConnection();
    }

    public BaseRepository8(Connection conn) {
        this.conn = conn;
    }

    /**
     * Khởi tạo kết nối JDBC đọc động từ tệp application.properties trên classpath,
     * đảm bảo khi các lớp con được khởi tạo độc lập vẫn lấy đúng thông tin kết nối đã cấu hình.
     */
    private void initConnection() {
        try {
            Properties props = new Properties();
            try (InputStream is = getClass().getResourceAsStream("/application.properties")) {
                if (is != null) {
                    props.load(is);
                }
            }
            // Đọc cấu hình bảo mật local nếu tồn tại (tránh lộ mật khẩu trong git)
            java.io.File localFile = new java.io.File("application-local.properties");
            if (!localFile.exists()) {
                localFile = new java.io.File("backend/application-local.properties");
            }
            if (localFile.exists()) {
                try (InputStream fis = new java.io.FileInputStream(localFile)) {
                    props.load(fis);
                }
            }
            String dbUrl = props.getProperty("spring.datasource.url");
            String user = System.getenv("DB_USERNAME");
            if (user == null || user.isEmpty()) {
                user = props.getProperty("spring.datasource.username", "root");
                if (user.startsWith("${")) user = "root";
            }
            String password = System.getenv("DB_PASSWORD");
            if (password == null) {
                password = props.getProperty("spring.datasource.password", "");
                if (password.startsWith("${")) password = "";
            }
            String driver = props.getProperty("spring.datasource.driver-class-name");
            if (driver == null || driver.isEmpty()) {
                driver = props.getProperty("spring.datasource.driverClassName", "com.mysql.cj.jdbc.Driver");
            }
            if (driver != null && !driver.isEmpty()) {
                Class.forName(driver);
            }
            if (dbUrl != null && user != null) {
                this.conn = DriverManager.getConnection(dbUrl, user, password != null ? password : "");
            }
        } catch (Exception e) {
            // Khi chạy trong Spring Boot, dataSource sẽ được inject và khởi tạo qua @PostConstruct
        }
    }

    /**
     * Tự động đồng bộ kết nối JDBC thuần từ DataSource do Spring Boot quản lý
     * ngay khi Spring hoàn tất inject các dependency.
     */
    @PostConstruct
    public void init() {
        try {
            if (this.dataSource != null && (this.conn == null || this.conn.isClosed())) {
                this.conn = this.dataSource.getConnection();
            }
        } catch (SQLException e) {
            System.err.println("Không thể nạp kết nối JDBC từ DataSource: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (this.conn == null || this.conn.isClosed()) {
                if (this.dataSource != null) {
                    this.conn = this.dataSource.getConnection();
                } else {
                    initConnection();
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo kết nối JDBC: " + e.getMessage());
        }
        return this.conn;
    }

    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public <T> T save(T entity) {
        if (entityManager != null) {
            entityManager.persist(entity);
            return entity;
        }
        return entity;
    }

    @Transactional
    public <T> T merge(T entity) {
        if (entityManager != null) {
            return entityManager.merge(entity);
        }
        return entity;
    }

    @Transactional
    public <T> List<T> saveAll(Iterable<T> entities) {
        if (entityManager != null) {
            for (T entity : entities) {
                entityManager.persist(entity);
            }
        }
        return (List<T>) entities;
    }

    public <T, ID> Optional<T> findById(Class<T> clazz, ID id) {
        if (entityManager != null) {
            T found = entityManager.find(clazz, id);
            return Optional.ofNullable(found);
        }
        return Optional.empty();
    }

    public <T> List<T> findAll(Class<T> clazz) {
        if (entityManager != null) {
            return entityManager.createQuery("SELECT e FROM " + clazz.getSimpleName() + " e", clazz).getResultList();
        }
        return List.of();
    }

    public <T> long count(Class<T> clazz) {
        if (entityManager != null) {
            return entityManager.createQuery("SELECT COUNT(e) FROM " + clazz.getSimpleName() + " e", Long.class).getSingleResult();
        }
        return 0;
    }
}
