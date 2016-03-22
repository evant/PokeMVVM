package me.tatarka.pokemvvm.pokedex;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import me.tatarka.loader.Loader;
import me.tatarka.loader.Result;
import me.tatarka.pokemvvm.BaseFragment;
import me.tatarka.pokemvvm.R;
import me.tatarka.pokemvvm.api.PokemonItem;
import me.tatarka.pokemvvm.api.RxPagedLoader;
import me.tatarka.pokemvvm.dagger.Dagger;
import me.tatarka.pokemvvm.databinding.PokedexFragmentBinding;

/**
 * Displays a paginated lists of all the pokemon.
 */
public class PokedexFragment extends BaseFragment implements Loader.Callbacks<Result<PagedResult<PokemonItem>>>, PokedexViewModel.Callbacks {
    private static final String TAG = "PokemonListFragment";

    @Inject
    PokedexPager pager;
    @Inject
    PokedexViewModel viewModel;

    private PokedexFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.pokedex_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dagger.inject(this);

        viewModel.setCallbacks(this);

        RxPagedLoader<PokemonItem> loader = loaderManager().init(0, RxPagedLoader.create(pager.pokemon()), this);
        loader.start();

        if (loader.isRunning()) {
            viewModel.startLoading();
        }

        binding.setViewModel(viewModel);
    }

    @Override
    public void onRequestNextPage() {
        pager.requestNextPage();
    }

    @Override
    public void onLoaderStart() {

    }

    @Override
    public void onLoaderResult(Result<PagedResult<PokemonItem>> result) {
        if (result.isSuccess()) {
            PagedResult<PokemonItem> pagedResult = result.getSuccess();
            viewModel.addItems(pagedResult);
        } else {
            Throwable error = result.getError();
            viewModel.setError();
            Log.e(TAG, error.getMessage(), error);
        }
    }

    @Override
    public void onLoaderComplete() {
        viewModel.stopLoading();
    }
}
