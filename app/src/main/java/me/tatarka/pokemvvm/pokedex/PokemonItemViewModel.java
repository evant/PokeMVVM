package me.tatarka.pokemvvm.pokedex;

import android.support.annotation.NonNull;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModel;
import me.tatarka.pokemvvm.BR;
import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.api.PokemonItem;

public class PokemonItemViewModel implements ItemViewModel {

    private final PokemonItem item;

    public PokemonItemViewModel(@NonNull PokemonItem item) {
        this.item = item;
    }

    public String name() {
        return item.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PokemonItemViewModel that = (PokemonItemViewModel) o;
        return item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }

    @Override
    public void itemView(ItemView itemView) {
        itemView.set(BR.item, R.layout.pokedex_item);
    }
}
