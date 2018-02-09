package philip.com.hackernews.mvvm.view.main;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import philip.com.hackernews.R;

/**
 * Main Activity. Launcher.
 */
public class MainActivity extends DaggerAppCompatActivity {
    @Inject
    public PostFragment mPostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switchToPostFragment();
    }

    private void switchToPostFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.layout_contentFrame, mPostFragment).commit();
    }
}
