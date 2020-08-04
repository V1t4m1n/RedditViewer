package ua.vitamin.redditviewer.allerts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import ua.vitamin.redditviewer.R;
import ua.vitamin.redditviewer.databinding.ActivityMainBinding;

public class FullScreenImageActivity extends AppCompatActivity {

    private ImageView fullScreenImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_dialog);

        String url = getIntent().getStringExtra("url_image");

        fullScreenImageView = findViewById(R.id.fullScreenImageView);
        Picasso.get().load(url).into(fullScreenImageView);
    }
}
