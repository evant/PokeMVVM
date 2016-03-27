package me.tatarka.pokemvvm.dagger;

import android.content.Context;

import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.api.ApiModule;
import me.tatarka.pokemvvm.pokedex.PokedexFragment;
import me.tatarka.pokemvvm.pokedex.PokedexRetainedComponent;
import me.tatarka.pokemvvm.pokemon.PokemonFragment;
import me.tatarka.retainstate.RetainState;

public class Dagger {

    private static SingletonComponent singletonComponent;

    public static SingletonComponent component(Context context) {
        if (singletonComponent == null) {
            singletonComponent = DaggerSingletonComponent.builder()
                    .apiModule(new ApiModule(context))
                    .build();
        }
        return singletonComponent;
    }

    public static void inject(final PokedexFragment fragment) {
        RetainState.from(fragment).retain(R.id.component, new RetainState.OnCreate<PokedexRetainedComponent>() {
            @Override
            public PokedexRetainedComponent onCreate() {
                return component(fragment.getContext()).retainedPokedex();
            }
        }).pokedex().inject(fragment);
    }

    public static void inject(PokemonFragment fragment) {
        component(fragment.getContext()).pokemon().inject(fragment);
    }
}
