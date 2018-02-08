package philip.com.hackernews.mvvm.view.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

interface PostRecyclerListener {
    void onClick(@NonNull String url, @Nullable String by, @Nullable int[] kids, int parent);
}
