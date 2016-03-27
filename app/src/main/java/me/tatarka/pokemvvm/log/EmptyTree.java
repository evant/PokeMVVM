package me.tatarka.pokemvvm.log;

import timber.log.Timber;

/**
 * A tree that does nothing, useful for production or tests.
 */
public class EmptyTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {

    }
}
