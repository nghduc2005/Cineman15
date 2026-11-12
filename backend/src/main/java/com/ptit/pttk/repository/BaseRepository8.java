package com.ptit.pttk.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * BaseRepository8 duy trì kết nối JDBC thuần (#conn : Connection) và EntityManager dùng chung
 * để các Repository con kế thừa, phục vụ yêu cầu kiến trúc PTTK khi cần truy vấn trực tiếp
 * bằng JDBC (gọi Stored Procedure hoặc tối ưu hiệu năng) song song với JPA.
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

    private void initConnection() {
        try {
            String dbUrl = "jdbc:h2:mem:cinemadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
            String user = "sa";
            String password = "";
            Class.forName("org.h2.Driver");
            this.conn = DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
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
