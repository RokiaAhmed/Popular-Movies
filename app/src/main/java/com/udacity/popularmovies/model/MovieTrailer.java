package com.udacity.popularmovies.model;

public class MovieTrailer {

    String id;
    String iso_639_1;
    String iso_3166_1;
    String key;
    String name;
    String site;
    int size;
    String type;

    public String getKey() {
        return key;
    }


    public String getName() {
        return name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }
}
