package me.tatarka.pokemvvm.pokedex;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import me.tatarka.pokemvvm.api.Page;
import me.tatarka.pokemvvm.api.PokeService;
import me.tatarka.pokemvvm.api.PokemonItem;
import okhttp3.HttpUrl;
import rx.Single;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PokedexPagerTest {

    @Test
    public void immediatelyReturnsFirstPage() throws Exception {
        PokeService service = mock(PokeService.class);
        when(service.pokemon()).thenReturn(Single.just(Page.create(0, null, null, Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ))));
        PokedexPager pager = new PokedexPager(service);
        TestSubscriber<List<PokemonItem>> subscriber = TestSubscriber.create();
        pager.pokemon().subscribe(subscriber);

        subscriber.assertReceivedOnNext(Arrays.asList(
                Arrays.asList(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")))
        ));
        subscriber.assertCompleted();
    }

    @Test
    public void returnsNextPageWhenAsked() throws Exception {
        PokeService service = mock(PokeService.class);
        when(service.pokemon()).thenReturn(Single.just(Page.create(0, HttpUrl.parse("http://2/"), null, Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ))));
        when(service.nextPokemon(eq("http://2/"))).thenReturn(Single.just(Page.create(0, null, HttpUrl.parse("http://1/"), Arrays.asList(
                PokemonItem.create("ivysaur", HttpUrl.parse("http://url"))
        ))));
        PokedexPager pager = new PokedexPager(service);
        TestSubscriber<List<PokemonItem>> subscriber = TestSubscriber.create();
        pager.pokemon().subscribe(subscriber);
        pager.requestNextPage();

        subscriber.assertReceivedOnNext(Arrays.asList(
                Arrays.asList(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))),
                Arrays.asList(PokemonItem.create("ivysaur", HttpUrl.parse("http://url")))
        ));
        subscriber.assertCompleted();
    }

    @Test
    public void onFailureAResubscriptionContinuesWhereItLeftOff() {
        PokeService service = mock(PokeService.class);
        when(service.pokemon()).thenReturn(Single.just(Page.create(0, HttpUrl.parse("http://2/"), null, Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ))));
        when(service.nextPokemon(eq("http://2/"))).thenReturn(Single.<Page<PokemonItem>>error(new IOException()));
        PokedexPager pager = new PokedexPager(service);
        TestSubscriber<List<PokemonItem>> subscriber = TestSubscriber.create();
        pager.pokemon().subscribe(subscriber);
        pager.requestNextPage();

        subscriber.assertReceivedOnNext(Arrays.asList(
                Arrays.asList(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")))
        ));
        subscriber.assertError(IOException.class);

        when(service.nextPokemon(eq("http://2/"))).thenReturn(Single.just(Page.create(0, null, HttpUrl.parse("http://1/"), Arrays.asList(
                PokemonItem.create("ivysaur", HttpUrl.parse("http://url"))
        ))));
        subscriber = TestSubscriber.create();
        pager.pokemon().subscribe(subscriber);

        subscriber.assertReceivedOnNext(Arrays.asList(
                Arrays.asList(PokemonItem.create("ivysaur", HttpUrl.parse("http://url")))
        ));
        subscriber.assertCompleted();
    }

    @Test
    public void settingNextPageToNulLResetsToFirstPage() {
        PokeService service = mock(PokeService.class);
        when(service.pokemon()).thenReturn(Single.just(Page.create(0, HttpUrl.parse("http://2/"), null, Arrays.asList(
                PokemonItem.create("bulbasaur", HttpUrl.parse("http://url"))
        ))));
        when(service.nextPokemon(eq("http://2/"))).thenReturn(Single.just(Page.create(0, null, HttpUrl.parse("http://1/"), Arrays.asList(
                PokemonItem.create("ivysaur", HttpUrl.parse("http://url"))
        ))));
        PokedexPager pager = new PokedexPager(service);
        TestSubscriber<List<PokemonItem>> subscriber = TestSubscriber.create();
        pager.pokemon().subscribe(subscriber);
        pager.requestNextPage();
        pager.setNextPage(null);
        subscriber = TestSubscriber.create();
        pager.pokemon().subscribe(subscriber);

        subscriber.assertReceivedOnNext(Arrays.asList(
                Arrays.asList(PokemonItem.create("bulbasaur", HttpUrl.parse("http://url")))
        ));
    }
}