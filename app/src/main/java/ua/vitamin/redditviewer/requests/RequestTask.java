package ua.vitamin.redditviewer.requests;

import android.annotation.SuppressLint;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ua.vitamin.redditviewer.callback.Callable;
import ua.vitamin.redditviewer.dto.Post;

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
        Date past = null;
        Date now = null;
        long timeAgo = 0;

        for (int i = 0; i < jsonArray.length(); i++) {
            Post post = new Post();

            JSONObject topic = jsonArray.getJSONObject(i).getJSONObject("data");

            post.setAuthor("Author: " + topic.getString("author"));
            post.setThumbnail(topic.getString("thumbnail"));
            try {
               /* SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                format.setTimeZone(TimeZone.getTimeZone("UTC"));

                past = new Date(String.valueOf(format.parse(topic.getString("created_utc"))));
                now = new Date();*/

                //timeAgo = now.getTime() - past.getTime();

            } catch (Exception e) {
                e.printStackTrace();
            }
            post.setDateAdded(/*TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime())*/10 + " hours ago");

            post.setCommentsCount("Comments count: " + topic.getString("num_comments"));

            postList.add(post);
            Log.d("ONE_POST", post.getAuthor());
        }
        return postList;
    }
}
