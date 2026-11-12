package com.ptit.pttk.model;

import jakarta.persistence.*;

@Entity
@Table(name = "crew_member_8")
public class CrewMember8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 500)
    private String headshot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CrewRole8 role;

    public CrewMember8() {
    }

    public CrewMember8(Integer id, String name, String headshot, CrewRole8 role) {
        this.id = id;
        this.name = name;
        this.headshot = headshot;
        this.role = role;
    }

    public CrewMember8(String name, String headshot, CrewRole8 role) {
        this.name = name;
        this.headshot = headshot;
        this.role = role;
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

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public CrewRole8 getRole() {
        return role;
    }

    public void setRole(CrewRole8 role) {
        this.role = role;
    }
}
