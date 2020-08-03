package ua.vitamin.redditviewer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.vitamin.redditviewer.adapters.PostsRecyclerViewAdapter;
import ua.vitamin.redditviewer.dto.Post;
import ua.vitamin.redditviewer.requests.RequestTask;

public class MainActivity extends AppCompatActivity {

    private RequestTask requestTask;
    private RecyclerView listPostsRecyclerView;
    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;

    private void onInitRecyclerView() {
        List<Post> empt = new ArrayList<>();
        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter(empt);
        listPostsRecyclerView = findViewById(R.id.listPostsRecyclerView);
        listPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listPostsRecyclerView.setAdapter(postsRecyclerViewAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onInitRecyclerView();

        requestTask = new RequestTask("https://www.reddit.com/top.json");


    }

    @Override
    protected void onResume() {
        super.onResume();

        requestTask.execute();
    }
}