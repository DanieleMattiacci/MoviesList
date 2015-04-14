package myself.movieslist;

import android.app.AlertDialog;
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
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class FilmFragment  extends Fragment implements LoaderCallbacks<Cursor> {
    FragmentActivity mActivity;
    static FilmAdapter mFilmAdapter;
    ArrayList<ResponseFilm> movies;
    String orderBy;
    ListView listView;

    private static final int FILM_LOADER = 0;
    private int mPosition = ListView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private static final String[] FILM_COLUMNS = {
            FilmContract.FilmEntry._ID,
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

    public static final int _ID=0;
    public static final int COLUMN_TITLE_FILM=1;
    public static final int COLUMN_YEAR=2;
    public static final int COLUMN_RATED=3;
    public static final int COLUMN_RELEASED_DATE=4;
    public static final int COLUMN_RUNTIME=5;
    public static final int COLUMN_GENRE=6;
    public static final int COLUMN_DIRECTOR=7;
    public static final int COLUMN_WRITER=8;
    public static final int COLUMN_ACTOR=9;
    public static final int COLUMN_PLOT=10;
    public static final int COLUMN_AWARDS=11;
    public static final int COLUMN_RATING=12;
    public static final int COLUMN_METASCORE=13;
    public static final int COLUMN_VOTES=14;
    public static final int COLUMN_POSTER_URL=15;
    public static final int COLUMN_POSTER=16;
    public static final int COLUMN_WATCHED=17;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String title_film);
    }


    public FilmFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
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

        DBUtility dbUtil= new DBUtility();
        movies=dbUtil.ReadDb(rootView.getContext(), orderBy);

        mFilmAdapter = new FilmAdapter(getActivity(), null, 0);

        listView = (ListView) rootView.findViewById(R.id.listview_film);

        listView.setAdapter(mFilmAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = mFilmAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    ((Callback)getActivity())
                            .onItemSelected(cursor.getString(COLUMN_TITLE_FILM));
                }


            /*    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    try {

                        ((Callback) getActivity())
                                .onItemSelected(FilmContract.FilmEntry.buildFilmWithTitle(
                                        cursor.getString(cursor.getColumnIndex(FilmContract.FilmEntry.COLUMN_TITLE_FILM)))));
                    } catch (Exception e) {
                        Log.e("LOG_TAG","Errore nel caricamento del cursore");
                    }
                }


                Cursor cursor = mFilmAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, cursor.getString(cursor.getColumnIndex(FilmContract.FilmEntry.COLUMN_TITLE_FILM)));
                    startActivity(intent);
                }*/
                mPosition = position;
            }
        });


        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

   String getOrderBy(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
         String order = prefs.getString(getString(R.string.pref_order_by_key),
                getString(R.string.pref_order_by_rating));
        return order;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FILM_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        orderBy=getOrderBy();
        getLoaderManager().restartLoader(FILM_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri filmTitleUri = FilmContract.FilmEntry.buildFilmTitle();

        return new CursorLoader(
                getActivity(),
                filmTitleUri,
                FILM_COLUMNS,
                null,
                null,
                orderBy+" DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFilmAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            listView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFilmAdapter.swapCursor(null);
    }

   public void ReloadLoader(){
       getLoaderManager().restartLoader(FILM_LOADER, null, this);
    }

    public class SearchFilmTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = SearchFilmTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

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
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
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
                ReloadLoader();
                Toast.makeText(mActivity.getApplicationContext(), "Movie added to the list", Toast.LENGTH_SHORT).show();
            }
            else if (inserted == 0)
                Toast.makeText(mActivity.getApplicationContext(), "Movie not found", Toast.LENGTH_SHORT).show();
            else if (inserted == 2)
                Toast.makeText(mActivity.getApplicationContext(), "Movie already present in the list", Toast.LENGTH_SHORT).show();
        }
    }
}