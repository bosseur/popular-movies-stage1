package br.bosseur.popuplarmoviesapp.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {
    public static final int FAVORITES = 100;
    public static final int FAVORITE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);


        /* This URI is content://br.bosseur.popuplarmoviesapp/favorites/ */
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES, FAVORITES);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITES + "/#", FAVORITE_WITH_ID);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITES:
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case FAVORITE_WITH_ID:
                final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                String idMovie = uri.getPathSegments().get(1);
                String idSelection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] idSelectionArgs = new String[]{idMovie};
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        idSelection,
                        idSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri; // URI to be returned
        if (FAVORITES == sUriMatcher.match(uri)) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
            if (id > 0) {
                returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
            } else {
                throw new android.database.SQLException("Failed to insert row into " + uri);
            }
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int numRowsDeleted;

        if (FAVORITE_WITH_ID == sUriMatcher.match(uri)) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            String idMovie = uri.getPathSegments().get(1);
            // Use selections/selectionArgs to filter for this MOVIE_ID
            numRowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?", new String[]{idMovie});
        } else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
}
