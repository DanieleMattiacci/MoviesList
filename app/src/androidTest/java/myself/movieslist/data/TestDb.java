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



        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        FilmDbHelper dbHelper = new FilmDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = createFilmValues();

        long filmRowId = db.insert(FilmEntry.TABLE_NAME, null, testValues);

        assertTrue(filmRowId != -1);

        String[] columns = {
                FilmEntry.TITLE_FILM,
                FilmEntry.YEAR,
                FilmEntry.RATED,
                FilmEntry.COUNTRY,
                FilmEntry.IMDB_ID,
                FilmEntry.RELEASED_DATE,
                FilmEntry.RUNTIME,
                FilmEntry.GENRE,
                FilmEntry.DIRECTOR,
                FilmEntry.WRITER,
                FilmEntry.ACTOR,
                FilmEntry.PLOT,
                FilmEntry.AWARDS,
                FilmEntry.RATING,
                FilmEntry.METASCORE,
                FilmEntry.VOTES,
                FilmEntry.POSTER_URL,
                FilmEntry.POSTER,
                FilmEntry.WATCHED,
                FilmEntry.LANGUAGE,
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

        // If possible, move to the first row of the query results.
      /*  if (cursor.moveToFirst()) {


            String title = "braveheart";
            String year = "2000";
            String rated = "N/A";
            String country = "USA";
            assertEquals(cursor.getString(cursor.getColumnIndex(FilmEntry.RATED)), rated);
            assertEquals(cursor.getString(cursor.getColumnIndex(FilmEntry.TITLE_FILM)), title);
            assertEquals(cursor.getString(cursor.getColumnIndex(FilmEntry.YEAR)), year);
            assertEquals(cursor.getString(cursor.getColumnIndex(FilmEntry.COUNTRY)), country);
            // Fantastic.  Now that we have a location, add some weather!
        } else {
            // That's weird, it works on MY machine...
            fail("No values returned :(");
        }*/
        dbHelper.close();
    }

    static ContentValues createFilmValues() {

        // Test data we're going to insert into the DB to see if it works.
        String title = "braveheart";
        String year = "2000";
        String rated = "N/A";
        String country = "USA";

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FilmEntry.TITLE_FILM, title);
        values.put(FilmEntry.YEAR, year);
        values.put(FilmEntry.RATED, rated);
        values.put(FilmEntry.COUNTRY, country);
        values.put(FilmEntry.IMDB_ID, title);
        values.put(FilmEntry.RELEASED_DATE, year);
        values.put(FilmEntry.RATED, rated);
        values.put(FilmEntry.RUNTIME, country);
        values.put(FilmEntry.GENRE, title);
        values.put(FilmEntry.DIRECTOR, year);
        values.put(FilmEntry.WRITER, rated);
        values.put(FilmEntry.ACTOR, country);
        values.put(FilmEntry.PLOT, title);
        values.put(FilmEntry.AWARDS, year);
        values.put(FilmEntry.RATING, rated);
        values.put(FilmEntry.METASCORE, country);
        values.put(FilmEntry.VOTES, country);
        values.put(FilmEntry.POSTER_URL, title);
        values.put(FilmEntry.POSTER, year);
        values.put(FilmEntry.WATCHED, rated);
        values.put(FilmEntry.LANGUAGE, country);
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


    /*public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        // Test data we're going to insert into the DB to see if it works.
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, testLocationSetting);
        values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, testCityName);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, testLatitude);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, testLongitude);

        long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Specify which columns you want.
        String[] columns = {
                WeatherContract.LocationEntry._ID,
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
                WeatherContract.LocationEntry.COLUMN_CITY_NAME,
                WeatherContract.LocationEntry.COLUMN_COORD_LAT,
                WeatherContract.LocationEntry.COLUMN_COORD_LONG
        };

        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME,  // Table to Query
                columns,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // If possible, move to the first row of the query results.
        if (cursor.moveToFirst()) {
            // Get the value in each column by finding the appropriate column index.
            int locationIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);

            int nameIndex = cursor.getColumnIndex((WeatherContract.LocationEntry.COLUMN_CITY_NAME));
            String name = cursor.getString(nameIndex);

            int latIndex = cursor.getColumnIndex((WeatherContract.LocationEntry.COLUMN_COORD_LAT));
            double latitude = cursor.getDouble(latIndex);

            int longIndex = cursor.getColumnIndex((WeatherContract.LocationEntry.COLUMN_COORD_LONG));
            double longitude = cursor.getDouble(longIndex);

            // Hooray, data was returned!  Assert that it's the right data, and that the database
            // creation code is working as intended.
            // Then take a break.  We both know that wasn't easy.
            assertEquals(testCityName, name);
            assertEquals(testLocationSetting, location);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);

            // Fantastic.  Now that we have a location, add some weather!
        } else {
            // That's weird, it works on MY machine...
            fail("No values returned :(");
        }

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATETEXT, "20141205");
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, 321);

        dbHelper.close();
    }*/
}
