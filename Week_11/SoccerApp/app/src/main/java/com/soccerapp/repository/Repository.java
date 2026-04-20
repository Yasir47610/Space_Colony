package com.soccerapp.repository;

import com.soccerapp.model.SoccerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generic repository that can manage any collection of SoccerEntity objects.
 *
 * <p>Bounded type parameter {@code <T extends SoccerEntity>} ensures that only
 * proper domain entities are stored here, giving us access to getId() and getName()
 * without needing unchecked casts.</p>
 *
 * <p>Lambda usage:
 *   <ul>
 *     <li>{@link #filter(Predicate)} – caller passes a lambda Predicate, e.g.
 *         {@code repo.filter(t -> t.getLeague().equals("La Liga"))}</li>
 *     <li>Internal stream pipeline in {@link #filter(Predicate)} uses a method-reference
 *         lambda to collect results.</li>
 *   </ul>
 * </p>
 *
 * @param <T> the entity type, must implement SoccerEntity
 */
public class Repository<T extends SoccerEntity> {

    // The backing store – kept private to enforce access through the API
    private final List<T> items;

    public Repository() {
        this.items = new ArrayList<>();
    }

    public Repository(List<T> initialItems) {
        if (initialItems == null) throw new IllegalArgumentException("Initial list cannot be null");
        this.items = new ArrayList<>(initialItems);
    }

    // -----------------------------------------------------------------------
    // Core CRUD operations
    // -----------------------------------------------------------------------

    /**
     * Adds an item to the repository.
     *
     * @throws IllegalArgumentException if item is null
     */
    public void add(T item) {
        if (item == null) throw new IllegalArgumentException("Cannot add a null item to the repository");
        items.add(item);
    }

    /**
     * Returns a defensive copy of all items in the repository.
     */
    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    /**
     * Returns the number of items in the repository.
     */
    public int size() {
        return items.size();
    }

    /**
     * Removes all items from the repository.
     */
    public void clear() {
        items.clear();
    }

    // -----------------------------------------------------------------------
    // Lambda-powered filtering  (Lambda use #1 in the architecture)
    // -----------------------------------------------------------------------

    /**
     * Filters the repository using a Predicate lambda and returns a new list
     * containing only the matching items.
     *
     * <p>Typical caller usage with a lambda:
     * <pre>
     *   List&lt;Team&gt; laLiga = teamRepo.filter(t -&gt; t.getLeague().equals("La Liga"));
     * </pre>
     * </p>
     *
     * @param predicate a lambda or method reference that returns true for items to keep
     * @return a new list of matching items (never null)
     */
    public List<T> filter(Predicate<T> predicate) {
        if (predicate == null) return getAll();
        // Stream + lambda pipeline – demonstrates generics + lambdas together
        return items.stream()
                .filter(predicate)          // lambda Predicate passed in by the caller
                .collect(Collectors.toList());
    }

    /**
     * Convenience method: find by name (case-insensitive, partial match).
     * Internally delegates to {@link #filter(Predicate)} with a lambda.
     */
    public List<T> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) return getAll();
        String lower = query.toLowerCase().trim();
        // Lambda #1b – inline lambda passed to filter()
        return filter(item -> item.getName().toLowerCase().contains(lower));
    }

    /**
     * Finds a single item by its exact ID.
     *
     * @return the item, or null if not found
     */
    public T findById(String id) {
        if (id == null) return null;
        return items.stream()
                .filter(item -> item.getId().equals(id))   // lambda inside stream
                .findFirst()
                .orElse(null);
    }
}
