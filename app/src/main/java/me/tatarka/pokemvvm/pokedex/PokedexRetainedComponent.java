package me.tatarka.pokemvvm.pokedex;

import dagger.Subcomponent;
import me.tatarka.pokemvvm.dagger.RetainedScope;

@RetainedScope
@Subcomponent
public interface PokedexRetainedComponent {
    PokedexComponent pokedex();
}
