package com.ptit.pttk.model;

import java.util.List;

public class FormatMovie8 {
    private Movie8 movie;
    private List<Format8> formats;

    public FormatMovie8() {
    }

    public FormatMovie8(Movie8 movie, List<Format8> formats) {
        this.movie = movie;
        this.formats = formats;
    }

    public Movie8 getMovie() {
        return movie;
    }

    public void setMovie(Movie8 movie) {
        this.movie = movie;
    }

    public List<Format8> getFormats() {
        return formats;
    }

    public void setFormats(List<Format8> formats) {
        this.formats = formats;
    }
}
