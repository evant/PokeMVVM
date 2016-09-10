package me.tatarka.pokemvvm.pokedex;

import com.android.databinding.library.baseAdapters.BR;

import me.tatarka.bindingcollectionadapter.ItemBinding;
import me.tatarka.bindingcollectionadapter.itembindings.ItemBindingModel;
import me.tatarka.pokemvvm.R;

public class ErrorItemViewModel implements ItemBindingModel {

    private final OnRetryListener listener;

    public ErrorItemViewModel(OnRetryListener listener) {
        this.listener = listener;
    }

    public void retry() {
        listener.retry();
    }

    @Override
    public void onItemBind(ItemBinding itemBinding) {
        itemBinding.set(BR.item, R.layout.pokedex_item_error);
    }

    public interface OnRetryListener {
        void retry();
    }
}
