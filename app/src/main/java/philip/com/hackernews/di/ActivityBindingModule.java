package philip.com.hackernews.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import philip.com.hackernews.mvvm.view.main.MainActivity;
import philip.com.hackernews.mvvm.view.main.MainModule;

@Module
abstract class ActivityBindingModule {
    @Binds
    abstract Context bindContext(Application application);

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();
}