package myself.movieslist.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import myself.movieslist.data.database.FilmContract.FilmEntry;
import myself.movieslist.data.database.FilmDbHelper;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(FilmDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new FilmDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {
        FilmDbHelper dbHelper = new FilmDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = createFilmValues();

        long filmRowId = db.insert(FilmEntry.TABLE_NAME, null, testValues);

        assertTrue(filmRowId != -1);

        String[] columns = {
                FilmEntry.COLUMN_TITLE_FILM,
                FilmEntry.COLUMN_YEAR,
                FilmEntry.COLUMN_RATED,
                FilmEntry.COLUMN_COUNTRY,
                FilmEntry.COLUMN_IMDB_ID,
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
                FilmEntry.COLUMN_LANGUAGE,
        };

        Cursor cursor = db.query(
                FilmEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        validateCursor(cursor, testValues);
        dbHelper.close();
    }

    static ContentValues createFilmValues() {
        String title = "LOL";
        String year = "2000";
        String rated = "N/A";
        String country = "USA";

        ContentValues values = new ContentValues();
        values.put(FilmEntry.COLUMN_TITLE_FILM, title);
        values.put(FilmEntry.COLUMN_YEAR, year);
        values.put(FilmEntry.COLUMN_RATED, rated);
        values.put(FilmEntry.COLUMN_COUNTRY, country);
        values.put(FilmEntry.COLUMN_IMDB_ID, title);
        values.put(FilmEntry.COLUMN_RELEASED_DATE, year);
        values.put(FilmEntry.COLUMN_RATED, rated);
        values.put(FilmEntry.COLUMN_RUNTIME, country);
        values.put(FilmEntry.COLUMN_GENRE, title);
        values.put(FilmEntry.COLUMN_DIRECTOR, year);
        values.put(FilmEntry.COLUMN_WRITER, rated);
        values.put(FilmEntry.COLUMN_ACTOR, country);
        values.put(FilmEntry.COLUMN_PLOT, title);
        values.put(FilmEntry.COLUMN_AWARDS, year);
        values.put(FilmEntry.COLUMN_RATING, rated);
        values.put(FilmEntry.COLUMN_METASCORE, country);
        values.put(FilmEntry.COLUMN_VOTES, country);
        values.put(FilmEntry.COLUMN_POSTER_URL, title);
        values.put(FilmEntry.COLUMN_POSTER, year);
        values.put(FilmEntry.COLUMN_WATCHED, "false");
        values.put(FilmEntry.COLUMN_LANGUAGE, country);
        return values;
    }

    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {

        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals(expectedValue, valueCursor.getString(idx));
        }
        valueCursor.close();
    }
}
