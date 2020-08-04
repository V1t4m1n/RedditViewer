package ua.vitamin.redditviewer.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ua.vitamin.redditviewer.MainActivity;
import ua.vitamin.redditviewer.R;
import ua.vitamin.redditviewer.allerts.FullScreenImageActivity;
import ua.vitamin.redditviewer.callback.Callable;
import ua.vitamin.redditviewer.dto.Post;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.PostsViewHolder> {

    private List <Post> postsList;
    private Context context;

    public PostsRecyclerViewAdapter(List<Post> postsList, Context context) {
        this.postsList = postsList;
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
        holder.dateAddedTextView.setText(postsList.get(position).getDateAdded());
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
                    //onOpenThumbnail(itemView.getContext());
                }
            });
        }

        private void onOpenThumbnail(Context con) {
            Intent intent = new Intent(itemView.getContext(), FullScreenImageActivity.class);
            intent.putExtra("url_image", postsList.get(getAdapterPosition()).getThumbnail());
            con.startActivity(intent);
        }
    }
}
