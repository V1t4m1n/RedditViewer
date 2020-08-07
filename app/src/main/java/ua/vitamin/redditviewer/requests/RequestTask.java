package ua.vitamin.redditviewer.requests;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ua.vitamin.redditviewer.callback.Callable;
import ua.vitamin.redditviewer.utils.dto.Post;
import ua.vitamin.redditviewer.utils.dto.TimeFormatter;

public class RequestTask extends AsyncTask<String, String, String> {

    private String responseString;
    private String BASE_URL;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private StringBuilder sb;
    private InputStream inputStream;

    private Callable callable;

    public RequestTask(String url, Callable callable) {
        this.BASE_URL = url;
        this.callable = callable;
    }

    @Override
    protected String doInBackground(String... uri) {
        try {
            URL url = new URL(BASE_URL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            Log.d("STATUS_CODE", String.valueOf(connection.getResponseCode()));
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                String tmp;
                sb = new StringBuilder();
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8);

                while ((tmp = bufferedReader.readLine()) != null) {
                    sb.append(tmp);
                }
                inputStream.close();
                responseString = sb.toString();
            } else {
                Log.d("STATUS_CODE", String.valueOf(connection.getResponseCode()));
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            if (result != null)
                callable.setAdapter(CreateJsonArray(result));
            else
                new JSONException("Result equals null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<Post> CreateJsonArray(String jsonString) throws JSONException {
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
