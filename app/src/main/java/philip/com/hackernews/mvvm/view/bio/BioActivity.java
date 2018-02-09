package philip.com.hackernews.mvvm.view.bio;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.Arrays;

import philip.com.hackernews.R;
import philip.com.hackernews.mvvm.model.local.UserEntity;
import philip.com.hackernews.util.Constant;

/**
 * Bio Activity that shows User Information.
 */
public class BioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        TextView textViewId = findViewById(R.id.text_id);
        TextView textViewAbout = findViewById(R.id.text_about);
        TextView textViewKrama = findViewById(R.id.text_karma);
        TextView textViewSubmitted = findViewById(R.id.text_submitted);
        TextView textViewCreated = findViewById(R.id.text_created);

        UserEntity user = getIntent().getParcelableExtra(Constant.EXTRA_USER);
        if (user != null) {
            textViewId.setText(user.getId());
            if (!TextUtils.isEmpty(user.getAbout())) {
                String about = user.getAbout();
                Spanned aboutHtml;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    aboutHtml = Html.fromHtml(about, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    aboutHtml = Html.fromHtml(about);
                }

                textViewAbout.setText(aboutHtml);
            }
            textViewKrama.setText(String.valueOf(user.getKarma()));
            textViewSubmitted.setText(Arrays.toString(user.getSubmitted()));
            textViewCreated.setText(user.getCreated());
        }
    }
}
