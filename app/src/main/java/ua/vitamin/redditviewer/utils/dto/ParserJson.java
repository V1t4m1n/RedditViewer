package ua.vitamin.redditviewer.utils.dto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ParserJson {

    public List<Post> onCreateListModels(String jsonString) throws JSONException {
        JSONObject response = new JSONObject(jsonString);
        JSONObject data = response.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("children");
        List<Post> postList = new ArrayList<>();

        String format = "yyyy/MM/dd HH:mm:ss";

        for (int i = 0; i < jsonArray.length(); i++) {
            Post post = new Post();

            JSONObject topic = jsonArray.getJSONObject(i).getJSONObject("data");

            post.setAuthor("Author: " + topic.getString("author"));
            post.setThumbnail(topic.getString("thumbnail"));

            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date localDate = new Date(topic.getLong("created_utc"));
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date gmtTime = new Date(sdf.format(localDate));

            Date fromGmt = new Date(gmtTime.getTime() + TimeZone.getDefault().getOffset(localDate.getTime()));
            String time = TimeFormatter.getTimeAgo(fromGmt.getTime());
            post.setDateAdded(time);

            post.setCommentsCount("Comments count: " + topic.getString("num_comments"));

            postList.add(post);
            Log.d("ONE_POST", post.getAuthor());
        }
        return postList;
    }
}
