package ua.vitamin.redditviewer.requests;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestTask extends AsyncTask<String, String, String> {

    private String responseString;
    private String BASE_URL;
    private HttpURLConnection connection;
    private BufferedReader bufferedReader;
    private StringBuilder sb;
    private InputStream inputStream;

    TextView textView;

    public RequestTask(String url, TextView textView) {
        this.BASE_URL = url;
        this.textView = textView;
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
                Log.d("BODY", "ERROR");
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        textView.setText(s);
    }
}
