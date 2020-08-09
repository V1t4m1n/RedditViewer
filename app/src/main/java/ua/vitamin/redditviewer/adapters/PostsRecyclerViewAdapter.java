package ua.vitamin.redditviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ua.vitamin.redditviewer.R;
import ua.vitamin.redditviewer.allerts.FullScreenImageDialog;
import ua.vitamin.redditviewer.dto.Post;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.PostsViewHolder> {

    private List <Post> postsList;
    private Context context;
    private FragmentManager manager;

    public PostsRecyclerViewAdapter(List<Post> postsList, Context context) {
        this.postsList = postsList;
        this.context = context;
    }
    public PostsRecyclerViewAdapter(List<Post> postsList, FragmentManager manager, Context context) {
        this.postsList = postsList;
        this.manager = manager;
        this.context = context;
    }
    public PostsRecyclerViewAdapter(List<Post> postsList) {
        this.postsList = postsList;
    }

    public PostsRecyclerViewAdapter() {}

    @Override
    public int getItemCount() {
        if (postsList != null && postsList.size() > 0)
            return postsList.size();
        else
            return 0;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_recycler_view, parent, false);
        PostsViewHolder viewHolder = new PostsViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {

        Picasso.get().load(postsList.get(position).getThumbnail()).into(holder.thumbnailImageView);

        holder.authorTextView.setText(postsList.get(position).getAuthor());
        holder.commentsCountTextView.setText(postsList.get(position).getCommentsCount());
        holder.dateAddedTextView.setText(""+postsList.get(position).getDateAdded());
    }

    class PostsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailImageView;
        TextView authorTextView;
        TextView dateAddedTextView;
        TextView commentsCountTextView;

        ImageView fullScreenImageView;

        public PostsViewHolder(final View itemView) {
            super(itemView);

            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            dateAddedTextView = itemView.findViewById(R.id.dateAddedTextView);
            commentsCountTextView = itemView.findViewById(R.id.commentsCountTextView);

            fullScreenImageView = itemView.findViewById(R.id.fullScreenImageView);

            thumbnailImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openImage(postsList.get(getAdapterPosition()).getImage());
                }
            });
        }

        private void openImage(String imageURL) {
            FullScreenImageDialog dialog = new FullScreenImageDialog(imageURL);
            dialog.show(manager, "FULL_SCREEN_DIALOG");
        }
    }
}
