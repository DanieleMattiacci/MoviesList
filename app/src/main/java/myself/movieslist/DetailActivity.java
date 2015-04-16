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
}