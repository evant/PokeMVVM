package me.tatarka.pokemvvm.pokedex;

import dagger.Subcomponent;
import me.tatarka.pokemvvm.dagger.ViewScope;

@ViewScope
@Subcomponent
public interface PokedexComponent {
    void inject(PokedexFragment fragment);
}
