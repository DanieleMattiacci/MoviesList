package myself.movieslist.data.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daniele on 08/04/15.
 */
public class FilmContract {
    public static final String CONTENT_AUTHORITY = "myself.movieslist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FILM = "film";

   public static final class FilmEntry implements BaseColumns {
         public static final Uri CONTENT_URI =BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILM).build();

        public static final String CONTENT_TYPE ="vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FILM;
        public static final String CONTENT_ITEM_TYPE ="vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FILM;

        public static final String TABLE_NAME = "film";

        public static final String FILM_ID = "film_id";
        public static final String TITLE_FILM = "title_film";
        public static final String IMDB_ID = "imdb_id";
        public static final String YEAR = "year";
        public static final String RELEASED_DATE = "released_date";
        public static final String RATED = "rated";
        public static final String RUNTIME = "runtime";
        public static final String GENRE = "genre";
        public static final String DIRECTOR = "director";
        public static final String WRITER = "writer";
        public static final String ACTOR = "actor";
        public static final String PLOT = "plot";
        public static final String AWARDS = "awards";
        public static final String RATING = "rating";
        public static final String METASCORE = "metascore";
        public static final String VOTES = "votes";
        public static final String POSTER_URL = "poster_url";
        public static final String POSTER = "poster";
        public static final String WATCHED = "watched";
        public static final String LANGUAGE = "language";
        public static final String COUNTRY = "country";

         public static Uri buildFilmUri(long id) {
             return ContentUris.withAppendedId(CONTENT_URI, id);
         }
       public static Uri buildFilmTitle(String titleFilm) {
           return CONTENT_URI.buildUpon().appendPath(titleFilm).build();
       }

       public static String getFilmTitleFromUri(Uri uri) {
           return uri.getPathSegments().get(1);
       }

    }
}
