package myself.movieslist;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class YouTubeApiPlayer extends YouTubeBaseActivity{
	String filmTitle;
	static private final String DEVELOPER_KEY = "AIzaSyBCYv3No9ry4VHfIXZl11QYoPMxoEadoJw";
    private String VIDEO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube);
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
            filmTitle = extras.getString("title");

		}
		new YoutubeAsyncTask().execute();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public class YoutubeAsyncTask extends AsyncTask<String, Integer, String> implements OnInitializedListener{
		protected String title;
		protected String Url="https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&type=video&key="+DEVELOPER_KEY+"&q="+filmTitle+"+official+trailer";

		@Override
		protected String doInBackground(String... params) {
			String response=SendHttpGet(filmTitle);
			return response;
		}

		public YoutubeAsyncTask(){
		}

		protected void onPostExecute(String result){
			YouTubePlayerView youTubeView = (YouTubePlayerView)findViewById(R.id.youtube_view);
            final TextView titleTextView = (TextView) findViewById(R.id.titolo);
            final TextView descriptionTextView = (TextView) findViewById(R.id.descrizione);
            JSONObject  jObject = null;
            try {
                jObject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject item= null;
            try {
                item = jObject.getJSONArray("items").getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(item!=null){

                VIDEO = getVideoId(item);

                titleTextView.setText(getTitle(item));
                descriptionTextView.setText(getDescription(item));
			youTubeView.initialize(DEVELOPER_KEY, this);

			}else ShowErrorMessageNoResults();
		}




		private String getVideoId(JSONObject item){
			String videoId = null;
			try {
				JSONObject id=item.getJSONObject("id");

				videoId = id.getString("videoId");

			} catch (JSONException e) {
				ShowErrorMessage();
			}
			return videoId;
		}

		private String getTitle(JSONObject item){
			String title="";
			try {
				JSONObject snippet=item.getJSONObject("snippet");

                title = snippet.getString("title");
			} catch (JSONException e) {
				ShowErrorMessage();
			}

			return title;
		}

		private String getDescription(JSONObject item){
			String description="";

			try {
				JSONObject snippet=item.getJSONObject("snippet");

                description = snippet.getString("description");;
			} catch (JSONException e) {
				ShowErrorMessage();
			}

			return description;
		}

		private String SendHttpGet(String title){
            title=title.replace(" ","+");
			String Url="https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&type=video&key="+DEVELOPER_KEY+"&q="+title+"+official+trailer";
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(Url);
			String resp = null;
			try {
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httpget);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                HttpEntity ht = response.getEntity();

				BufferedHttpEntity buf = new BufferedHttpEntity(ht);

				InputStream is = buf.getContent();

				BufferedReader r = new BufferedReader(new InputStreamReader(is));

				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				resp=total.toString();

			} catch (ClientProtocolException e) {
				ShowErrorMessage();
			} catch (IOException e) {
				ShowErrorMessage();
			}

			return resp;
		}

		private void ShowErrorMessageNoResults(){
			Toast.makeText(getApplicationContext(), "No results from the research", Toast.LENGTH_LONG).show();
			finish();
		}

		private void ShowErrorMessage(){
			Toast.makeText(getApplicationContext(), "Connection error to Youtube", Toast.LENGTH_LONG).show();
			finish();
		}

		@Override
		public void onInitializationFailure(Provider arg0,YouTubeInitializationResult arg1) {
			ShowErrorMessage();

		}

		@Override
		public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,boolean arg2) {
			arg1.loadVideo(VIDEO);

		}
	}
}