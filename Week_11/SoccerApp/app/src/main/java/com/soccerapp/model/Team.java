package com.soccerapp.model;

import java.util.UUID;

/**
 * Represents a soccer team. Implements SoccerEntity so it can be stored
 * in the generic Repository<T extends SoccerEntity>.
 */
public class Team implements SoccerEntity {

    private final String id;
    private String name;
    private String country;
    private String league;
    private String stadium;
    private int founded;

    public Team(String name, String country, String league, String stadium, int founded) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Team name cannot be empty");
        if (country == null || country.trim().isEmpty()) throw new IllegalArgumentException("Country cannot be empty");
        if (league == null || league.trim().isEmpty()) throw new IllegalArgumentException("League cannot be empty");
        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.country = country.trim();
        this.league = league.trim();
        this.stadium = stadium != null ? stadium.trim() : "Unknown";
        this.founded = founded;
    }

    @Override public String getId()   { return id; }
    @Override public String getName() { return name; }
    public String getCountry()        { return country; }
    public String getLeague()         { return league; }
    public String getStadium()        { return stadium; }
    public int    getFounded()        { return founded; }

    public void setName(String name)       { this.name = name; }
    public void setCountry(String country) { this.country = country; }
    public void setLeague(String league)   { this.league = league; }

    @Override
    public String toString() {
        return name + " (" + league + ")";
    }
}
