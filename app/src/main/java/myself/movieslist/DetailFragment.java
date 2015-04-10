package myself.movieslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import myself.movieslist.data.DBUtility;
import myself.movieslist.data.pojo.ResponseFilm;

public class DetailFragment extends Fragment {
   /* ImageView i;
    Bitmap bitmap;
*/
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private String filmTitle;
    private static final String FILM_SHARE_HASHTAG = " #MoviesListApp: See this film ";
    private String mForecastStr;

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
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            filmTitle = bundle.getString("title", "title");

            DBUtility dbutil= new DBUtility();
            ResponseFilm entry=dbutil.selectFilm(filmTitle, getActivity().getApplicationContext());

            title=((TextView) rootView.findViewById(R.id.film_title));
            title.setText(entry.getTitle());
            release_label=((TextView) rootView.findViewById(R.id.released_date_label));
            release_value=((TextView) rootView.findViewById(R.id.released_date));
            release_value.setText(entry.getReleased());
            rated_label=((TextView) rootView.findViewById(R.id.rated_label));
            rated_value=((TextView) rootView.findViewById(R.id.rated));
            rated_value.setText(entry.getRated());
            runtime_label= ((TextView) rootView.findViewById(R.id.runtime_label));
            runtime_value= ((TextView) rootView.findViewById(R.id.runtime));
            runtime_value.setText(entry.getRuntime());
            genre_label= ((TextView) rootView.findViewById(R.id.genre_label));
            genre_value= ((TextView) rootView.findViewById(R.id.genre));
            genre_value.setText(entry.getGenre());
            director_label= ((TextView) rootView.findViewById(R.id.director_label));
            director_value= ((TextView) rootView.findViewById(R.id.director));
            director_value.setText(entry.getDirector());
            writer_label= ((TextView) rootView.findViewById(R.id.writer_label));
            writer_value= ((TextView) rootView.findViewById(R.id.writer));
            writer_value.setText(entry.getWriter());
            actors_label= ((TextView) rootView.findViewById(R.id.actors_label));
            actors_value= ((TextView) rootView.findViewById(R.id.actors));
            actors_value.setText(entry.getActors());
            plot_label= ((TextView) rootView.findViewById(R.id.plot_label));
            plot_value= ((TextView) rootView.findViewById(R.id.plot));
            plot_value.setText(entry.getPlot());
            awards_label= ((TextView) rootView.findViewById(R.id.awards_label));
            awards_value= ((TextView) rootView.findViewById(R.id.awards));
            awards_value.setText(entry.getAwards());
            rating_label= ((TextView) rootView.findViewById(R.id.rating_label));
            rating_value= ((TextView) rootView.findViewById(R.id.rating));
            rating_value.setText(entry.getImdbRating());
            metascore_label= ((TextView) rootView.findViewById(R.id.metascore_label));
            metascore_value= ((TextView) rootView.findViewById(R.id.metascore));
            metascore_value.setText(entry.getMetascore());
            votes_label= ((TextView) rootView.findViewById(R.id.votes_label));
            votes_value= ((TextView) rootView.findViewById(R.id.votes));
            votes_value.setText(entry.getImdbVotes());
            watched= ((ImageView) rootView.findViewById(R.id.watched));
            if(entry.getWatched().equals("false"))watched.setVisibility(View.INVISIBLE);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareFilmIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareFilmIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                FILM_SHARE_HASHTAG+filmTitle);
        return shareIntent;
    }
}