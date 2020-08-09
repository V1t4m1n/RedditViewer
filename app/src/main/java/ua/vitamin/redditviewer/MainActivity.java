package ua.vitamin.redditviewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ua.vitamin.redditviewer.adapters.PostsRecyclerViewAdapter;
import ua.vitamin.redditviewer.databinding.ActivityMainBinding;
import ua.vitamin.redditviewer.dto.Post;
import ua.vitamin.redditviewer.requests.LoadPostsTask;

public class MainActivity extends AppCompatActivity implements LoadPostsTask.ResponseHandler {

    private LoadPostsTask loadPostsTask;
    private RecyclerView listPostsRecyclerView;
    private PostsRecyclerViewAdapter postsRecyclerViewAdapter;
    private final String redditURL = "https://www.reddit.com/top.json";
    private ActivityMainBinding binding;
    private View content;
    private List<Post> savedList;
    private boolean isScreenInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

            ArrayList<String>authorList;
            ArrayList<String>timeList;
            ArrayList<String>commentsList;
            ArrayList<String>thumbnailList;

            isScreenInitialized = savedInstanceState.getBoolean("isScreenInitialized");
            authorList = savedInstanceState.getStringArrayList("authorList");
            timeList = savedInstanceState.getStringArrayList("timeList");
            commentsList = savedInstanceState.getStringArrayList("commentsList");
            thumbnailList = savedInstanceState.getStringArrayList("thumbnailList");

            savedList = new ArrayList<>();

            for (int index = 0; index < 25; index++) {
                Post item = new Post();

                item.setAuthor(authorList.get(index));
                item.setDateAdded(timeList.get(index));
                item.setCommentsCount(commentsList.get(index));
                item.setThumbnail(thumbnailList.get(index));
                savedList.add(item);
            }
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        content = binding.getRoot();
        setContentView(content);

        postsRecyclerViewAdapter = new PostsRecyclerViewAdapter();
        listPostsRecyclerView = findViewById(R.id.listPostsRecyclerView);
        listPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listPostsRecyclerView.setAdapter(postsRecyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isScreenInitialized) {
            loadPostsTask = new LoadPostsTask(redditURL, this);
            loadPostsTask.execute();
        } else {
            if (savedList != null && savedList.size() > 0) {
                Log.d("RESULTS_SIZE", String.valueOf(savedList.size()));
                listPostsRecyclerView.setAdapter(new PostsRecyclerViewAdapter(savedList, getSupportFragmentManager(), getApplicationContext()));
            } else {
                listPostsRecyclerView.setAdapter(new PostsRecyclerViewAdapter(onGenerateFakeData(1)));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        ArrayList<String>authorList = new ArrayList<>();
        ArrayList<String>timeList = new ArrayList<>();
        ArrayList<String>commentsList = new ArrayList<>();
        ArrayList<String>thumbnailList = new ArrayList<>();

        for (Post item: savedList) {
            authorList.add(item.getAuthor());
            timeList.add(item.getDateAdded());
            commentsList.add(item.getCommentsCount());
            thumbnailList.add(item.getThumbnail());
        }

        outState.putBoolean("isScreenInitialized", isScreenInitialized);
        outState.putStringArrayList("authorList",authorList);
        outState.putStringArrayList("timeList",timeList);
        outState.putStringArrayList("commentsList",commentsList);
        outState.putStringArrayList("thumbnailList",thumbnailList);

        super.onSaveInstanceState(outState);
    }

    private List<Post> onGenerateFakeData(int count) {
        List<Post> posts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Post item = new Post();
            item.setAuthor("Author " + i);
            item.setCommentsCount("Comments count " + i);
            item.setDateAdded("Date Added " + i);
            item.setThumbnail("https://refactoring.guru/images/content-public/logos/logo-new.png");
            posts.add(item);
        }
        return posts;
    }

    @Override
    public void onResponse(List<Post> posts) {
        if (posts != null && posts.size() > 0) {
            Log.d("RESULTS_SIZE", String.valueOf(posts.size()));
            listPostsRecyclerView.setAdapter(new PostsRecyclerViewAdapter(posts, getSupportFragmentManager(), getApplicationContext()));
            savedList = posts;
            isScreenInitialized = true;
        } else {
            onError(new Error("Error!"));
        }
    }

    @Override
    public void onError(Error error) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Error")
                .setMessage(error.getMessage())
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}