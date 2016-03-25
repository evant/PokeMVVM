package me.tatarka.pokemvvm.pokedex;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.VisibleForTesting;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.collections.MergeObservableList;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModel;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModelSelector;
import me.tatarka.loader.Result;
import me.tatarka.pokemvvm.BR;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.dagger.ViewScope;
import rx.functions.Func1;

@ViewScope
public class PokedexViewModel extends BaseObservable implements ErrorItemViewModel.OnRetryListener {
    private static final String TAG = "PokedexViewModel";

    @VisibleForTesting
    final LoadingItemViewModel loading;
    @VisibleForTesting
    final ErrorItemViewModel error;

    private State state;
    private ObservableList<PokemonItemViewModel> pokemonItems = new ObservableArrayList<>();
    private MergeObservableList<ItemViewModel> items = new MergeObservableList<ItemViewModel>()
            .insertList(pokemonItems);
    private Callbacks callbacks;

    @Inject
    public PokedexViewModel() {
        loading = new LoadingItemViewModel();
        error = new ErrorItemViewModel(this);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public ObservableList<ItemViewModel> items() {
        return items;
    }

    @Bindable
    public State getState() {
        return state;
    }

    public void startLoading() {
        state = State.LOADING;
        notifyPropertyChanged(BR.state);
        if (!pokemonItems.isEmpty()) {
            items.insertItem(loading);
        }
    }

    public void stopLoading() {
        if (state != State.LOADING) {
            return;
        }
        state = State.LOADED;
        items.removeItem(loading);
    }

    @Override
    public void retry() {
        items.removeItem(error);
        startLoading();
        callbacks.onRequestRetry();
    }

    public void addItems(Result<PagedResult<PokemonItem>> result) {
        if (result.isSuccess()) {
            if (state == State.LOADING && pokemonItems.isEmpty()) {
                this.items.insertItem(loading);
            }
            result.getSuccess().consume(pokemonItems, new Func1<PokemonItem, PokemonItemViewModel>() {
                @Override
                public PokemonItemViewModel call(PokemonItem pokemonItem) {
                    return new PokemonItemViewModel(pokemonItem);
                }
            });
            notifyPropertyChanged(BR.state);
        } else {
            this.items.removeItem(loading);
            if (!items.isEmpty()) {
                this.items.insertItem(error);
            }
            state = State.ERROR;
            notifyPropertyChanged(BR.state);
        }
    }

    public void onScrolled(int lastVisiblePosition) {
        if (state == State.LOADING && lastVisiblePosition >= items.size() - 1) {
            callbacks.onRequestNextPage();
        }
    }

    public final ItemViewSelector<ItemViewModel> itemView = new ItemViewModelSelector<>();

    public enum State {
        LOADING, LOADED, ERROR
    }

    public interface Callbacks {
        void onRequestRetry();

        void onRequestNextPage();
    }
}
