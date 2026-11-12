package com.ptit.pttk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trans_type_8")
public class TransType8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    public TransType8() {
    }

    public TransType8(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public TransType8(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
