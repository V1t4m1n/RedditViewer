package ua.vitamin.redditviewer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ua.vitamin.redditviewer.R;
import ua.vitamin.redditviewer.dto.Post;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.PostsViewHolder> {

    private List <Post> postsList;

    public PostsRecyclerViewAdapter(List<Post> postsList) {
        this.postsList = postsList;
    }

    @Override
    public int getItemCount() {
        return postsList.size();
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
        holder.dateAddedTextView.setText(postsList.get(position).getDateAdded());
    }

    class PostsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnailImageView;
        TextView authorTextView;
        TextView dateAddedTextView;
        TextView commentsCountTextView;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            dateAddedTextView = itemView.findViewById(R.id.dateAddedTextView);
            commentsCountTextView = itemView.findViewById(R.id.commentsCountTextView);
        }
    }
}
