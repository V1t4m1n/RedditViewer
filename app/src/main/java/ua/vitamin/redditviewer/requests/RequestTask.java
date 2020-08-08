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

import ua.vitamin.redditviewer.callback.Callable;
import ua.vitamin.redditviewer.utils.dto.ParserJson;

public class RequestTask extends AsyncTask<String, String, String> {

    private String responseString;
    private String BASE_URL;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private StringBuilder sb;
    private InputStream inputStream;
    private ParserJson parser;

    private Callable callable;

    public RequestTask(String url, Callable callable) {
        this.BASE_URL = url;
        this.callable = callable;
        this.parser = new ParserJson();
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
                callable.setAdapter(parser.onCreateListModels(result));
            else
                new JSONException("Result equals null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String onRequest() {
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

}
