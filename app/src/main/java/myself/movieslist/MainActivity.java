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

public class MainActivity extends ActionBarActivity implements FilmFragment.Callback{
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    Activity thisActivity;
    public static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisActivity = this;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9800")));


        if (findViewById(R.id.film_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.film_detail_container, new DetailFragment())
                        .commit();

            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
        FilmFragment ff = (FilmFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_film);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_film) {
            RunSearchDialog();
            return true;
        }else  if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void RunSearchDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(R.string.title_search_dialog);
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (input.getText().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Insert the title of the film to search", Toast.LENGTH_SHORT);
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

    @Override
    protected void onResume() {
        super.onResume();
        FilmFragment ff = (FilmFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_film);
        ff.ReloadLoader();
    }

    @Override
    public void onItemSelected(String film_title) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString(DetailActivity.FILM_TITLE_KEY, film_title);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.film_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivity.FILM_TITLE_KEY, film_title);
            startActivity(intent);
        }
    }
}

