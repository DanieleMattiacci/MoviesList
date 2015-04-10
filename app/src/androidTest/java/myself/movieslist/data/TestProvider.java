package myself.movieslist.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import myself.movieslist.data.database.FilmContract.FilmEntry;
import myself.movieslist.data.database.FilmDbHelper;

public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void testDeleteDb() throws Throwable {
        mContext.deleteDatabase(FilmDbHelper.DATABASE_NAME);
    }

    public void testInsertReadProvider() {

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        FilmDbHelper dbHelper = new FilmDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestDb.createFilmValues();

        long filmRowId;
        filmRowId = db.insert(FilmEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(filmRowId != -1);
       // Log.d(LOG_TAG, "New row id: " + locationRowId);

        Cursor cursor = mContext.getContentResolver().query(
                FilmEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, testValues);

        dbHelper.close();
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(FilmEntry.CONTENT_URI);
        // vnd.android.cursor.dir/myself.movieslist/film
        assertEquals(FilmEntry.CONTENT_TYPE, type);
    }
}