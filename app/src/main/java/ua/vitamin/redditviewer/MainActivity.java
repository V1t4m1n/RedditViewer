package ua.vitamin.redditviewer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ua.vitamin.redditviewer.adapters.PostsRecyclerViewAdapter;
import ua.vitamin.redditviewer.callback.Callable;
import ua.vitamin.redditviewer.dto.Post;
import ua.vitamin.redditviewer.requests.RequestTask;

public class MainActivity extends AppCompatActivity implements Callable {

    private RequestTask requestTask;
    private RecyclerView listPostsRecyclerView;
    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private final String BASE_URL = "https://www.reddit.com/top.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter();
        listPostsRecyclerView = findViewById(R.id.listPostsRecyclerView);
        listPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listPostsRecyclerView.setAdapter(postsRecyclerViewAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestTask = new RequestTask(BASE_URL, this);
        requestTask.execute();
    }

    @Override
    public void setAdapter(List<Post> posts) {
        if (posts != null && posts.size() > 0)
            listPostsRecyclerView.setAdapter(new PostsRecyclerViewAdapter(posts));
        else
            listPostsRecyclerView.setAdapter(new PostsRecyclerViewAdapter(onGenerateFakeData(1)));
    }

    private List<Post> onGenerateFakeData(int count) {
        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Post item = new Post();
            item.setAuthor("Author " + i);
            item.setCommentsCount("Comments count " + i);
            item.setDateAdded("Added data " + i);
            item.setThumbnail("https://refactoring.guru/images/content-public/logos/logo-new.png");
            posts.add(item);
        }
        return posts;
    }


}