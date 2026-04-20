package com.soccerapp.model;

import java.util.UUID;

/**
 * Represents a soccer match. Implements SoccerEntity.
 * getName() returns a human-readable "Home vs Away" label.
 */
public class Match implements SoccerEntity {

    private final String id;
    private String homeTeam;
    private String awayTeam;
    private String score;
    private String league;
    private String date;
    private String venue;

    public Match(String homeTeam, String awayTeam, String score,
                 String league, String date, String venue) {
        if (homeTeam == null || homeTeam.trim().isEmpty()) throw new IllegalArgumentException("Home team cannot be empty");
        if (awayTeam == null || awayTeam.trim().isEmpty()) throw new IllegalArgumentException("Away team cannot be empty");
        this.id = UUID.randomUUID().toString();
        this.homeTeam = homeTeam.trim();
        this.awayTeam = awayTeam.trim();
        this.score  = score  != null ? score.trim()  : "TBD";
        this.league = league != null ? league.trim() : "Unknown";
        this.date   = date   != null ? date.trim()   : "TBD";
        this.venue  = venue  != null ? venue.trim()  : "Unknown";
    }

    @Override public String getId()   { return id; }
    @Override public String getName() { return homeTeam + " vs " + awayTeam; }
    public String getHomeTeam()       { return homeTeam; }
    public String getAwayTeam()       { return awayTeam; }
    public String getScore()          { return score; }
    public String getLeague()         { return league; }
    public String getDate()           { return date; }
    public String getVenue()          { return venue; }

    public void setScore(String score) { this.score = score; }

    /** Returns true if this match involves the given team name (home or away). */
    public boolean involvesTeam(String teamName) {
        if (teamName == null) return false;
        return homeTeam.equalsIgnoreCase(teamName) || awayTeam.equalsIgnoreCase(teamName);
    }

    @Override
    public String toString() {
        return homeTeam + " vs " + awayTeam + "  " + score;
    }
}
