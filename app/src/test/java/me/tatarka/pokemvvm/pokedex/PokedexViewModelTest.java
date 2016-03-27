package me.tatarka.pokemvvm.pokedex;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.tatarka.loader.Result;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.log.EmptyTree;
import me.tatarka.pokemvvm.viewmodel.State;
import okhttp3.HttpUrl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PokedexViewModelTest {

    PokedexViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new PokedexViewModel(new EmptyTree());
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
        viewModel.addItems(PagedResult.success(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0));

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")), viewModel)
        );
    }

    @Test
    public void loadingWithItemsAddsLoadingToEnd() {
        viewModel.startLoading();
        viewModel.addItems(PagedResult.success(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0));

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")), viewModel),
                viewModel.loading
        );
    }

    @Test
    public void stopLoadingRemovesBottomLoadingIndicator() {
        viewModel.startLoading();
        viewModel.addItems(PagedResult.success(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0));
        viewModel.stopLoading();

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")), viewModel)
        );
    }

    @Test
    public void scrollingToLastItemRequestsNextPage() {
        PokedexViewModel.Callbacks callbacks = mock(PokedexViewModel.Callbacks.class);
        viewModel.setCallbacks(callbacks);
        viewModel.startLoading();
        viewModel.addItems(PagedResult.success(Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ), 0));
        viewModel.onScrolled(1);

        verify(callbacks).onRequestNextPage();
    }

    @Test
    public void errorWhenEmptyIsEmptyWithErrorState() {
        viewModel.startLoading();
        viewModel.addItems(PagedResult.error(Collections.<PokemonItem>emptyList(), new Exception()));
        viewModel.stopLoading();

        assertThat(viewModel.items()).isEmpty();
        assertThat(viewModel.getState()).isEqualTo(State.ERROR);
    }

    @Test
    public void errorWithItemsHasErrorItemAndErrorState() {
        viewModel.startLoading();
        List<PokemonItem> items = Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        );
        viewModel.addItems(PagedResult.success(items, 0));
        viewModel.addItems(PagedResult.error(items, new Exception()));
        viewModel.stopLoading();

        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")), viewModel),
                viewModel.error
        );
        assertThat(viewModel.getState()).isEqualTo(State.ERROR);
    }

    @Test
    public void retryErrorWithItemsReplacesErrorWithLoading() {
        PokedexViewModel.Callbacks callbacks = mock(PokedexViewModel.Callbacks.class);
        viewModel.setCallbacks(callbacks);
        viewModel.startLoading();
        List<PokemonItem> items = Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        );
        viewModel.addItems(PagedResult.success(items, 0));
        viewModel.addItems(PagedResult.error(items, new Exception()));
        viewModel.stopLoading();
        viewModel.retry();

        verify(callbacks).onRequestRetry();
        assertThat(viewModel.items()).containsExactly(
                new PokemonItemViewModel(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")), viewModel),
                viewModel.loading
        );
    }
}