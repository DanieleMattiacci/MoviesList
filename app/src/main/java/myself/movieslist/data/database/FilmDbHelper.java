package myself.movieslist.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import myself.movieslist.data.database.FilmContract.FilmEntry;

public class FilmDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "film.db";

    public FilmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FILM_TABLE = "CREATE TABLE " + FilmEntry.TABLE_NAME + " (" +

                FilmEntry.FILM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FilmEntry.TITLE_FILM + " TEXT NOT NULL, " +
                FilmEntry.IMDB_ID + " TEXT NOT NULL, " +
                FilmEntry.YEAR + " TEXT NOT NULL, " +
                FilmEntry.RELEASED_DATE + " TEXT NOT NULL, " +
                FilmEntry.RATED + " TEXT NOT NULL, " +
                FilmEntry.RUNTIME + " TEXT NOT NULL, " +
                FilmEntry.GENRE  + " TEXT NOT NULL, " +
                FilmEntry.DIRECTOR + " TEXT NOT NULL, " +
                FilmEntry.WRITER + " TEXT NOT NULL, " +
                FilmEntry.ACTOR + " TEXT NOT NULL, " +
                FilmEntry.PLOT + " TEXT NOT NULL, " +
                FilmEntry.AWARDS + " TEXT NOT NULL, " +
                FilmEntry.RATING + " TEXT NOT NULL, " +
                FilmEntry.METASCORE + " TEXT NOT NULL, " +
                FilmEntry.VOTES + " TEXT NOT NULL, " +
                FilmEntry.POSTER_URL + " TEXT NOT NULL, " +
                FilmEntry.POSTER + " TEXT NOT NULL, " +
                FilmEntry. WATCHED + " TEXT NOT NULL, " +
                FilmEntry.LANGUAGE + " TEXT NOT NULL, " +
                FilmEntry.COUNTRY + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_FILM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FilmEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
