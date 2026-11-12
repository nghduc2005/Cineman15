package com.ptit.pttk.model;

import java.util.List;

public class CrewMemberMovie8 {
    private Movie8 movie;
    private List<CrewMember8> crewMembers;

    public CrewMemberMovie8() {
    }

    public CrewMemberMovie8(Movie8 movie, List<CrewMember8> crewMembers) {
        this.movie = movie;
        this.crewMembers = crewMembers;
    }

    public Movie8 getMovie() {
        return movie;
    }

    public void setMovie(Movie8 movie) {
        this.movie = movie;
    }

    public List<CrewMember8> getCrewMembers() {
        return crewMembers;
    }

    public void setCrewMembers(List<CrewMember8> crewMembers) {
        this.crewMembers = crewMembers;
    }
}
