package myself.movieslist.data.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by daniele on 08/04/15.
 */
public class FilmContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "myself.movieslist";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_FILM = "film";

     /* Inner class that defines the table contents of the film table */
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
    }
}
