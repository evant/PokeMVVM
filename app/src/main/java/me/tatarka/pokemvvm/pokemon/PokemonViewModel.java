package me.tatarka.pokemvvm.pokemon;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import me.tatarka.loader.Result;
import me.tatarka.pokemvvm.BR;
import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.api.Pokemon;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.dagger.ViewScope;
import me.tatarka.pokemvvm.util.StringUtils;
import me.tatarka.pokemvvm.viewmodel.ErrorViewModel;
import me.tatarka.pokemvvm.viewmodel.State;
import timber.log.Timber;

@ViewScope
public class PokemonViewModel extends BaseObservable implements ErrorViewModel {

    private static final SimpleArrayMap<String, Integer> TYPE_COLOR_MAP = new SimpleArrayMap<>();

    static {
        TYPE_COLOR_MAP.put("bug", R.color.light_green);
        TYPE_COLOR_MAP.put("dark", R.color.brown);
        TYPE_COLOR_MAP.put("dragon", R.color.deep_purple);
        TYPE_COLOR_MAP.put("electirc", R.color.yellow);
        TYPE_COLOR_MAP.put("fairy", R.color.pink_accent);
        TYPE_COLOR_MAP.put("fighting", R.color.deep_orange);
        TYPE_COLOR_MAP.put("fire", R.color.red);
        TYPE_COLOR_MAP.put("flying", R.color.teal);
        TYPE_COLOR_MAP.put("ghost", R.color.indigo);
        TYPE_COLOR_MAP.put("grass", R.color.green);
        TYPE_COLOR_MAP.put("ground", R.color.lime);
        TYPE_COLOR_MAP.put("ice", R.color.cyan);
        TYPE_COLOR_MAP.put("normal", R.color.grey);
        TYPE_COLOR_MAP.put("poison", R.color.purple);
        TYPE_COLOR_MAP.put("psychic", R.color.pink);
        TYPE_COLOR_MAP.put("rock", R.color.orange);
        TYPE_COLOR_MAP.put("steel", R.color.blue_grey);
        TYPE_COLOR_MAP.put("water", R.color.blue);
    }

    private final Timber.Tree log;
    private PokemonItem item;
    private Pokemon pokemon;
    private State state = State.LOADING;
    private Callbacks callbacks;

    @Inject
    public PokemonViewModel(Timber.Tree log) {
        this.log = log;
    }

    public void setPokemonItem(PokemonItem item) {
        this.item = item;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void setPokemon(Result<Pokemon> result) {
        if (result.isSuccess()) {
            this.pokemon = result.getSuccess();
            state = State.LOADED;
            notifyPropertyChanged(BR.pokemon);
            notifyPropertyChanged(BR.state);
        } else {
            state = State.ERROR;
            notifyPropertyChanged(BR.state);
            Throwable error = result.getError();
            log.e(error, error.getMessage());
        }
    }

    public String name(Resources resources) {
        return resources.getString(R.string.pokemon_name, item.number(), StringUtils.capitalize(item.name()));
    }

    @Nullable
    @Bindable
    public Pokemon getPokemon() {
        return pokemon;
    }

    @Bindable
    public State getState() {
        return state;
    }

    @Nullable
    public CharSequence heightWeight(Resources resources, @Nullable Pokemon pokemon) {
        return pokemon == null
                ? null
                : resources.getString(R.string.pokemon_height_weight, pokemon.height(), pokemon.weight());
    }

    public List<Chip> chips(Resources resources, @Nullable Pokemon pokemon) {
        if (pokemon == null) {
            return Collections.emptyList();
        }
        List<Pokemon.TypeEntry> types = new ArrayList<>(pokemon.types());
        Collections.sort(types, new Comparator<Pokemon.TypeEntry>() {
            @Override
            public int compare(Pokemon.TypeEntry lhs, Pokemon.TypeEntry rhs) {
                return Integer.compare(lhs.slot(), rhs.slot());
            }
        });
        List<Chip> chips = new ArrayList<>(types.size());
        for (Pokemon.TypeEntry entry : types) {
            Pokemon.Type type = entry.type();
            Integer colorRes = TYPE_COLOR_MAP.get(type.name());
            int color = resources.getColor(colorRes != null ? colorRes : R.color.grey);
            chips.add(new Chip(color, type.name()));
        }
        return chips;
    }

    public List<Row> statData(@Nullable Pokemon pokemon) {
        if (pokemon == null) {
            return Collections.emptyList();
        }
        List<Pokemon.StatEntry> stats = pokemon.stats();
        List<Row> rows = new ArrayList<>(stats.size());
        for (Pokemon.StatEntry entry : stats) {
            Pokemon.Stat stat = entry.stat();
            rows.add(new Row(stat.name(), Integer.toString(entry.baseStat())));
        }
        return rows;
    }

    public void startLoading() {
        state = State.LOADING;
        notifyPropertyChanged(BR.state);
    }

    @Override
    public void retry() {
        startLoading();
        callbacks.onRequestRetry();
    }

    public interface Callbacks {
        void onRequestRetry();
    }
}
