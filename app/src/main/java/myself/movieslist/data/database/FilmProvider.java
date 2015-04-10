package myself.movieslist.data.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class FilmProvider  extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FilmDbHelper mOpenHelper;

    private static final int FILM = 100;
    private static final int FILM_WITH_TITLE= 101;

    private static final SQLiteQueryBuilder FilmsQueryBuilder;

    static{
        FilmsQueryBuilder = new SQLiteQueryBuilder();
        FilmsQueryBuilder.setTables(
                FilmContract.FilmEntry.TABLE_NAME);
    }

    //location.location_setting = ? AND date = ?
    private static final String sDaySelection =
            FilmContract.FilmEntry.TABLE_NAME +"." + FilmContract.FilmEntry.TITLE_FILM + " = ? ";



    private Cursor getFilmTitleFromUri(Uri uri, String[] projection, String sortOrder) {

        String title = FilmContract.FilmEntry.getFilmTitleFromUri(uri);

        Cursor c = FilmsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sDaySelection,
                new String[]{title},
                null,
                null,
                sortOrder
        );
        return  c;
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FilmContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FilmContract.PATH_FILM, FILM);
        matcher.addURI(authority, FilmContract.PATH_FILM + "/*", FILM_WITH_TITLE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FilmDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/title_film"
            case FILM_WITH_TITLE: {
                retCursor = getFilmTitleFromUri(uri, projection, sortOrder);
                break;
            }
            // "weather"
            case FILM: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FilmContract.FilmEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FILM_WITH_TITLE:
                return FilmContract.FilmEntry.CONTENT_TYPE;
            case FILM:
                return FilmContract.FilmEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FILM: {
                long _id = db.insert(FilmContract.FilmEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FilmContract.FilmEntry.buildFilmUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case FILM:
                rowsDeleted = db.delete(
                        FilmContract.FilmEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case FILM:
                rowsUpdated = db.update(FilmContract.FilmEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FILM:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FilmContract.FilmEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}