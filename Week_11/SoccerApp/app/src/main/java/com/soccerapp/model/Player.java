package com.soccerapp.model;

import java.util.UUID;

/**
 * Represents a soccer player. Implements SoccerEntity.
 */
public class Player implements SoccerEntity {

    private final String id;
    private String name;
    private int age;
    private String nationality;
    private String position;
    private String team;
    private int jerseyNumber;

    public Player(String name, int age, String nationality, String position, String team, int jerseyNumber) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Player name cannot be empty");
        if (age < 1 || age > 60) throw new IllegalArgumentException("Invalid player age: " + age);
        if (jerseyNumber < 1 || jerseyNumber > 99) throw new IllegalArgumentException("Jersey number must be 1-99");
        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.age = age;
        this.nationality = nationality != null ? nationality.trim() : "Unknown";
        this.position = position != null ? position.trim() : "Unknown";
        this.team = team != null ? team.trim() : "Free Agent";
        this.jerseyNumber = jerseyNumber;
    }

    @Override public String getId()    { return id; }
    @Override public String getName()  { return name; }
    public int    getAge()             { return age; }
    public String getNationality()     { return nationality; }
    public String getPosition()        { return position; }
    public String getTeam()            { return team; }
    public int    getJerseyNumber()    { return jerseyNumber; }

    public void setTeam(String team)   { this.team = team; }
    public void setAge(int age)        { this.age = age; }

    @Override
    public String toString() {
        return name + " #" + jerseyNumber + " – " + position;
    }
}
