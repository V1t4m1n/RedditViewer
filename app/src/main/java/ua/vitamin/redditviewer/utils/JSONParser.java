package ua.vitamin.redditviewer.utils;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ua.vitamin.redditviewer.dto.Post;

import static android.text.Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE;

public class JSONParser {

    public List<Post> parse(String jsonString) throws JSONException {
        JSONObject response = new JSONObject(jsonString);
        JSONObject data = response.getJSONObject("data");
        JSONArray jsonArray = data.getJSONArray("children");

        List<Post> postList = new ArrayList<>();

        String format = "yyyy/MM/dd HH:mm:ss";

        for (int index = 0; index < jsonArray.length(); index++) {
            Post post = new Post();

            JSONObject topic = jsonArray.getJSONObject(index).getJSONObject("data");

            JSONObject previewJsonObject = topic.getJSONObject("preview");
            JSONObject imagesJsonObject = previewJsonObject.getJSONArray("images").getJSONObject(0);
            JSONObject sourceJsonObject = imagesJsonObject.getJSONObject("source");

            String imageUrlString = Html.fromHtml(sourceJsonObject.getString("url"), TO_HTML_PARAGRAPH_LINES_CONSECUTIVE).toString();

            post.setImage(imageUrlString);

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
