package de.sidanner.newsalarm;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by simon on 3/27/16.
 */
public class Podcast implements Parcelable {

    public static final Creator<Podcast> CREATOR = new Creator<Podcast>() {
        @Override
        public Podcast createFromParcel(Parcel in) {
            return new Podcast(in);
        }

        @Override
        public Podcast[] newArray(int size) {
            return new Podcast[size];
        }
    };
    public String name;
    public URL url;
    private String latestUrl;


    public Podcast() {

        try {
            url = new URL("http://www.deutschlandfunk.de/podcast-nachrichten.1257.de.podcast.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected Podcast(Parcel in) {
        name = in.readString();
        try {
            url = new URL(in.readString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getLastNews(OnTaskCompleted listener) {
        new DownloadXmlTask(listener).execute(url.toString());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url.toString());
    }

    public String getLatestUrl() {
        return latestUrl;
    }


    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        private OnTaskCompleted listener;

        private DownloadXmlTask(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "ioexception";
            } catch (XmlPullParserException e) {
                return "parserexception";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("bla", "result: " + result);
            listener.onTaskCompleted(result);
        }


        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            ITunesParser parser = new ITunesParser();
            List<ITunesParser.Item> items = null;

            Log.d("bla", "getting url " + urlString);

            try {
                stream = downloadUrl(urlString);
                items = parser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }


            return items.get(0).link;
        }

        // Given a string representation of a URL, sets up a connection and gets
// an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }
}


