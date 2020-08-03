package ua.vitamin.redditviewer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;

import ua.vitamin.redditviewer.requests.RequestTask;

public class MainActivity extends AppCompatActivity {

    private TextView view;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.textView);
        new RequestTask("https://www.reddit.com/top.json", view).execute();
    }

    public void SetUp(String result) {
        view.setText(result);
    }
}