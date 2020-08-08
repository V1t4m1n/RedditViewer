package ua.vitamin.redditviewer.callback;

import java.util.List;

import ua.vitamin.redditviewer.utils.dto.Post;

public interface Callable {
    public void setAdapter(List<Post> posts);
    public void onOpenThumbnail (String imageURL);
}
