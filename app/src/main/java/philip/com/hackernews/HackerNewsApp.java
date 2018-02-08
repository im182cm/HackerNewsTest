package philip.com.hackernews;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import philip.com.hackernews.di.DaggerAppComponent;

public class HackerNewsApp extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
