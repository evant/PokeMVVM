package me.tatarka.pokemvvm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import me.tatarka.loader.LoaderManager;
import me.tatarka.retainstate.RetainState;
import me.tatarka.retainstate.fragment.RetainStateFragment;

public class BaseFragment extends Fragment implements RetainState.Provider {
    private RetainState retainState;
    private LoaderManager loaderManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retainState = RetainState.from(getHost()).retain(RetainStateFragment.getId(this), RetainState.CREATE);
        loaderManager = retainState.retain(R.id.loader_manager, LoaderManager.CREATE);
    }

    public LoaderManager loaderManager() {
        if (loaderManager == null) {
            throw new IllegalStateException("LoaderManager has not yet been initialized");
        }
        return loaderManager;
    }

    @Override
    public RetainState getRetainState() {
        if (retainState == null) {
            throw new IllegalStateException("RetainState has not yet been initialized");
        }
        return retainState;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loaderManager != null && isRemoving()) {
            loaderManager.detach();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loaderManager != null) {
            if (getActivity().isFinishing() || isRemoving()) {
                RetainState.from(getHost()).remove(RetainStateFragment.getId(this));
                loaderManager.destroy();
            } else {
                loaderManager.detach();
            }
        }
    }
}
