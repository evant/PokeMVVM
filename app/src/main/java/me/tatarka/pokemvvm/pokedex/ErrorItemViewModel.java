package me.tatarka.pokemvvm.pokedex;

import com.android.databinding.library.baseAdapters.BR;

import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModel;
import me.tatarka.pokemvvm.R;

public class ErrorItemViewModel implements ItemViewModel {

    private final OnRetryListener listener;

    public ErrorItemViewModel(OnRetryListener listener) {
        this.listener = listener;
    }

    public void retry() {
        listener.retry();
    }

    @Override
    public void itemView(ItemView itemView) {
        itemView.set(BR.item, R.layout.pokedex_item_error);
    }

    public interface OnRetryListener {
        void retry();
    }
}
