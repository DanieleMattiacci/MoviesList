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
        String type = mContext.getContentResolver().getType(FilmEntry.CONTENT_URI);

        assertEquals(FilmEntry.CONTENT_TYPE, type);

        String testFilmTitle = "LOL";

        type = mContext.getContentResolver().getType(
                FilmEntry.buildFilmWithTitle(testFilmTitle));
        assertEquals(FilmEntry.CONTENT_TYPE, type);
    }

    public void testBasicFilmQuery() {
        FilmDbHelper dbHelper = new FilmDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestDb.createFilmValues();
        long filmRowId = insertFilmValues(mContext);

        ContentValues values = TestDb.createFilmValues();

        long filmRowId2 = db.insert(FilmEntry.TABLE_NAME, null, values);
        assertTrue("Unable to Insert FilmEntry into the Database", filmRowId2 != -1);

        db.close();

        Cursor filmCursor = mContext.getContentResolver().query(
                FilmContract.FilmEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
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
        FilmDbHelper dbHelper = new FilmDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestDb.createFilmValues();

        long locationRowId;
        locationRowId = db.insert(FilmEntry.TABLE_NAME, null, testValues);

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
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(cursor, testValues);
    }

    public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                FilmProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: FilmProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + FilmContract.CONTENT_AUTHORITY,
                    providerInfo.authority, FilmContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
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