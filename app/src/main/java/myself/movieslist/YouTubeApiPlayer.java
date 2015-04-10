package myself.movieslist;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
//	Activity thisActivity;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube);
	//	thisActivity = this;
		Bundle extras = getIntent().getExtras();
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9800")));
	//	getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (extras != null) {
            filmTitle = extras.getString("title");

		}

		new YoutubeAsyncTask().execute();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	
	/**********************************************YoutubeAsyncTask********************************************************/
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
			//Log.i("ricevuto", result);

			YouTubePlayerView youTubeView = (YouTubePlayerView)findViewById(R.id.youtube_view);
            final TextView titoloView = (TextView) findViewById(R.id.titolo);
            final TextView descrizioneView = (TextView) findViewById(R.id.descrizione);
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

                VIDEO = RicavaVideoId(item);

                String titolo=RicavaTitolo(item);
                String descrizione=RicavaDescrizione(item);

                titoloView.setText(titolo);
                descrizioneView.setText(descrizione);
			youTubeView.initialize(DEVELOPER_KEY, this);



			}else ShowErrorMessageNoResults();
		}




		private String RicavaVideoId(JSONObject item){
			String videoId = null;
			try {
				JSONObject id=item.getJSONObject("id");

				videoId = id.getString("videoId");

			} catch (JSONException e) {
				ShowErrorMessage();
			}
            Log.i("","Videoid: "+videoId);
			return videoId;
		}

		private String RicavaTitolo(JSONObject item){
			String titolo="";

			try {
				JSONObject snippet=item.getJSONObject("snippet");

				titolo = snippet.getString("title");
                Log.i("","titolo: "+titolo);
			} catch (JSONException e) {
				ShowErrorMessage();
			}

			return titolo;
		}

		private String RicavaDescrizione(JSONObject item){
			String descrizione="";

			try {
				JSONObject snippet=item.getJSONObject("snippet");

				descrizione = snippet.getString("description");
                Log.i("","descrizione: "+descrizione);
			} catch (JSONException e) {
				ShowErrorMessage();
			}

			return descrizione;
		}

		private String SendHttpGet(String title){
            title=title.replace(" ","+");
			//titolo=ReplaceChar(titolo);
			//autore=ReplaceChar(autore);
			String Url="https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&type=video&key="+DEVELOPER_KEY+"&q="+title+"+official+trailer";
			Log.i("inviato Url youtube",Url);
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
			Toast.makeText(getApplicationContext(), "La ricerca su Youtube non ha trovato nessun video", Toast.LENGTH_LONG).show();
			finish();
		}

		private void ShowErrorMessage(){
			Toast.makeText(getApplicationContext(), "Errore di connessione a Youtube", Toast.LENGTH_LONG).show();
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
	/****************************************************************************************************************/
}