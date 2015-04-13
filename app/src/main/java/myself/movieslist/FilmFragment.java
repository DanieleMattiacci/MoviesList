package myself.movieslist;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import myself.movieslist.data.DBUtility;
import myself.movieslist.data.FilmAdapter;
import myself.movieslist.data.database.FilmContract;
import myself.movieslist.data.pojo.ResponseFilm;

public class FilmFragment  extends Fragment {
    FragmentActivity mActivity;

    static ArrayAdapter<ResponseFilm> mFilmAdapter;
    ArrayList<ResponseFilm> movies;
    String orderBy;
    View appView;
    ListView listView;

    public FilmFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.film_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_film) {
            RunSearchDialog();
            return true;
        }else  if (id == R.id.action_settings) {
            startActivity(new Intent(mActivity, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void RunSearchDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
        alert.setMessage(R.string.title_search_dialog);
        final EditText input = new EditText(mActivity);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (input.getText().length() == 0) {
                    Toast toast = Toast.makeText(mActivity.getApplicationContext(), "Insert the title of the film to search", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    new SearchFilmTask().execute(input.getText().toString());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_film, container, false);
        orderBy=getOrderBy();
        appView=rootView;

        DBUtility dbUtil= new DBUtility();
        movies=dbUtil.ReadDb(rootView.getContext(), orderBy);

        mFilmAdapter = new FilmAdapter(rootView.getContext(),R.layout.list_item_film,movies,orderBy);
        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.listview_film);

        listView.setAdapter(mFilmAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ResponseFilm film = mFilmAdapter.getItem(position);
               // String itemFilm = mFilmAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, film.getTitle());
                startActivity(intent);
            }
        });

        return rootView;
    }

    String getOrderBy(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
         String order = prefs.getString(getString(R.string.pref_order_by_key),
                getString(R.string.pref_order_by_rating));
        return order;
    }

    @Override
    public void onStart() {
        super.onStart();
        DBUtility dbUtil= new DBUtility();
        orderBy=getOrderBy();
        movies=dbUtil.ReadDb(appView.getContext(), orderBy);
        //  Log.i("","movies.size() fragment: "+movies.size());
        mFilmAdapter = new FilmAdapter(appView.getContext(),R.layout.list_item_film,movies,orderBy);
        listView.setAdapter(mFilmAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        //mFilmAdapter.notifyDataSetChanged();
         DBUtility dbUtil= new DBUtility();
        orderBy=getOrderBy();
        movies=dbUtil.ReadDb(appView.getContext(), orderBy);
        //  Log.i("","movies.size() fragment: "+movies.size());
        mFilmAdapter = new FilmAdapter(appView.getContext(),R.layout.list_item_film,movies,orderBy);
        listView.setAdapter(mFilmAdapter);
    }

    public class SearchFilmTask extends AsyncTask<String, Void, String[]> {

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
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity.getApplicationContext());

            String plot = prefs.getString(getString(R.string.pref_plot_key),getString(R.string.pref_plot_short));
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

            int inserted = dbUtil.insertFilm(result[0], mActivity.getApplicationContext());
            if (inserted == 1) {
                orderBy=getOrderBy();
                movies=dbUtil.ReadDb(appView.getContext(), orderBy);
                mFilmAdapter = new FilmAdapter(appView.getContext(),R.layout.list_item_film,movies,orderBy);
                listView.setAdapter(mFilmAdapter);
                Toast.makeText(mActivity.getApplicationContext(), "Movie added to the list", Toast.LENGTH_SHORT).show();
            }
            else if (inserted == 0)
                Toast.makeText(mActivity.getApplicationContext(), "Movie not found", Toast.LENGTH_SHORT).show();
            else if (inserted == 2)
                Toast.makeText(mActivity.getApplicationContext(), "Movie already present in the list", Toast.LENGTH_SHORT).show();
        }
    }
}