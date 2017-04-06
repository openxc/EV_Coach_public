package com.ford.ev_coach;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by benvesel on 4/5/17.
 */

public class DatabaseProvider extends ContentProvider {

    private EVCoachDBHelper dbHelper;
    private static final String TAG = DatabaseProvider.class.getSimpleName();
    private static final String AUTHORITY = "com.ford.ev_coach";

    private static final String BASE_PATH = "ev_coach";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);


    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, 0);
    }

    public boolean onCreate() {
        dbHelper = new EVCoachDBHelper(getContext());
        return false;
    }


    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.d(TAG, "I'm hERE");
        int uriType = sURIMatcher.match(uri);

        //TODO <BMV> http://www.vogella.com/tutorials/AndroidSQLite/article.html
        if(uriType != 0) {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBTableContract.OverviewTableEntry.TABLE_NAME);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Unsuppored Operation in DatabaseProvider.java");

    }

    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Unsuppored Operation in DatabaseProvider.java");
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Unsuppored Operation in DatabaseProvider.java");
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Unsuppored Operation in DatabaseProvider.java");
    }

}
