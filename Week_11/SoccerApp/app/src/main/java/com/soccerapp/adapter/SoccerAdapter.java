package com.soccerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soccerapp.R;
import com.soccerapp.model.SoccerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A generic RecyclerView adapter that can display any {@link SoccerEntity}.
 *
 * <p>Lambda use: the {@code primaryMapper} and {@code secondaryMapper} are
 * {@link Function} lambdas passed by the caller to map an entity to display strings,
 * keeping this adapter completely reusable across Teams, Players, and Matches.</p>
 *
 * @param <T> the entity type to display
 */
public class SoccerAdapter<T extends SoccerEntity> extends RecyclerView.Adapter<SoccerAdapter.ViewHolder> {

    private List<T> items;
    private final Function<T, String> primaryMapper;    // Lambda: entity -> primary text
    private final Function<T, String> secondaryMapper;  // Lambda: entity -> secondary text
    private OnItemClickListener<T> clickListener;

    /**
     * @param items           the data list
     * @param primaryMapper   lambda: T -> primary display text (e.g. entity.getName())
     * @param secondaryMapper lambda: T -> secondary display text (subtitle row)
     */
    public SoccerAdapter(List<T> items,
                         Function<T, String> primaryMapper,
                         Function<T, String> secondaryMapper) {
        this.items           = new ArrayList<>(items);
        this.primaryMapper   = primaryMapper;
        this.secondaryMapper = secondaryMapper;
    }

    // -----------------------------------------------------------------------
    // Click-listener interface (callback lambda pattern)
    // -----------------------------------------------------------------------

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    /** Registers a click listener lambda, e.g. {@code adapter.setOnItemClickListener(item -> ...)}. */
    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }

    // -----------------------------------------------------------------------
    // RecyclerView.Adapter overrides
    // -----------------------------------------------------------------------

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_soccer_entity, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = items.get(position);
        // Apply the caller-supplied lambdas to map entity fields to text views
        holder.tvPrimary.setText(primaryMapper.apply(item));
        holder.tvSecondary.setText(secondaryMapper.apply(item));

        // Lambda-style click callback (#4 – click handler with lambda)
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // -----------------------------------------------------------------------
    // Data update helper
    // -----------------------------------------------------------------------

    /** Replaces the current data set and notifies the adapter. */
    public void updateItems(List<T> newItems) {
        this.items = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    // -----------------------------------------------------------------------
    // ViewHolder
    // -----------------------------------------------------------------------

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvPrimary;
        final TextView tvSecondary;

        ViewHolder(View itemView) {
            super(itemView);
            tvPrimary   = itemView.findViewById(R.id.tvPrimary);
            tvSecondary = itemView.findViewById(R.id.tvSecondary);
        }
    }
}
