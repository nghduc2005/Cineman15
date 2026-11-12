package com.ptit.pttk.model;

import java.util.List;

public class CategoryMovie8 {
    private Movie8 movie;
    private List<Category8> categories;

    public CategoryMovie8() {
    }

    public CategoryMovie8(Movie8 movie, List<Category8> categories) {
        this.movie = movie;
        this.categories = categories;
    }

    public Movie8 getMovie() {
        return movie;
    }

    public void setMovie(Movie8 movie) {
        this.movie = movie;
    }

    public List<Category8> getCategories() {
        return categories;
    }

    public void setCategories(List<Category8> categories) {
        this.categories = categories;
    }
}
