package me.tatarka.pokemvvm.pokedex;

import me.tatarka.bindingcollectionadapter.ItemBinding;
import me.tatarka.bindingcollectionadapter.itembindings.ItemBindingModel;
import me.tatarka.pokemvvm.R;

/**
 * Represents an item that just shows a loading indicator.
 */
public class LoadingItemViewModel implements ItemBindingModel {
    @Override
    public void onItemBind(ItemBinding itemBinding) {
        itemBinding.set(ItemBinding.VAR_NONE, R.layout.pokedex_item_loading);
    }
}
