package myself.movieslist.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.google.gson.Gson;

import java.util.ArrayList;

import myself.movieslist.data.database.FilmContract;
import myself.movieslist.data.database.FilmContract.FilmEntry;
import myself.movieslist.data.database.FilmDbHelper;
import myself.movieslist.data.pojo.ResponseFilm;


public class DBUtility extends AndroidTestCase {
/*
* 0, not inserted
* 1, inserted
* 2, alreadyPresent
* */
    public int insertFilm(String jsonFilm, Context context) {
        int resp=0;

        boolean present=alreadyPresent(jsonFilm, context);
        if(present==false) {
            boolean insertedSuccessful = false;

            ContentValues testValues = CreateFilmValues(jsonFilm);

            Uri filmnUri = context.getContentResolver().insert(FilmContract.FilmEntry.CONTENT_URI, testValues);
            long filmRowId = ContentUris.parseId(filmnUri);

            if (filmRowId != -1) insertedSuccessful = true;

            if (insertedSuccessful == true) return resp = 1;
            else return resp = 0;
        }else return resp=2;
    }

    boolean alreadyPresent(String jsonFilm,Context context){
        String title=getTitleFromJson(jsonFilm);
            Cursor cursor = selectFilmByTitle(title,context);
            if(cursor.getCount() <= 0)return false;
        return true;
    }

    public ResponseFilm getFilm(String title,Context context){
        Cursor cursor=selectFilmByTitle(title,context);
        ResponseFilm entry=CursorToResponseFilm(cursor);
        return entry;
    }

    public Cursor selectFilmByTitle(String title,Context context){
        String[] params = {title};

        String[] columns = {
                FilmEntry.TITLE_FILM,
                FilmEntry.YEAR,
                FilmEntry.RATED,
                FilmEntry.RELEASED_DATE,
                FilmEntry.RUNTIME,
                FilmEntry.GENRE,
                FilmEntry.DIRECTOR,
                FilmEntry.WRITER,
                FilmEntry.ACTOR,
                FilmEntry.PLOT,
                FilmEntry.AWARDS,
                FilmEntry.RATING,
                FilmEntry.METASCORE,
                FilmEntry.VOTES,
                FilmEntry.POSTER_URL,
                FilmEntry.POSTER,
                FilmEntry.WATCHED,
        };

        Cursor cursor = context.getContentResolver().query(
                FilmEntry.CONTENT_URI,
                columns, // leaving "columns" null just returns all the columns.
                FilmContract.FilmEntry.TITLE_FILM+"=?", // cols for "where" clause
                params, // values for "where" clause
                null  // sort order
        );
        return cursor;
    }

    public boolean updateWatchedFilm(String title,Context context) {
        ContentValues values = new ContentValues();
        values.put(FilmEntry.WATCHED, "true");
        return context.getContentResolver().update(
                FilmEntry.CONTENT_URI, values, FilmEntry.TITLE_FILM + "= ?",
                new String[] {title})>0;
    }

    ResponseFilm CursorToResponseFilm(Cursor cursor){
        ResponseFilm entry= new ResponseFilm();

        if (cursor.moveToFirst()){
            entry.setTitle(cursor.getString(cursor.getColumnIndex(FilmEntry.TITLE_FILM)));
            entry.setRated(cursor.getString(cursor.getColumnIndex(FilmEntry.RATED)));
            entry.setReleased(cursor.getString(cursor.getColumnIndex(FilmEntry.RELEASED_DATE)));
            entry.setRuntime(cursor.getString(cursor.getColumnIndex(FilmEntry.RUNTIME)));
            entry.setGenre(cursor.getString(cursor.getColumnIndex(FilmEntry.GENRE)));
            entry.setDirector(cursor.getString(cursor.getColumnIndex(FilmEntry.DIRECTOR)));
            entry.setWriter(cursor.getString(cursor.getColumnIndex(FilmEntry.WRITER)));
            entry.setActors(cursor.getString(cursor.getColumnIndex(FilmEntry.ACTOR)));
            entry.setPlot(cursor.getString(cursor.getColumnIndex(FilmEntry.PLOT)));
            entry.setAwards(cursor.getString(cursor.getColumnIndex(FilmEntry.AWARDS)));
            entry.setImdbRating(cursor.getString(cursor.getColumnIndex(FilmEntry.RATING)));
            entry.setMetascore(cursor.getString(cursor.getColumnIndex(FilmEntry.METASCORE)));
            entry.setImdbVotes(cursor.getString(cursor.getColumnIndex(FilmEntry.VOTES)));
            entry.setWatched(cursor.getString(cursor.getColumnIndex(FilmEntry.WATCHED)));
        }
        return entry;
    }

