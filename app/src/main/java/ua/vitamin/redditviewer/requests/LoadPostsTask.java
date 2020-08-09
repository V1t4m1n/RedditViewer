package ua.vitamin.redditviewer.requests;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ua.vitamin.redditviewer.dto.Post;
import ua.vitamin.redditviewer.utils.JSONParser;

public class LoadPostsTask extends AsyncTask<String, String, String> {

    public interface ResponseHandler {
        void onResponse(List<Post> posts);
        void onError(Error error);
    }

    private String responseString;
    private String url;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private StringBuilder sb;
    private InputStream inputStream;
    private JSONParser parser;

    private ResponseHandler handler;

    public LoadPostsTask(String url, ResponseHandler handler) {
        this.url = url;
        this.handler = handler;
        this.parser = new JSONParser();
    }

    @Override
    protected String doInBackground(String... uri) {
        return onRequest();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            if (result != null)
                handler.onResponse(parser.parse(result));
            else
                new JSONException("Result equals null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String onRequest() {
        try {
            URL url = new URL(this.url);

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
}
