package myself.movieslist.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import myself.movieslist.data.database.FilmContract;

import static myself.movieslist.Utility.CreateFilmValues;
import static myself.movieslist.Utility.getTitleFromJson;


public class SearchFilmService extends IntentService {
    public static final String FILM_TITLE = "service_title_film";

    public SearchFilmService() {
        super("MoviesList");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String titleFilm = intent.getStringExtra(FILM_TITLE);
        String filmJsonStr = getJsonString(titleFilm);
        String title=getTitleFromJson(filmJsonStr);

        if(title==null)ShowToastInIntentService("Movie not found");
        else {
            if (!alreadyPresent(title)) {
                Vector<ContentValues> cVVector = new Vector<>();

                ContentValues filmValues = CreateFilmValues(filmJsonStr);

                cVVector.add(filmValues);

                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    this.getContentResolver().bulkInsert(FilmContract.FilmEntry.CONTENT_URI, cvArray);
                    ShowToastInIntentService("Movie added to the list");
                }
            }else ShowToastInIntentService("Movie already present in the list");
        }
    }

    String getJsonString(String titleFilm) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String filmJsonStr = null;

        String year = "";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String plot = prefs.getString("plot", "short");
        String response = "json";

        try {
            final String FILM_BASE_URL = "http://www.omdbapi.com/?";
            final String TITLE_PARAM = "t";
            final String YEAR_PARAM = "y";
            final String PLOT_PARAM = "plot";
            final String RESPONSE_PARAM = "r";

            Uri builtUri = Uri.parse(FILM_BASE_URL).buildUpon()
                    .appendQueryParameter(TITLE_PARAM, titleFilm)
                    .appendQueryParameter(YEAR_PARAM, year)
                    .appendQueryParameter(PLOT_PARAM, plot)
                    .appendQueryParameter(RESPONSE_PARAM, response)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            filmJsonStr = buffer.toString();
        } catch (IOException e) {
            ShowToastInIntentService("Internet Connection Problem! retry Later.");
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }
        return filmJsonStr;
    }

    boolean alreadyPresent(String title){
        Cursor cursor = selectFilmByTitle(title);
        if(cursor.getCount() <= 0)return false;
        else return true;
    }

    public Cursor selectFilmByTitle(String title){
        String[] params = {title};

        String[] columns = {
                FilmContract.FilmEntry.COLUMN_TITLE_FILM,
                FilmContract.FilmEntry.COLUMN_YEAR,
                FilmContract.FilmEntry.COLUMN_RATED,
                FilmContract.FilmEntry.COLUMN_RELEASED_DATE,
                FilmContract.FilmEntry.COLUMN_RUNTIME,
                FilmContract.FilmEntry.COLUMN_GENRE,
                FilmContract.FilmEntry.COLUMN_DIRECTOR,
                FilmContract.FilmEntry.COLUMN_WRITER,
                FilmContract.FilmEntry.COLUMN_ACTOR,
                FilmContract.FilmEntry.COLUMN_PLOT,
                FilmContract.FilmEntry.COLUMN_AWARDS,
                FilmContract.FilmEntry.COLUMN_RATING,
                FilmContract.FilmEntry.COLUMN_METASCORE,
                FilmContract.FilmEntry.COLUMN_VOTES,
                FilmContract.FilmEntry.COLUMN_POSTER_URL,
                FilmContract.FilmEntry.COLUMN_POSTER,
                FilmContract.FilmEntry.COLUMN_WATCHED,
        };

        Cursor cursor = getApplicationContext().getContentResolver().query(
                FilmContract.FilmEntry.CONTENT_URI,
                columns,
                FilmContract.FilmEntry.COLUMN_TITLE_FILM+"=?",
                params,
                null
        );
        return cursor;
    }

    public void ShowToastInIntentService(final String sText)
    {  final Context MyContext = this;
        new Handler(Looper.getMainLooper()).post(new Runnable()
        {  @Override public void run()
            {  Toast toast1 = Toast.makeText(MyContext, sText, Toast.LENGTH_LONG);
                toast1.show();
            }
        });
    }
}