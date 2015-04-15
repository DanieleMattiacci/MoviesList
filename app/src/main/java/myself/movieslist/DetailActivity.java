package myself.movieslist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import myself.movieslist.service.SearchFilmService;


public class DetailActivity extends ActionBarActivity {
Activity thisActivity;
 String title_film;
    public static final String FILM_TITLE_KEY = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        thisActivity = this;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9800")));

        if (savedInstanceState == null) {

            if (savedInstanceState == null) {
                title_film = getIntent().getStringExtra(FILM_TITLE_KEY);
                DetailFragment detailFragment=new DetailFragment();
                Bundle arguments = new Bundle();
                arguments.putString(FILM_TITLE_KEY, title_film);
                detailFragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().add(R.id.film_detail_container, detailFragment).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else if (id == R.id.action_search_film) {
            RunSearchDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void RunSearchDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(thisActivity);
        alert.setMessage(R.string.title_search_dialog);
        final EditText input = new EditText(thisActivity);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (input.getText().length() == 0) {
                    Toast toast = Toast.makeText(thisActivity.getApplicationContext(), "Insert the title of the film to search", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent intent = new Intent(thisActivity, SearchFilmService.class);
                    intent.putExtra(SearchFilmService.FILM_TITLE,input.getText().toString());
                    thisActivity.startService(intent);
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    /*void MarkWatched(){
        DBUtility dbUtil= new DBUtility();
        boolean watched=dbUtil.updateWatchedFilm(title_film,getApplicationContext());
        if(watched==true)DetailFragment.watched.setVisibility(View.VISIBLE);
    }*/

   /* public class SearchFilmTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = SearchFilmTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String filmJsonStr = null;

            String year = "";
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
           /* String location = prefs.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));*/
         /*   String plot = prefs.getString(getString(R.string.pref_plot_key),
                    getString(R.string.pref_plot_short));
            String response = "json";

            try {
                final String FILM_BASE_URL = "http://www.omdbapi.com/?";
                final String TITLE_PARAM = "t";
                final String YEAR_PARAM = "y";
                final String PLOT_PARAM = "plot";
                final String RESPONSE_PARAM = "r";

                Uri builtUri = Uri.parse(FILM_BASE_URL).buildUpon()
                        .appendQueryParameter(TITLE_PARAM, params[0])
                        .appendQueryParameter(YEAR_PARAM, year)
                        .appendQueryParameter(PLOT_PARAM, plot)
                        .appendQueryParameter(RESPONSE_PARAM, response)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.i(LOG_TAG, url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                filmJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            String[] result = new String[]{filmJsonStr};
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            DBUtility dbUtil = new DBUtility();
            int inserted = dbUtil.insertFilm(result[0], getApplicationContext());
            if (inserted == 1) {
                Toast.makeText(getApplicationContext(), "Movie added to the list", Toast.LENGTH_SHORT).show();
            }
            else if (inserted == 0)
                Toast.makeText(getApplicationContext(), "Movie not found", Toast.LENGTH_SHORT).show();
            else if (inserted == 2)
                Toast.makeText(getApplicationContext(), "Movie already present in the list", Toast.LENGTH_SHORT).show();
        }
    }*/
}