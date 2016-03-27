package me.tatarka.pokemvvm.pokedex;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import me.tatarka.loader.Loader;
import me.tatarka.pokemvvm.BaseFragment;
import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.api.RxPagedLoader;
import me.tatarka.pokemvvm.dagger.Dagger;
import me.tatarka.pokemvvm.databinding.PokedexFragmentBinding;

/**
 * Displays a paginated lists of all the firstPokemonPage.
 */
public class PokedexFragment extends BaseFragment implements Loader.Callbacks<PagedResult<PokemonItem>>, PokedexViewModel.Callbacks {

    public static PokedexFragment newInstance() {
        return new PokedexFragment();
    }

    @Inject
    PokedexPager pager;
    @Inject
    PokedexViewModel viewModel;

    private RxPagedLoader<PokemonItem> loader;
    private PokedexFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.pokedex_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dagger.inject(this);

        viewModel.setCallbacks(this);

        loader = loaderManager().init(0, RxPagedLoader.create(pager.pokemon()), this);
        loader.start();

        if (loader.isRunning()) {
            viewModel.startLoading();
        }

        binding.setViewModel(viewModel);
    }

    @Override
    public void onRequestRetry() {
        loader.restart();
    }

    @Override
    public void onRequestNextPage() {
        pager.requestNextPage();
    }

    @Override
    public void onSelectItem(PokemonItem item) {
        ((PokemonItemViewModel.OnSelectListener) getHost()).selectItem(item);
    }

    @Override
    public void onLoaderStart() {

    }

    @Override
    public void onLoaderResult(PagedResult<PokemonItem> result) {
        viewModel.addItems(result);
    }

    @Override
    public void onLoaderComplete() {
        viewModel.stopLoading();
    }

    public void addSharedElements(FragmentTransaction ft) {
        ft.addSharedElement(binding.appBar, "app_bar")
                .addSharedElement(binding.toolbar, "toolbar");
    }
}
