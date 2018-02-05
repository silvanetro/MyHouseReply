package ro.diamondtech.myhousereply.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user1 on 24/01/2018.
 */

//operations for SQLite databases

public class MyHouseProvider  extends ContentProvider {
    //for Log
    private final String TAG = MyHouseProvider.class.getSimpleName();
    //constant used to match URI with data
    public static final int CODE_MYHOUSEREPLY = 200;
    public static final int CODE_MYHOUSEREPLY_WITH_DATE = 201;

   // The URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher() ;
    private MyHouseReplyDBHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MyHouseRoomContract.CONTENT_AUTHORITY;
        /* This URI is content://ro.diamondtech.myhousereply/myhousereply/ */
        matcher.addURI(authority, MyHouseRoomContract.PATH_MYHOUSEREPLY, CODE_MYHOUSEREPLY);
        // and with data
        matcher.addURI(authority, MyHouseRoomContract.PATH_MYHOUSEREPLY + "/#", CODE_MYHOUSEREPLY_WITH_DATE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MyHouseReplyDBHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int selecturi = sUriMatcher.match(uri);
        switch (selecturi) {

            case CODE_MYHOUSEREPLY:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MyHouseRoomContract.MyHouseRoomEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    Log.i(TAG,"Inserted in db "+Integer.toString(rowsInserted));
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String select, @Nullable String[] selArgs, @Nullable String sortOrd) {
        Cursor cursor;
        int selecturi = sUriMatcher.match(uri);
        switch (selecturi) {
            case CODE_MYHOUSEREPLY_WITH_DATE :{
                cursor = mOpenHelper.getReadableDatabase().query(
                   MyHouseRoomContract.MyHouseRoomEntry.TABLE_NAME,
                        columns,
                        MyHouseRoomContract.MyHouseRoomEntry.COLUMN_DEVICE_CODE+" = ? ",
                        selArgs,
                        null,
                        null,
                        sortOrd
                );
                break;}
            case CODE_MYHOUSEREPLY :{
                cursor = mOpenHelper.getReadableDatabase().query(
                        MyHouseRoomContract.MyHouseRoomEntry.TABLE_NAME,
                        columns,
                        select,
                        selArgs,
                        null,
                        null,
                        sortOrd);


                break;}

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }


        getContext().getContentResolver().notifyChange(uri, null);
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String select, @Nullable String[] selArgs) {
        int nrrowsdeleted =0;
        int selecturi = sUriMatcher.match(uri);

        switch (selecturi) {
            case CODE_MYHOUSEREPLY:{
                nrrowsdeleted=
                        mOpenHelper.getWritableDatabase().delete(
                          MyHouseRoomContract.MyHouseRoomEntry.TABLE_NAME,
                                select,
                                selArgs

                        );
                break;
            }
        }

        return nrrowsdeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int selecturi = sUriMatcher.match(uri);
        switch (selecturi) {
            case CODE_MYHOUSEREPLY:
                //do nothing
                break;
            case CODE_MYHOUSEREPLY_WITH_DATE:
                String id = uri.getPathSegments().get(1);
                selection = MyHouseRoomContract.MyHouseRoomEntry._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(MyHouseRoomContract.MyHouseRoomEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;

    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