    String getTitleFromJson(String jsonFilm){
        ContentValues values=CreateFilmValues(jsonFilm);
        String title= (String) values.get(FilmEntry.TITLE_FILM);
        return title;
    }

    static ContentValues CreateFilmValues(String jsonFilm){
       Gson gson = new Gson();
       ResponseFilm resp = gson.fromJson(jsonFilm,ResponseFilm.class);
       resp.setWatched("false");
       ContentValues values=ObjectToContentValues(resp);
       return values;
   }

    static ContentValues ObjectToContentValues(ResponseFilm resp) {
        ContentValues values = new ContentValues();
        values.put(FilmEntry.TITLE_FILM, resp.getTitle());
        values.put(FilmEntry.YEAR, resp.getYear());
        values.put(FilmEntry.RATED, resp.getRated());
        values.put(FilmEntry.COUNTRY, resp.getCountry());
        values.put(FilmEntry.IMDB_ID, resp.getImdbID());
        values.put(FilmEntry.RELEASED_DATE, resp.getReleased());
        values.put(FilmEntry.RUNTIME, resp.getRuntime());
        values.put(FilmEntry.GENRE, resp.getGenre());
        values.put(FilmEntry.DIRECTOR, resp.getDirector());
        values.put(FilmEntry.WRITER, resp.getWriter());
        values.put(FilmEntry.ACTOR, resp.getActors());
        values.put(FilmEntry.PLOT, resp.getPlot());
        values.put(FilmEntry.AWARDS, resp.getAwards());
        values.put(FilmEntry.RATING, resp.getImdbRating());
        values.put(FilmEntry.METASCORE, resp.getMetascore());
        values.put(FilmEntry.VOTES, resp.getImdbVotes());
        values.put(FilmEntry.POSTER_URL, resp.getPoster());
        values.put(FilmEntry.POSTER, "");
        values.put(FilmEntry.WATCHED, resp.getWatched());
        values.put(FilmEntry.LANGUAGE, resp.getLanguage());
        return values;
    }

    public ArrayList<ResponseFilm> ReadDb(Context context, String orderBy) {
        ArrayList<ResponseFilm> movies = new ArrayList<ResponseFilm>();

        Cursor cursor = getCursorOrdered(context,orderBy/*,db*/);

        if (cursor.moveToFirst())
            do {
                ResponseFilm entryFilm = new ResponseFilm();
                entryFilm.setTitle(cursor.getString(cursor.getColumnIndex(FilmEntry.TITLE_FILM)));
                entryFilm.setWatched(cursor.getString(cursor.getColumnIndex(FilmEntry.WATCHED)));
                if (orderBy.equals("rating"))
                    entryFilm.setImdbRating(cursor.getString(cursor.getColumnIndex(FilmEntry.RATING)));
                else if (orderBy.equals("metascore"))
                    entryFilm.setMetascore(cursor.getString(cursor.getColumnIndex(FilmEntry.METASCORE)));
                else if (orderBy.equals("votes"))
                    entryFilm.setImdbVotes(cursor.getString(cursor.getColumnIndex(FilmEntry.VOTES)));
                movies.add(entryFilm);
            }while(cursor.moveToNext());
        return movies;
    }

    Cursor getCursorOrdered(Context context,String orderBy/*,SQLiteDatabase db*/){
        String[] columns = {
                FilmEntry.TITLE_FILM,
                FilmEntry.RATING,
                FilmEntry.METASCORE,
                FilmEntry.VOTES,
                FilmEntry.WATCHED,
        };

        Cursor cursor = context.getContentResolver().query(
                FilmEntry.CONTENT_URI,
                columns, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                orderBy+" DESC"  // sort order
        );
        return cursor;
    }
}

