package com.soccerapp.model;

/**
 * Interface that all soccer domain entities must implement.
 * Acts as a bounded type constraint for generic classes.
 */
public interface SoccerEntity {
    String getId();
    String getName();
}
