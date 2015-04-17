package myself.movieslist;

import android.content.ContentValues;
import android.util.Log;

import com.google.gson.Gson;

import myself.movieslist.data.database.FilmContract;
import myself.movieslist.data.pojo.ResponseFilm;

public class Utility {

    public static String getTitleFromJson(String jsonFilm){
        ContentValues values=CreateFilmValues(jsonFilm);
        String title= (String) values.get(FilmContract.FilmEntry.COLUMN_TITLE_FILM);
        return title;
    }

    public static ContentValues CreateFilmValues(String jsonFilm) {
        Gson gson = new Gson();
        ResponseFilm resp = gson.fromJson(jsonFilm, ResponseFilm.class);
        resp.setWatched("false");
        ContentValues values = ObjectToContentValues(resp);
        return values;
    }

    static ContentValues ObjectToContentValues(ResponseFilm resp) {
        ContentValues values = new ContentValues();
        values.put(FilmContract.FilmEntry.COLUMN_TITLE_FILM, resp.getTitle());
        values.put(FilmContract.FilmEntry.COLUMN_YEAR, resp.getYear());
        values.put(FilmContract.FilmEntry.COLUMN_RATED, resp.getRated());
        values.put(FilmContract.FilmEntry.COLUMN_COUNTRY, resp.getCountry());
        values.put(FilmContract.FilmEntry.COLUMN_IMDB_ID, resp.getImdbID());
        values.put(FilmContract.FilmEntry.COLUMN_RELEASED_DATE, resp.getReleased());
        values.put(FilmContract.FilmEntry.COLUMN_RUNTIME, resp.getRuntime());
        values.put(FilmContract.FilmEntry.COLUMN_GENRE, resp.getGenre());
        values.put(FilmContract.FilmEntry.COLUMN_DIRECTOR, resp.getDirector());
        values.put(FilmContract.FilmEntry.COLUMN_WRITER, resp.getWriter());
        values.put(FilmContract.FilmEntry.COLUMN_ACTOR, resp.getActors());
        values.put(FilmContract.FilmEntry.COLUMN_PLOT, resp.getPlot());
        values.put(FilmContract.FilmEntry.COLUMN_AWARDS, resp.getAwards());
        values.put(FilmContract.FilmEntry.COLUMN_RATING, resp.getImdbRating());
        values.put(FilmContract.FilmEntry.COLUMN_METASCORE, resp.getMetascore());
        values.put(FilmContract.FilmEntry.COLUMN_VOTES, resp.getImdbVotes());
        values.put(FilmContract.FilmEntry.COLUMN_POSTER_URL, resp.getPoster());
        values.put(FilmContract.FilmEntry.COLUMN_POSTER, "");
        values.put(FilmContract.FilmEntry.COLUMN_WATCHED, resp.getWatched());
        values.put(FilmContract.FilmEntry.COLUMN_LANGUAGE, resp.getLanguage());
        return values;
    }

}

