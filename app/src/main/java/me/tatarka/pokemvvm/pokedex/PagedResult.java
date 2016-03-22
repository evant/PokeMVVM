package me.tatarka.pokemvvm.pokedex;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Represents a paged result.
 */
public class PagedResult<T> {
    private final List<T> items;
    private final int newItemsStartIndex;

    /**
     * Constructs a new PagedResult with the given items in the index where new items from the last
     * paged result starts.
     */
    public PagedResult(@NonNull List<T> items, int newItemsStartIndex) {
        this.items = items;
        this.newItemsStartIndex = newItemsStartIndex;
    }

    /**
     * Updates the given list with the contents of the paged result. If the given list is empty it
     * will be filled with all the results. Otherwise it is assumed that it has all but the latest
     * results and only those will be added.
     */
    public void consume(@NonNull List<T> out) {
        consume(out, new Func1<T, T>() {
            @Override
            public T call(T t) {
                return t;
            }
        });
    }

    /**
     * Updates the given list with the contents of the paged result. If the given list is empty it
     * will be filled with all the results. Otherwise it is assumed that it has all but the latest
     * results and only those will be added.
     */
    public <R> void consume(@NonNull List<R> out, Func1<T, R> f) {
        List<T> itemsToConsume;
        if (out.isEmpty()) {
            itemsToConsume = items;
        } else {
            itemsToConsume = items.subList(newItemsStartIndex, items.size());
        }
        List<R> newItems = new ArrayList<>(itemsToConsume.size());
        for (T item : itemsToConsume) {
            newItems.add(f.call(item));
        }
        out.addAll(newItems);
    }
}
