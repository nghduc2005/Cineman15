package com.ptit.pttk.model;

import java.util.List;

public class MovieTransType8 {
    private Movie8 movie;
    private List<TransType8> transTypes;

    public MovieTransType8() {
    }

    public MovieTransType8(Movie8 movie, List<TransType8> transTypes) {
        this.movie = movie;
        this.transTypes = transTypes;
    }

    public Movie8 getMovie() {
        return movie;
    }

    public void setMovie(Movie8 movie) {
        this.movie = movie;
    }

    public List<TransType8> getTransTypes() {
        return transTypes;
    }

    public void setTransTypes(List<TransType8> transTypes) {
        this.transTypes = transTypes;
    }
}
