package me.tatarka.pokemvvm.dagger;

import javax.inject.Singleton;

import dagger.Component;
import me.tatarka.pokemvvm.api.ApiModule;
import me.tatarka.pokemvvm.log.LogModule;
import me.tatarka.pokemvvm.pokedex.PokedexRetainedComponent;
import me.tatarka.pokemvvm.pokemon.PokemonComponent;

@Singleton
@Component(modules = {ApiModule.class, LogModule.class})
public interface SingletonComponent {

    PokedexRetainedComponent retainedPokedex();

    PokemonComponent pokemon();
}
