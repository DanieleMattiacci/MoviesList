package myself.movieslist.data.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FilmContract {
    public static final String CONTENT_AUTHORITY = "myself.movieslist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FILM = "film";

   public static final class FilmEntry implements BaseColumns {
         public static final Uri CONTENT_URI =BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILM).build();

        public static final String CONTENT_TYPE ="vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_FILM;
        public static final String CONTENT_ITEM_TYPE ="vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_FILM;

        public static final String TABLE_NAME = "film";

        //public static final String COLUMN_FILM_ID = "film_id";
        public static final String COLUMN_TITLE_FILM = "title_film";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_RELEASED_DATE = "released_date";
        public static final String COLUMN_RATED = "rated";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_DIRECTOR = "director";
        public static final String COLUMN_WRITER = "writer";
        public static final String COLUMN_ACTOR = "actor";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_AWARDS = "awards";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_METASCORE = "metascore";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_WATCHED = "watched";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_COUNTRY = "country";

         public static Uri buildFilmUri(long id) {
             return ContentUris.withAppendedId(CONTENT_URI, id);
         }
       /*public static Uri buildFilmTitle(String titleFilm) {
           return CONTENT_URI.buildUpon().appendPath(titleFilm).build();
       }*/
       public static Uri buildFilmTitle() {
           return CONTENT_URI.buildUpon().build();
       }

       public static String getFilmTitleFromUri(Uri uri) {
           return uri.getPathSegments().get(1);
       }

    }
}
