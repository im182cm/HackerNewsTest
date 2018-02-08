package philip.com.hackernews.mvvm.view.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import philip.com.hackernews.di.FragmentScoped;

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 */
@Module
public abstract class MainModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract PostFragment postFragment();

}
