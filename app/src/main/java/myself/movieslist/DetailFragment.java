package myself.movieslist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import myself.movieslist.data.database.FilmContract;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
   /* ImageView i;
    Bitmap bitmap;
*/
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private String filmTitle, mFilmDetail;
    private ShareActionProvider mShareActionProvider;
    private static final String FILM_SHARE_HASHTAG = " #MoviesListApp:";

    private static final int DETAIL_LOADER = 0;

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

    private  TextView title;
    private  TextView release_label;
    private  TextView release_value;
    private  TextView rated_label;
    private  TextView rated_value;
    private  TextView runtime_label;
    private  TextView runtime_value;
    private  TextView genre_label;
    private  TextView genre_value;
    private  TextView director_label;
    private  TextView director_value;
    private  TextView writer_label;
    private  TextView writer_value;
    private  TextView actors_label;
    private  TextView actors_value;
    private  TextView plot_label;
    private  TextView plot_value;
    private  TextView awards_label;
    private  TextView awards_value;
    private  TextView rating_label;
    private  TextView rating_value;
    private  TextView metascore_label;
    private  TextView metascore_value;
    private  TextView votes_label;
    private  TextView votes_value;
    public static ImageView watched;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null)
            filmTitle = bundle.getString(DetailActivity.FILM_TITLE_KEY, "title");

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        title=((TextView) rootView.findViewById(R.id.film_title));
        release_label=((TextView) rootView.findViewById(R.id.released_date_label));
        release_value=((TextView) rootView.findViewById(R.id.released_date));
        rated_label=((TextView) rootView.findViewById(R.id.rated_label));
        rated_value=((TextView) rootView.findViewById(R.id.rated));
        runtime_label= ((TextView) rootView.findViewById(R.id.runtime_label));
        runtime_value= ((TextView) rootView.findViewById(R.id.runtime));
        genre_label= ((TextView) rootView.findViewById(R.id.genre_label));
        genre_value= ((TextView) rootView.findViewById(R.id.genre));
        director_label= ((TextView) rootView.findViewById(R.id.director_label));
        director_value= ((TextView) rootView.findViewById(R.id.director));
        writer_label= ((TextView) rootView.findViewById(R.id.writer_label));
        writer_value= ((TextView) rootView.findViewById(R.id.writer));
        actors_label= ((TextView) rootView.findViewById(R.id.actors_label));
        actors_value= ((TextView) rootView.findViewById(R.id.actors));
        plot_label= ((TextView) rootView.findViewById(R.id.plot_label));
        plot_value= ((TextView) rootView.findViewById(R.id.plot));
        awards_label= ((TextView) rootView.findViewById(R.id.awards_label));
        awards_value= ((TextView) rootView.findViewById(R.id.awards));
        rating_label= ((TextView) rootView.findViewById(R.id.rating_label));
        rating_value= ((TextView) rootView.findViewById(R.id.rating));
        metascore_label= ((TextView) rootView.findViewById(R.id.metascore_label));
        metascore_value= ((TextView) rootView.findViewById(R.id.metascore));
        votes_label= ((TextView) rootView.findViewById(R.id.votes_label));
        votes_value= ((TextView) rootView.findViewById(R.id.votes));
        watched= ((ImageView) rootView.findViewById(R.id.watched));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mFilmDetail != null )
            mShareActionProvider.setShareIntent(createShareFilmIntent());
    }

    private Intent createShareFilmIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,FILM_SHARE_HASHTAG+mFilmDetail);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri filmTitleUri = FilmContract.FilmEntry.buildFilmWithTitle(filmTitle);

        return new CursorLoader(
                getActivity(),
                filmTitleUri,
                FILM_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            title.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_TITLE_FILM)));
            release_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_RELEASED_DATE)));
            rated_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_RATED)));
            runtime_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_RUNTIME)));
            genre_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_GENRE)));
            director_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_DIRECTOR)));
            writer_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_WRITER)));
            actors_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_ACTOR)));
            plot_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_PLOT)));
            awards_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_AWARDS)));
            rating_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_RATING)));
            metascore_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_METASCORE)));
            votes_value.setText(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_VOTES)));

            if(data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_WATCHED)).equals("false"))watched.setVisibility(View.INVISIBLE);

            if (mShareActionProvider != null )
                mShareActionProvider.setShareIntent(createShareFilmIntent());
            mFilmDetail = " See this film"+data.getString(data.getColumnIndex(FilmContract.FilmEntry.COLUMN_TITLE_FILM));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}