package myself.movieslist.data;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.Map;
import java.util.Set;

import myself.movieslist.data.database.FilmContract;
import myself.movieslist.data.database.FilmContract.FilmEntry;
import myself.movieslist.data.database.FilmDbHelper;
import myself.movieslist.data.database.FilmProvider;


public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                FilmEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                FilmEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    public void setUp() {
        deleteAllRecords();
    }



    public void testGetType() {
        // content://myself.movieslist/film/
        String type = mContext.getContentResolver().getType(FilmEntry.CONTENT_URI);
        // vnd.android.cursor.dirmyself.movieslistp/weather
        assertEquals(FilmEntry.CONTENT_TYPE, type);

        String testFilmTitle = "LOL";
        // content://com.example.android.sunshine.app/film/LOL
        type = mContext.getContentResolver().getType(
                FilmEntry.buildFilmTitle(testFilmTitle));
        assertEquals(FilmEntry.CONTENT_TYPE, type);
    }

    public void testBasicFilmQuery() {
        // insert our test records into the database
        FilmDbHelper dbHelper = new FilmDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestDb.createFilmValues();
        long filmRowId = insertFilmValues(mContext);

        // Fantastic.  Now that we have a location, add some tvseries!
        ContentValues values = TestDb.createFilmValues();

        long filmRowId2 = db.insert(FilmEntry.TABLE_NAME, null, values);
        assertTrue("Unable to Insert FilmEntry into the Database", filmRowId2 != -1);

        db.close();

        // Test the basic content provider query
        Cursor filmCursor = mContext.getContentResolver().query(
                FilmContract.FilmEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        validateCursor("testBasicFilmQuery", filmCursor, values);
    }

    void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    long insertFilmValues(Context context) {
        // insert our test records into the database
        FilmDbHelper dbHelper = new FilmDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestDb.createFilmValues();

        long locationRowId;
        locationRowId = db.insert(FilmEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Film Values", locationRowId != -1);

        return locationRowId;
    }

    public void testInsertReadProvider() {

        ContentValues testValues = TestDb.createFilmValues();

        Uri filmnUri = mContext.getContentResolver().insert(FilmEntry.CONTENT_URI, testValues);
        long filmRowId = ContentUris.parseId(filmnUri);

        assertTrue(filmRowId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                FilmEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, testValues);

        // Now see if we can successfully query if we include the row id
      /*  cursor = mContext.getContentResolver().query(
                FilmEntry.buildFilmUri(filmRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestDb.validateCursor(cursor, testValues);*/
    }

    /*public void testUpdateFilm() {
        ContentValues values = TestDb.createFilmValues();

        Uri filmnUri = mContext.getContentResolver().
                insert(FilmEntry.CONTENT_URI, values);
        long filmRowId = ContentUris.parseId(filmnUri);

        assertTrue(filmRowId != -1);
        Log.d(LOG_TAG, "New row id: " + filmRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(FilmEntry._ID, filmRowId);
        updatedValues.put(FilmEntry.WATCHED, "true");
        String testFilmTitle = "LOL";
        int count = mContext.getContentResolver().update(
                FilmEntry.CONTENT_URI, updatedValues, FilmEntry._ID + "= ?",
                new String[] { testFilmTitle});

        assertEquals(count, 1);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                FilmEntry.buildFilmUri(filmRowId),
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort order
        );

        TestDb.validateCursor(cursor, updatedValues);
    }*/

    public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // tvseriesProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                FilmProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: FilmProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + FilmContract.CONTENT_AUTHORITY,
                    providerInfo.authority, FilmContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: FilmProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void addAllContentValues(ContentValues destination, ContentValues source) {
        for (String key : source.keySet()) {
            destination.put(key, source.getAsString(key));
        }
    }
}