package me.tatarka.pokemvvm.pokedex;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import javax.inject.Inject;

import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import me.tatarka.bindingcollectionadapter.collections.MergeObservableList;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModel;
import me.tatarka.bindingcollectionadapter.itemviews.ItemViewModelSelector;
import me.tatarka.pokemvvm.BR;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.dagger.ViewScope;
import rx.functions.Func1;

@ViewScope
public class PokedexViewModel extends BaseObservable {

    private State state;
    private ObservableList<PokemonItemViewModel> pokemonItems = new ObservableArrayList<>();
    private MergeObservableList<ItemViewModel> items = new MergeObservableList<ItemViewModel>()
            .insertList(pokemonItems);
    private Callbacks callbacks;

    @Inject
    public PokedexViewModel() {
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
            items.insertItem(LoadingItemViewModel.create());
        }
    }

    public void stopLoading() {
        state = State.LOADED;
        items.removeItem(LoadingItemViewModel.create());
    }

    public void addItems(PagedResult<PokemonItem> pagedResult) {
        if (state == State.LOADING && pokemonItems.isEmpty()) {
            this.items.insertItem(LoadingItemViewModel.create());
        }
        pagedResult.consume(pokemonItems, new Func1<PokemonItem, PokemonItemViewModel>() {
            @Override
            public PokemonItemViewModel call(PokemonItem pokemonItem) {
                return new PokemonItemViewModel(pokemonItem);
            }
        });
        notifyPropertyChanged(BR.state);
    }

    public void setError() {
        state = State.ERROR;
        notifyPropertyChanged(BR.state);
        this.items.removeItem(LoadingItemViewModel.create());
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
        void onRequestNextPage();
    }
}
