package me.tatarka.pokemvvm.dagger;

import javax.inject.Singleton;

import dagger.Component;
import me.tatarka.pokemvvm.api.ApiModule;
import me.tatarka.pokemvvm.pokedex.PokedexRetainedComponent;

@Singleton
@Component(modules = ApiModule.class)
public interface SingletonComponent {
  
    PokedexRetainedComponent retainedPokedex();
}
