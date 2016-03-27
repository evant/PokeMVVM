package me.tatarka.pokemvvm.pokedex;

import android.content.res.Resources;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModel;
import me.tatarka.pokemvvm.BR;
import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.util.StringUtils;

public class PokemonItemViewModel implements ItemViewModel {

    private final PokemonItem item;
    private final OnSelectListener listener;

    public PokemonItemViewModel(PokemonItem item, OnSelectListener listener) {
        this.item = item;
        this.listener = listener;
    }

    public String name(Resources resources) {
        return resources.getString(R.string.pokemon_name, item.number(), StringUtils.capitalize(item.name()));
    }

    public void select() {
        listener.selectItem(item);
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

    public interface OnSelectListener {
        void selectItem(PokemonItem item);
    }
}
