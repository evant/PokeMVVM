package me.tatarka.pokemvvm.pokedex;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import me.tatarka.loader.Result;
import me.tatarka.pokemvvm.api.PokemonItem;
import okhttp3.HttpUrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PokedexViewModelTest {

    PokedexViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new PokedexViewModel();
    }

    @Test
    public void startsEmpty() {
        assertThat(viewModel.items()).isEmpty();
    }

    @Test
    public void loadingWhileEmptyStaysEmpty() {
        viewModel.startLoading();
        assertThat(viewModel.items()).isEmpty();
    }

    @Test
    public void addsItemAsViewModel() {
        viewModel.addItems(Result.success(new PagedResult<>(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0)));

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")))
        );
    }

    @Test
    public void loadingWithItemsAddsLoadingToEnd() {
        viewModel.startLoading();
        viewModel.addItems(Result.success(new PagedResult<>(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0)));

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))),
                viewModel.loading
        );
    }

    @Test
    public void stopLoadingRemovesBottomLoadingIndicator() {
        viewModel.startLoading();
        viewModel.addItems(Result.success(new PagedResult<>(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0)));
        viewModel.stopLoading();

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")))
        );
    }

    @Test
    public void scrollingToLastItemRequestsNextPage() {
        PokedexViewModel.Callbacks callbacks = mock(PokedexViewModel.Callbacks.class);
        viewModel.setCallbacks(callbacks);
        viewModel.startLoading();
        viewModel.addItems(Result.success(new PagedResult<>(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0)));
        viewModel.onScrolled(1);

        verify(callbacks).onRequestNextPage();
    }

    @Test
    public void errorWhenEmptyIsEmptyWithErrorState() {
        viewModel.startLoading();
        viewModel.addItems(Result.<PagedResult<PokemonItem>>error(new Exception()));
        viewModel.stopLoading();

        assertThat(viewModel.items()).isEmpty();
        assertThat(viewModel.getState()).isEqualTo(PokedexViewModel.State.ERROR);
    }

    @Test
    public void errorWithItemsHasErrorItemAndErrorState() {
        viewModel.startLoading();
        viewModel.addItems(Result.success(new PagedResult<>(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0)));
        viewModel.addItems(Result.<PagedResult<PokemonItem>>error(new Exception()));
        viewModel.stopLoading();

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))),
                viewModel.error
        );
        assertThat(viewModel.getState()).isEqualTo(PokedexViewModel.State.ERROR);
    }

    @Test
    public void retryErrorWithItemsReplacesErrorWithLoading() {
        PokedexViewModel.Callbacks callbacks = mock(PokedexViewModel.Callbacks.class);
        viewModel.setCallbacks(callbacks);
        viewModel.startLoading();
        viewModel.addItems(Result.success(new PagedResult<>(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0)));
        viewModel.addItems(Result.<PagedResult<PokemonItem>>error(new Exception()));
        viewModel.stopLoading();
        viewModel.retry();

        verify(callbacks).onRequestRetry();
        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))),
                viewModel.loading
        );
    }
}