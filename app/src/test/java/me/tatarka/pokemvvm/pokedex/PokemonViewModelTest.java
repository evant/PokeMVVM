package me.tatarka.pokemvvm.pokedex;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import me.tatarka.loader.Result;
import me.tatarka.pokemvvm.api.Pokemon;
import me.tatarka.pokemvvm.log.EmptyTree;
import me.tatarka.pokemvvm.pokemon.PokemonViewModel;
import me.tatarka.pokemvvm.viewmodel.State;
import okhttp3.HttpUrl;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class PokemonViewModelTest {

    PokemonViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new PokemonViewModel(new EmptyTree());
    }

    @Test
    public void startsLoadingAndEmpty() {
        assertThat(viewModel.getState()).isEqualTo(State.LOADING);
        assertThat(viewModel.getPokemon()).isNull();
    }

    @Test
    public void setsPokemonAndFinishesLoading() {
        viewModel.setPokemon(Result.success(Pokemon.create(0, "bulbasuar", 7, 69,
                Pokemon.Sprites.create(HttpUrl.parse("http://url")),
                Arrays.asList(
                        Pokemon.StatEntry.create(45, Pokemon.Stat.create("speed")),
                        Pokemon.StatEntry.create(65, Pokemon.Stat.create("special-defense")),
                        Pokemon.StatEntry.create(65, Pokemon.Stat.create("special-attack")),
                        Pokemon.StatEntry.create(49, Pokemon.Stat.create("defense")),
                        Pokemon.StatEntry.create(49, Pokemon.Stat.create("attack")),
                        Pokemon.StatEntry.create(45, Pokemon.Stat.create("hp"))
                ),
                Arrays.asList(
                        Pokemon.TypeEntry.create(1, Pokemon.Type.create("grass")), 
                        Pokemon.TypeEntry.create(2, Pokemon.Type.create("poison"))))));
        assertThat(viewModel.getState()).isEqualTo(State.LOADED);
        assertThat(viewModel.getPokemon()).isNotNull();
    }

    @Test
    public void setsErrorState() {
        viewModel.setPokemon(Result.<Pokemon>error(new Exception()));
        assertThat(viewModel.getState()).isEqualTo(State.ERROR);
    }
}
