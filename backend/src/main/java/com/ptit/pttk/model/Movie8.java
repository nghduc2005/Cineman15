package com.ptit.pttk.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movie_8")
public class Movie8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String poster;

    @Column(length = 500)
    private String trailer;

    @Column(name = "movie_charge", precision = 12, scale = 2)
    private BigDecimal movieCharge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MovieLanguage8 language;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false)
    private int runtime; // in minutes

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_label_id")
    private MovieLabel8 movieLabel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "category_movie_8",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category8> categoryList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "crew_member_movie_8",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "crew_member_id")
    )
    private Set<CrewMember8> crewMemberList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "format_movie_8",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "format_id")
    )
    private Set<Format8> formatList = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "movie_trans_type_8",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "trans_type_id")
    )
    private Set<TransType8> transTypeList = new HashSet<>();

    public Movie8() {
    }

    public Movie8(Integer id, String name, String description, String poster, String trailer,
                  BigDecimal movieCharge, MovieLanguage8 language, LocalDate releaseDate,
                  LocalDate endDate, int runtime, MovieLabel8 movieLabel) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.poster = poster;
        this.trailer = trailer;
        this.movieCharge = movieCharge;
        this.language = language;
        this.releaseDate = releaseDate;
        this.endDate = endDate;
        this.runtime = runtime;
        this.movieLabel = movieLabel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        if (code != null && !code.isEmpty()) {
            return code;
        }
        return id != null ? String.format("P%03d", id) : null;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public BigDecimal getMovieCharge() {
        return movieCharge;
    }

    public void setMovieCharge(BigDecimal movieCharge) {
        this.movieCharge = movieCharge;
    }

    public MovieLanguage8 getLanguage() {
        return language;
    }

    public void setLanguage(MovieLanguage8 language) {
        this.language = language;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public MovieLabel8 getMovieLabel() {
        return movieLabel;
    }

    public void setMovieLabel(MovieLabel8 movieLabel) {
        this.movieLabel = movieLabel;
    }

    public Set<Category8> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(Set<Category8> categoryList) {
        this.categoryList = categoryList;
    }

    public Set<CrewMember8> getCrewMemberList() {
        return crewMemberList;
    }

    public void setCrewMemberList(Set<CrewMember8> crewMemberList) {
        this.crewMemberList = crewMemberList;
    }

    public Set<Format8> getFormatList() {
        return formatList;
    }

    public void setFormatList(Set<Format8> formatList) {
        this.formatList = formatList;
    }

    public Set<TransType8> getTransTypeList() {
        return transTypeList;
    }

    public void setTransTypeList(Set<TransType8> transTypeList) {
        this.transTypeList = transTypeList;
    }
}
