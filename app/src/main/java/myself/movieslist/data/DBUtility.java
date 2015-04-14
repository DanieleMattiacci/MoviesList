package myself.movieslist.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import myself.movieslist.data.database.FilmContract;
import myself.movieslist.data.database.FilmContract.FilmEntry;
import myself.movieslist.data.database.FilmDbHelper;
import myself.movieslist.data.pojo.ResponseFilm;


public class DBUtility extends AndroidTestCase {
/*
* 0, not found
* 1, inserted
* 2, alreadyPresent
* */
    public int insertFilm(String jsonFilm, Context context) {
        int resp=0;
        String title=getTitleFromJson(jsonFilm);
        if(title!=null) {
            boolean present = alreadyPresent(title, context);
            if (present == false) {
                boolean insertedSuccessful = false;

                ContentValues testValues = CreateFilmValues(jsonFilm);

                Uri filmnUri = context.getContentResolver().insert(FilmContract.FilmEntry.CONTENT_URI, testValues);
                long filmRowId = ContentUris.parseId(filmnUri);

                if (filmRowId != -1) insertedSuccessful = true;

                if (insertedSuccessful == true) return resp = 1;
                else return resp = 0;
            } else return resp = 2;
        } else return 0;
    }

    boolean alreadyPresent(String title,Context context){
            Cursor cursor = selectFilmByTitle(title,context);
            if(cursor.getCount() <= 0)return false;
        return true;
    }

    public Cursor selectFilmByTitle(String title,Context context){
        String[] params = {title};

        String[] columns = {
                FilmEntry.COLUMN_TITLE_FILM,
                FilmEntry.COLUMN_YEAR,
                FilmEntry.COLUMN_RATED,
                FilmEntry.COLUMN_RELEASED_DATE,
                FilmEntry.COLUMN_RUNTIME,
                FilmEntry.COLUMN_GENRE,
                FilmEntry.COLUMN_DIRECTOR,
                FilmEntry.COLUMN_WRITER,
                FilmEntry.COLUMN_ACTOR,
                FilmEntry.COLUMN_PLOT,
                FilmEntry.COLUMN_AWARDS,
                FilmEntry.COLUMN_RATING,
                FilmEntry.COLUMN_METASCORE,
                FilmEntry.COLUMN_VOTES,
                FilmEntry.COLUMN_POSTER_URL,
                FilmEntry.COLUMN_POSTER,
                FilmEntry.COLUMN_WATCHED,
        };

        Cursor cursor = context.getContentResolver().query(
                FilmEntry.CONTENT_URI,
                columns,
                FilmContract.FilmEntry.COLUMN_TITLE_FILM+"=?",
                params,
                null
        );
        return cursor;
    }

    public boolean updateWatchedFilm(String title,Context context) {
        ContentValues values = new ContentValues();
        values.put(FilmEntry.COLUMN_WATCHED, "true");
        return context.getContentResolver().update(
                FilmEntry.CONTENT_URI, values, FilmEntry.COLUMN_TITLE_FILM + "= ?",
                new String[] {title})>0;
    }

    String getTitleFromJson(String jsonFilm){
        ContentValues values=CreateFilmValues(jsonFilm);
        String title= (String) values.get(FilmEntry.COLUMN_TITLE_FILM);
        return title;
    }

    static ContentValues CreateFilmValues(String jsonFilm){
       Gson gson = new Gson();
       ResponseFilm resp = gson.fromJson(jsonFilm, ResponseFilm.class);
       resp.setWatched("false");
       ContentValues values=ObjectToContentValues(resp);
       return values;
   }

    static ContentValues ObjectToContentValues(ResponseFilm resp) {
        ContentValues values = new ContentValues();
        values.put(FilmEntry.COLUMN_TITLE_FILM, resp.getTitle());
        values.put(FilmEntry.COLUMN_YEAR, resp.getYear());
        values.put(FilmEntry.COLUMN_RATED, resp.getRated());
        values.put(FilmEntry.COLUMN_COUNTRY, resp.getCountry());
        values.put(FilmEntry.COLUMN_IMDB_ID, resp.getImdbID());
        values.put(FilmEntry.COLUMN_RELEASED_DATE, resp.getReleased());
        values.put(FilmEntry.COLUMN_RUNTIME, resp.getRuntime());
        values.put(FilmEntry.COLUMN_GENRE, resp.getGenre());
        values.put(FilmEntry.COLUMN_DIRECTOR, resp.getDirector());
        values.put(FilmEntry.COLUMN_WRITER, resp.getWriter());
        values.put(FilmEntry.COLUMN_ACTOR, resp.getActors());
        values.put(FilmEntry.COLUMN_PLOT, resp.getPlot());
        values.put(FilmEntry.COLUMN_AWARDS, resp.getAwards());
        values.put(FilmEntry.COLUMN_RATING, resp.getImdbRating());
        values.put(FilmEntry.COLUMN_METASCORE, resp.getMetascore());
        values.put(FilmEntry.COLUMN_VOTES, resp.getImdbVotes());
        values.put(FilmEntry.COLUMN_POSTER_URL, resp.getPoster());
        values.put(FilmEntry.COLUMN_POSTER, "");
        values.put(FilmEntry.COLUMN_WATCHED, resp.getWatched());
        values.put(FilmEntry.COLUMN_LANGUAGE, resp.getLanguage());
        return values;
    }

    public ArrayList<ResponseFilm> ReadDb(Context context, String orderBy) {
        ArrayList<ResponseFilm> movies = new ArrayList<ResponseFilm>();

        Cursor cursor = getCursorOrdered(context,orderBy);

        if (cursor.moveToFirst())
            do {
                ResponseFilm entryFilm = new ResponseFilm();
                entryFilm.setTitle(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_TITLE_FILM)));
                entryFilm.setWatched(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_WATCHED)));
                if (orderBy.equals("rating"))
                    entryFilm.setImdbRating(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_RATING)));
                else if (orderBy.equals("metascore"))
                    entryFilm.setMetascore(cursor.getString(cursor.getColumnIndex(FilmEntry.COLUMN_METASCORE)));
                movies.add(entryFilm);
            }while(cursor.moveToNext());
        return movies;
    }

    Cursor getCursorOrdered(Context context,String orderBy){
        String[] columns = {
                FilmEntry.COLUMN_TITLE_FILM,
                FilmEntry.COLUMN_RATING,
                FilmEntry.COLUMN_METASCORE,
                FilmEntry.COLUMN_WATCHED,
        };

        Cursor cursor = context.getContentResolver().query(
                FilmEntry.CONTENT_URI,
                columns,
                null,
                null,
                orderBy+" DESC"
        );
        return cursor;
    }
}

