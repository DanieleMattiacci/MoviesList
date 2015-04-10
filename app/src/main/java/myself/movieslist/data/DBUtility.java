package myself.movieslist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.google.gson.Gson;

import java.util.ArrayList;

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

        boolean present=alreadyPresent(jsonFilm,context);
//Log.i("","Present: "+present);
        if(present==false) {
            boolean insertedSuccessful = false;

            FilmDbHelper dbHelper = new FilmDbHelper(context);

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues testValues = CreateFilmValues(jsonFilm);

            long filmRowId = db.insert(FilmEntry.TABLE_NAME, null, testValues);

            if (filmRowId != -1) insertedSuccessful = true;

            db.close();

            if (insertedSuccessful == true) return resp = 1;
            else return resp = 0;
        }else return resp=2;
       // return resp;
    }

    public boolean deleteFilm(String film, Context context){
        FilmDbHelper dbHelper = new FilmDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(FilmEntry.TABLE_NAME, FilmEntry.TITLE_FILM + "='" + film+"'", null)>0;
    }

    boolean alreadyPresent(String jsonFilm,Context context){
        String title=getTitleFromJson(jsonFilm);
        FilmDbHelper dbHelper = new FilmDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
            String Query = "Select * from " + FilmEntry.TABLE_NAME + " where " + FilmEntry.TITLE_FILM + " = '" + title+"'";
       // Log.i("",Query);
            Cursor cursor = db.rawQuery(Query, null);
            if(cursor.getCount() <= 0){
                db.close();
                return false;
            }
        db.close();
        return true;
    }

    public ResponseFilm selectFilm(String title,Context context){
        FilmDbHelper dbHelper = new FilmDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String Query = "Select * from " + FilmEntry.TABLE_NAME + " where " + FilmEntry.TITLE_FILM + " = '" + title+"'";
        Cursor cursor = db.rawQuery(Query, null);
        ResponseFilm entry=CursorToResponseFilm(cursor);
        return entry;
    }

    public boolean updateWatchedFilm(String title,Context context) {
        FilmDbHelper dbHelper = new FilmDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FilmEntry.WATCHED, "true");
        return db.update(FilmEntry.TABLE_NAME, values, FilmEntry.TITLE_FILM + "='" + title+"'", null)>0;
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
        FilmDbHelper dbHelper = new FilmDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();



        Cursor cursor = getCursorOrdered(orderBy,db);
//Log.i("","Cursor Lenght: "+cursor.getCount());


            if (cursor.moveToFirst()) {
                if (cursor.getCount() == 1) {
                    ResponseFilm entryFilm = new ResponseFilm();
                    entryFilm.setTitle(cursor.getString(cursor.getColumnIndex(FilmEntry.TITLE_FILM)));
                    entryFilm.setWatched(cursor.getString(cursor.getColumnIndex(FilmEntry.WATCHED)));
                    if (orderBy.equals("rating")) {
                        entryFilm.setImdbRating(cursor.getString(cursor.getColumnIndex(FilmEntry.RATING)));
                    } else if (orderBy.equals("metascore")) {
                        entryFilm.setMetascore(cursor.getString(cursor.getColumnIndex(FilmEntry.METASCORE)));
                    } else if (orderBy.equals("votes")) {
                        entryFilm.setImdbVotes(cursor.getString(cursor.getColumnIndex(FilmEntry.VOTES)));
                    }
                   // Log.i("", "Added: " + entryFilm.getTitle()+" "+entryFilm.getImdbRating());
                    movies.add(entryFilm);
                    db.close();
                    return movies;
                } else {
                    int i=0;
                    do {
                       // Log.i("", "Cursor title: " + cursor.getString(cursor.getColumnIndex(FilmEntry.TITLE_FILM)));
                        ResponseFilm entryFilm = new ResponseFilm();
                        entryFilm.setTitle(cursor.getString(cursor.getColumnIndex(FilmEntry.TITLE_FILM)));
                        entryFilm.setWatched(cursor.getString(cursor.getColumnIndex(FilmEntry.WATCHED)));
                        if (orderBy.equals("rating")) {
                            entryFilm.setImdbRating(cursor.getString(cursor.getColumnIndex(FilmEntry.RATING)));
                        } else if (orderBy.equals("metascore")) {
                            entryFilm.setMetascore(cursor.getString(cursor.getColumnIndex(FilmEntry.METASCORE)));
                        } else if (orderBy.equals("votes")) {
                            entryFilm.setImdbVotes(cursor.getString(cursor.getColumnIndex(FilmEntry.VOTES)));
                        }
                       // Log.i("", "Added: " + entryFilm.getTitle());
                        movies.add(entryFilm);
                       // i++;
                        // cursor.moveToNext();
                    }while(cursor.moveToNext());
                    db.close();
                    return movies;
                }
            }else{
                db.close();
                return null;
            }
    }

    Cursor getCursorOrdered(String orderBy,SQLiteDatabase db){
        Cursor cursor = null;

        String[] columns = {
                FilmEntry.TITLE_FILM,
                FilmEntry.RATING,
                FilmEntry.METASCORE,
                FilmEntry.VOTES,
                FilmEntry.WATCHED,
        };

        if (orderBy.equals("rating"))
            cursor = db.query(
                    FilmEntry.TABLE_NAME,  // Table to Query
                    columns,
                    null, // Columns for the "where" clause
                    null, // Values for the "where" clause
                    null, // columns to group by
                    null, // columns to filter by row groups
                    FilmEntry.RATING +" DESC"// sort order
            );
        else if (orderBy.equals("metascore"))
            cursor = db.query(
                    FilmEntry.TABLE_NAME,  // Table to Query
                    columns,
                    null, // Columns for the "where" clause
                    null, // Values for the "where" clause
                    null, // columns to group by
                    null, // columns to filter by row groups
                    FilmEntry.METASCORE+" DESC" // sort order
            );
        else if (orderBy.equals("votes"))
            cursor = db.query(
                    FilmEntry.TABLE_NAME,  // Table to Query
                    columns,
                    null, // Columns for the "where" clause
                    null, // Values for the "where" clause
                    null, // columns to group by
                    null, // columns to filter by row groups
                    FilmEntry.VOTES+" DESC" // sort order
            );
        return cursor;
    }
}

