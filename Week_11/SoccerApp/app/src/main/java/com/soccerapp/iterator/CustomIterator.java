package com.soccerapp.iterator;

/**
 * Generic custom iterator interface as shown in the UML diagram.
 * Mirrors java.util.Iterator but is our own contract.
 *
 * @param <T> the element type
 */
public interface CustomIterator<T> {
    /** Returns true if there are more elements to iterate over. */
    boolean hasNext();

    /** Returns the next element in the iteration. */
    T next();
}
