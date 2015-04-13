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

                FilmEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FilmEntry.COLUMN_TITLE_FILM + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_IMDB_ID + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_YEAR + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_RELEASED_DATE + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_RATED + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_RUNTIME + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_GENRE  + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_DIRECTOR + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_WRITER + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_ACTOR + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_AWARDS + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_METASCORE + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_VOTES + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_POSTER_URL + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_WATCHED + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_LANGUAGE + " TEXT NOT NULL, " +
                FilmEntry.COLUMN_COUNTRY + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_FILM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FilmEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
