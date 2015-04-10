package myself.movieslist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import myself.movieslist.data.DBUtility;

public class MainActivity extends ActionBarActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    Activity thisActivity;
    private boolean mTwoPane;
    String orderBy;
    boolean firstTime=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisActivity = this;
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9800")));
     //   FilmDbHelper dbhelper=new FilmDbHelper(thisActivity.getApplicationContext());
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/
        //orderBy=getOrderBy();
    if(firstTime==false)InsertSomeFilm();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FilmFragment())
                    .commit();
        }
    }

    public void InsertSomeFilm(){

        DBUtility dbUtil= new DBUtility();
        int inserted= dbUtil.insertFilm("{\"Title\":\"Braveheart\",\"Year\":\"1995\",\"Rated\":\"N/A\",\"Released\":\"24 May 1995\",\"Runtime\":\"177 min\",\"Genre\":\"Action, Biography, Drama\"," +
                "\"Director\":\"Mel Gibson\",\"Writer\":\"Randall Wallace\",\"Actors\":\"James Robinson, Sean Lawlor, Sandy Nelson, James Cosmo\"," +
                "\"Plot\":\"When his secret bride is executed for assaulting an English soldier who tried to rape her, William Wallace begins a revolt and leads Scottish warriors against the cruel" +
                " English tyrant who rules Scotland with an iron fist.\",\"Language\":\"English, French, Latin, Scottish Gaelic\",\"Country\":\"USA\",\"Awards\":\"Won 5 Oscars. Another 20 wins & 20 " +
                "nominations.\",\"Poster\":\"http://ia.media-imdb.com/images/M/MV5BNjA4ODYxMDU3Nl5BMl5BanBnXkFtZTcwMzkzMTk3OA@@._V1_SX300.jpg\",\"Metascore\":\"68\",\"imdbRating\":\"8.4\",\"imdbVotes\":" +
                "\"615,512\",\"imdbID\":\"tt0112573\",\"Type\":\"movie\",\"Response\":\"True\"}",thisActivity.getApplicationContext());
        firstTime=true;
       // Log.i(LOG_TAG,"Inserted: "+inserted);
    }

    String getOrderBy(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String order = prefs.getString(getString(R.string.pref_order_by_key),
                getString(R.string.pref_order_by_rating));
        return order;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     /*   if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }*/ /*else if (id == R.id.action_search_film) {


            //new SearchDialog().show();
            RunSearchDialog();
            //  Toast.makeText(getApplicationContext(), "premuto Search", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        /*if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

   /* private void RunSearchDialog() {
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
*/
    @Override
    protected void onStart() {
        Log.v(LOG_TAG, "in onStart");
        super.onStart();
        // The activity is about to become visible.
    }

    @Override
    protected void onResume() {
        Log.v(LOG_TAG, "in onResume");
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause() {
        Log.v(LOG_TAG, "in onPause");
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop() {
        Log.v(LOG_TAG, "in onStop");
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "in onDestroy");
        super.onDestroy();
        // The activity is about to be destroyed.
    }
}

