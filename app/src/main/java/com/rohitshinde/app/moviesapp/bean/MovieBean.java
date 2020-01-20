package com.rohitshinde.app.moviesapp.bean;

import java.io.Serializable;

public class MovieBean implements Serializable {
    private final String title;
    private final String releaseDate;
    private final String moviePoster;
    private final String voteAverage;
    private final String plotSynopsis;

    public MovieBean(String title, String releaseDate, String moviePoster, String voteAverage, String plotSynopsis) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

}
