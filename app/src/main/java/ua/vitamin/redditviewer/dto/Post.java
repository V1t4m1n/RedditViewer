package ua.vitamin.redditviewer.dto;

public class Post {

    String thumbnail = "https://refactoring.guru/images/content-public/logos/logo-new.png";
    String author;
    String dateAdded;
    String commentsCount;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        if (thumbnail == null) this.thumbnail = "https://refactoring.guru/images/content-public/logos/logo-new.png";
        else this.thumbnail = thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }
}
