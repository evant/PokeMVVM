package me.tatarka.pokemvvm.pokemon;

import dagger.Subcomponent;
import me.tatarka.pokemvvm.dagger.ViewScope;

@ViewScope
@Subcomponent
public interface PokemonComponent {
    void inject(PokemonFragment fragment);

    void inject(TitleView view);
}

